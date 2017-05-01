package es.unizar.tmdad.tweelytics.service;

import java.util.ArrayList;
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
import es.unizar.tmdad.tweelytics.entities.AnalyzedTweetListenerContainer;
import es.unizar.tmdad.tweelytics.service.SimpleStreamListener;

@Service
public class TwitterLookupService {
	
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
		FilterStreamParameters fsp = new FilterStreamParameters();
		fsp.track(query);
		
		List<StreamListener> l = new ArrayList<StreamListener>();
		l.add(new SimpleStreamListener(query, rabbitTemplate, toProcessorsTweetExchangeName));
		
		streams.putIfAbsent(query, twitterTemplate.streamingOperations()
				.filter(fsp, l));
		
		DirectExchange toChooserExchange = new DirectExchange(toChooserExchangeName, MessageBrokerConfig.DURABLE_QUEUES, MessageBrokerConfig.AUTODELETE_QUEUES);
		Queue queryQueue = new Queue(query);
		rabbitAdmin.declareQueue(queryQueue);
		rabbitAdmin.declareBinding(BindingBuilder.bind(queryQueue).to(toChooserExchange).with(query));
		analyzedTweetListenerContainer.addQueueNames(query);
    }
}
