package es.unizar.tmdad.tweelytics.entities;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

public class AnalyzedTweetListenerContainer extends SimpleMessageListenerContainer {
	
	public AnalyzedTweetListenerContainer(CachingConnectionFactory connectionFactory){
		super(connectionFactory);
	}
}
