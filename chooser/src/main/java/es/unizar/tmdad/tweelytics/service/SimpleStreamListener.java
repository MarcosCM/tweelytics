package es.unizar.tmdad.tweelytics.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.social.twitter.api.StreamDeleteEvent;
import org.springframework.social.twitter.api.StreamListener;
import org.springframework.social.twitter.api.StreamWarningEvent;
import org.springframework.social.twitter.api.Tweet;

import es.unizar.tmdad.tweelytics.domain.QueriedTweet;

public class SimpleStreamListener implements StreamListener {

	private static final Logger logger = LoggerFactory.getLogger(SimpleStreamListener.class);
	
	private String query;
	private RabbitTemplate rabbitTemplate;
	private String toProcessorsExchangeName;
	
	public SimpleStreamListener(String query, RabbitTemplate rabbitTemplate, String toProcessorsExchangeName){
		this.query = query;
		this.rabbitTemplate = rabbitTemplate;
		this.toProcessorsExchangeName = toProcessorsExchangeName;
	}
	
	@Override
	public void onDelete(StreamDeleteEvent ev) {
		logger.info("Tweet onDelete event triggered on tweet whose ID is: " + ev.getTweetId());
	}

	@Override
	public void onLimit(int limit) {
		logger.info("Tweet onLimit event triggered, limit: " + limit);
	}

	@Override
	public void onTweet(Tweet tweet) {
		logger.info("Received tweet from query "+ query);
		
		QueriedTweet queriedTweet = new QueriedTweet(tweet, query);
		
		rabbitTemplate.convertAndSend(toProcessorsExchangeName, queriedTweet.getMyQuery(), queriedTweet);
	}

	@Override
	public void onWarning(StreamWarningEvent ev) {
		logger.info("Tweet onWarning event triggered, code: " + ev.getCode()
										+ "\n\t\tMsg: " + ev.getMessage());
	}

}
