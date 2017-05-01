package es.unizar.tmdad.tweelytics.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import es.unizar.tmdad.tweelytics.entities.Analyzer;
import es.unizar.tmdad.tweelytics.entities.DeliveredTweetListenerContainer;
import es.unizar.tmdad.tweelytics.entities.DeliveredTweetMessageHandler;
import es.unizar.tmdad.tweelytics.entities.ProcessorConfigListenerContainer;
import es.unizar.tmdad.tweelytics.entities.ProcessorConfigMessageHandler;

@Configuration
@PropertySource(value = { "classpath:messagebroker.properties" })
public class MessageBrokerConfig {

	private static final int MAX_QUEUES = 10;
	
	public static final boolean DURABLE_QUEUES = true;
	public static final boolean AUTODELETE_QUEUES = true;
	
	@Value("${rabbitmq.host}")
	private String host;
	
	@Value("${rabbitmq.user}")
	private String user;
	
	@Value("${rabbitmq.pw}")
	private String pw;
	
	@Value("${rabbitmq.vhost}")
	private String vhost;
	
	@Value("${rabbitmq.toProcessorsTweetExchangeName}")
	private String toProcessorsTweetExchangeName;
	
	@Value("${rabbitmq.toProcessorsConfigExchangeName}")
	private String toProcessorsConfigExchangeName;
	
	@Value("${rabbitmq.toChooserExchangeName}")
	private String toChooserExchangeName;
	
	@Value("${rabbitmq.deliveredTweetQueueName}") 
	private String deliveredTweetQueueName;

	@Value("${rabbitmq.processorConfigQueueName}") 
	private String processorConfigQueueName;
	
	@Autowired
	private Analyzer analyzer;
	
	// Queried streams
	// LinkedHashMap to guarantee FIFO replacement when max number of streams is reached (insertion-ordered)
	private LinkedHashMap<String, Queue> queues = new LinkedHashMap<String, Queue>(MAX_QUEUES){
		// Remove oldest entry when max number of streams is reached
		protected boolean removeEldestEntry(Map.Entry<String, Queue> eldest){
			return this.size() > MAX_QUEUES;
		}
	};
	
	@Bean
	public MessageConverter jsonMessageConverter(){
		return new Jackson2JsonMessageConverter();
	}
	
	@Bean
	public CachingConnectionFactory cachingConnectionFactory(){
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
		connectionFactory.setUsername(user);
		connectionFactory.setPassword(pw);
		connectionFactory.setVirtualHost(vhost);
		
		connectionFactory.setRequestedHeartBeat(30);
		connectionFactory.setConnectionTimeout(30000);
		
		return connectionFactory;
	}
	
	@Bean
	public RabbitAdmin rabbitAdmin(){
		CachingConnectionFactory connectionFactory = cachingConnectionFactory();
		return new RabbitAdmin(connectionFactory);
	}
	
	@Bean
	public RabbitTemplate rabbitTemplate(){
		CachingConnectionFactory connectionFactory = cachingConnectionFactory();
		RabbitAdmin rabbitAdmin = rabbitAdmin();
		FanoutExchange toProcessorsTweetExchange = new FanoutExchange(toProcessorsTweetExchangeName, DURABLE_QUEUES, AUTODELETE_QUEUES);
		FanoutExchange toProcessorsConfigExchange = new FanoutExchange(toProcessorsConfigExchangeName, DURABLE_QUEUES, AUTODELETE_QUEUES);
		Queue deliveredTweetQueue = new Queue(deliveredTweetQueueName);
		Queue processorConfigQueue = new Queue(processorConfigQueueName);
		rabbitAdmin.declareQueue(deliveredTweetQueue);
		rabbitAdmin.declareQueue(processorConfigQueue);
		rabbitAdmin.declareBinding(BindingBuilder.bind(deliveredTweetQueue).to(toProcessorsTweetExchange));
		rabbitAdmin.declareBinding(BindingBuilder.bind(processorConfigQueue).to(toProcessorsConfigExchange));
		
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		
		return rabbitTemplate;
	}
	
	@Bean
	public DeliveredTweetListenerContainer deliveredTweetListenerContainer(){
		CachingConnectionFactory connectionFactory = cachingConnectionFactory();
		RabbitTemplate rabbitTemplate = rabbitTemplate();
		
		DeliveredTweetListenerContainer container = new DeliveredTweetListenerContainer(connectionFactory);
		
		container.setMessageListener(new MessageListenerAdapter(new DeliveredTweetMessageHandler(rabbitTemplate, toChooserExchangeName, analyzer), jsonMessageConverter()));
		container.setQueueNames(deliveredTweetQueueName);
		container.start();
		
		return container;
	}
	
	@Bean
	public ProcessorConfigListenerContainer processorConfigListenerContainer(){
		CachingConnectionFactory connectionFactory = cachingConnectionFactory();
		
		ProcessorConfigListenerContainer container = new ProcessorConfigListenerContainer(connectionFactory);
		
		container.setMessageListener(new MessageListenerAdapter(new ProcessorConfigMessageHandler(analyzer), jsonMessageConverter()));
		container.setQueueNames(processorConfigQueueName);
		container.start();
		
		return container;
	}
}
