package es.unizar.tmdad.tweelytics.config;

import java.util.LinkedHashMap;
import java.util.List;
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

import es.unizar.tmdad.tweelytics.domain.QueriedTweet;
import es.unizar.tmdad.tweelytics.entities.DeliveredTweetListenerContainer;
import es.unizar.tmdad.tweelytics.repository.TweetRepository;

@Configuration
@PropertySource(value = { "classpath:messagebroker.properties" })
public class MessageBrokerConfig {

	private static final int MAX_QUEUES = 10;
	
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
	
	@Value("${rabbitmq.toSaverExchangeName}")
	private String toSaverExchangeName;
	
	@Value("${rabbitmq.toSaverQueueName}")
	private String toSaverQueueName;
	
	@Autowired
	private TweetRepository tweetRepository;
	
	// Queried streams
	// LinkedHashMap to guarantee FIFO replacement when max number of streams is reached (insertion-ordered)
	private LinkedHashMap<String, Queue> queues = new LinkedHashMap<String, Queue>(MAX_QUEUES){
		// Remove oldest entry when max number of streams is reached
		protected boolean removeEldestEntry(Map.Entry<String, Queue> eldest){
			return this.size() > MAX_QUEUES;
		}
	};
	
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
		FanoutExchange toSaverExchange = new FanoutExchange(toSaverExchangeName, DURABLE_QUEUES, AUTODELETE_QUEUES);
		Queue toSaverQueue = new Queue(toSaverQueueName);
		rabbitAdmin.declareQueue(toSaverQueue);
		BindingBuilder.bind(toSaverQueue).to(toSaverExchange);
		
		return new RabbitTemplate(connectionFactory);
	}
	
	@Bean
	@DependsOn("rabbitTemplate")
	public DeliveredTweetListenerContainer analyzedTweetListenerContainer(){
		CachingConnectionFactory connectionFactory = cachingConnectionFactory();
		
		DeliveredTweetListenerContainer container = new DeliveredTweetListenerContainer(connectionFactory);
		Object listener = new Object() {
			public void handleMessage(QueriedTweet queriedTweet) {
				tweetRepository.save(queriedTweet);
			}
		};
		
		MessageListenerAdapter adapter = new MessageListenerAdapter(listener);
		container.setMessageListener(adapter);
		container.start();
		
		return container;
	}
}
