package es.unizar.tmdad.tweelytics.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.util.MimeTypeUtils;

import es.unizar.tmdad.tweelytics.domain.AnalyzedTweet;
import es.unizar.tmdad.tweelytics.entities.AnalyzedTweetListenerContainer;

@Configuration
@PropertySource(value = { "classpath:messagebroker.properties" })
public class MessageBrokerConfig {

	public static final boolean DURABLE_QUEUES = false;
	public static final boolean AUTODELETE_QUEUES = true;
	
	@Value("${rabbitmq.host}")
	private String host;
	
	@Value("${rabbitmq.user}")
	private String user;
	
	@Value("${rabbitmq.pw}")
	private String pw;
	
	@Value("${rabbitmq.vhost}")
	private String vhost;
	
	@Value("${rabbitmq.toProcessorsExchangeName}")
	private String toProcessorsExchangeName;
	
	@Value("${rabbitmq.deliveredTweetQueueName}")
	private String deliveredTweetQueueName;
	
	@Value("${rabbitmq.processorConfigQueueName}")
	private String processorConfigQueueName;
	
	@Autowired
	private SimpMessageSendingOperations messageSendingOperations;
	
	@Bean(name="cachingConnectionFactory")
	public CachingConnectionFactory cachingConnectionFactory(){
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
		connectionFactory.setUsername(user);
		connectionFactory.setPassword(pw);
		connectionFactory.setVirtualHost(vhost);
		
		connectionFactory.setRequestedHeartBeat(30);
		connectionFactory.setConnectionTimeout(30000);
		
		return connectionFactory;
	}
	
	
	@Bean(name="rabbitAdmin")
	@DependsOn("cachingConnectionFactory")
	public RabbitAdmin rabbitAdmin(){
		CachingConnectionFactory connectionFactory = cachingConnectionFactory();
		return new RabbitAdmin(connectionFactory);
	}
	
	@Bean(name="rabbitTemplate")
	@DependsOn("rabbitAdmin")
	public RabbitTemplate rabbitTemplate(){
		CachingConnectionFactory connectionFactory = cachingConnectionFactory();
		RabbitAdmin rabbitAdmin = rabbitAdmin();
		FanoutExchange toProcessorsExchange = new FanoutExchange(toProcessorsExchangeName, DURABLE_QUEUES, AUTODELETE_QUEUES);
		Queue deliveredTweetQueue = new Queue(deliveredTweetQueueName);
		Queue processorConfigQueue = new Queue(processorConfigQueueName);
		rabbitAdmin.declareQueue(deliveredTweetQueue);
		rabbitAdmin.declareQueue(processorConfigQueue);
		BindingBuilder.bind(deliveredTweetQueue).to(toProcessorsExchange);
		BindingBuilder.bind(processorConfigQueue).to(toProcessorsExchange);
		
		return new RabbitTemplate(connectionFactory);
	}
	
	@Bean
	@DependsOn("rabbitTemplate")
	public AnalyzedTweetListenerContainer analyzedTweetListenerContainer(){
		CachingConnectionFactory connectionFactory = cachingConnectionFactory();
		
		AnalyzedTweetListenerContainer container = new AnalyzedTweetListenerContainer(connectionFactory);
		Object listener = new Object() {
			public void handleMessage(AnalyzedTweet analyzedTweet) {
				Map<String, Object> headers = new HashMap<String, Object>();
				headers.put(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);
				messageSendingOperations.convertAndSend("/queue/search/" + analyzedTweet.getAnalyzedBy() + "/" + analyzedTweet.getQueriedTweet().getMyQuery(), analyzedTweet, headers);
			}
		};
		
		MessageListenerAdapter adapter = new MessageListenerAdapter(listener);
		container.setMessageListener(adapter);
		container.start();
		
		return container;
	}
}
