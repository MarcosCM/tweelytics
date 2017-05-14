package es.unizar.tmdad.tweelytics.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.twitter.api.FilterStreamParameters;
import org.springframework.social.twitter.api.Stream;
import org.springframework.social.twitter.api.StreamListener;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;

import es.unizar.tmdad.tweelytics.config.MessageBrokerConfig;
import es.unizar.tmdad.tweelytics.domain.ComponentConfig;
import es.unizar.tmdad.tweelytics.entities.AnalyzedTweetListenerContainer;
import es.unizar.tmdad.tweelytics.repository.ConfigsRepository;
import es.unizar.tmdad.tweelytics.service.SimpleStreamListener;

@Service
public class TwitterLookupService {
	
	@Autowired
	private ConfigsRepository configsRepository;
	
	@Autowired
	private TwitterTemplate twitterTemplate;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private AnalyzedTweetListenerContainer analyzedTweetListenerContainer;
	
	@Autowired
	private RabbitAdmin rabbitAdmin;
	
	@Value("${twitter.consumerKey}")
	private String consumerKey;
	
	@Value("${twitter.consumerSecret}")
	private String consumerSecret;
	
	@Value("${twitter.accessToken}")
	private String accessToken;
	
	@Value("${twitter.accessTokenSecret}")
	private String accessTokenSecret;
	
	@Value("${rabbitmq.toProcessorsTweetExchangeName}")
	private String toProcessorsTweetExchangeName;
	
	@Value("${rabbitmq.toChooserExchangeName}")
	private String toChooserExchangeName;
	
	@Value("${rabbitmq.toChooserQueueName}")
	private String toChooserQueueName;

	private ComponentConfig config;
	
	private static final int MAX_STREAMS = 10;
	
	// Queried streams
	// LinkedHashMap to guarantee FIFO replacement when max number of streams is reached (insertion-ordered)
	private LinkedHashMap<String, Stream> streams = new LinkedHashMap<String, Stream>(MAX_STREAMS){
		// Remove oldest entry when max number of streams is reached
		protected boolean removeEldestEntry(Map.Entry<String, Stream> eldest){
			return this.size() > MAX_STREAMS;
		}
	};
	
	public void search(String query) {
		fillComponentConfig();
		
		FilterStreamParameters fsp = new FilterStreamParameters();
		fsp.track(query);
		
		List<StreamListener> l = new ArrayList<StreamListener>();
		l.add(new SimpleStreamListener(query, rabbitTemplate, toProcessorsTweetExchangeName, config.getParams().get("highlightMode")));
		
		streams.putIfAbsent(query, twitterTemplate.streamingOperations()
				.filter(fsp, l));
		
		DirectExchange toChooserExchange = new DirectExchange(toChooserExchangeName, MessageBrokerConfig.DURABLE_QUEUES, MessageBrokerConfig.AUTODELETE_QUEUES);
		Queue queryQueue = new Queue(toChooserQueueName+query);
		rabbitAdmin.declareQueue(queryQueue);
		rabbitAdmin.declareBinding(BindingBuilder.bind(queryQueue).to(toChooserExchange).with(query));
		analyzedTweetListenerContainer.addQueueNames(toChooserQueueName+query);
    }
	
	public void setParam(String key, String value){
		fillComponentConfig();
		config.getParams().put(key, value);
		configsRepository.save(config);
	}
	
	private void fillComponentConfig(){
		if (config == null){
			config = configsRepository.findByComponent("chooser");
			if (config == null){
				config = new ComponentConfig();
			}
		}
		if (config.getParams() == null) config.setParams(new HashMap<String, String>());
		if (config.getComponent() == null) config.setComponent("chooser");
		if (config.getParams().get("highlightMode") == null) config.setParam("highlightMode", "<strong>$1</strong>");
	}
}