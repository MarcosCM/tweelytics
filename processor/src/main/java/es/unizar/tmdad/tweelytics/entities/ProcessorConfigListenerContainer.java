package es.unizar.tmdad.tweelytics.entities;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

public class ProcessorConfigListenerContainer extends SimpleMessageListenerContainer{

	public ProcessorConfigListenerContainer(CachingConnectionFactory connectionFactory){
		super(connectionFactory);
	}
}
