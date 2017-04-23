package es.unizar.tmdad.tweelytics.entities;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import es.unizar.tmdad.tweelytics.domain.QueriedTweet;
import es.unizar.tmdad.tweelytics.repository.TweetRepository;

public class QueriedTweetMessageHandler {
	
	private TweetRepository tweetRepository;
	
	public QueriedTweetMessageHandler(TweetRepository tweetRepository){
		this.tweetRepository = tweetRepository;
	}
	
	@RabbitListener
	public void handleMessage(QueriedTweet queriedTweet) {
		tweetRepository.save(queriedTweet);
	}
}
