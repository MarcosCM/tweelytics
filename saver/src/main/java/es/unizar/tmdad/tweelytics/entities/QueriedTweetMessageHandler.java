package es.unizar.tmdad.tweelytics.entities;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import es.unizar.tmdad.tweelytics.domain.QueriedTweet;

public class QueriedTweetMessageHandler {
	
	private TweetSaver tweetSaver;
	
	public QueriedTweetMessageHandler(TweetSaver tweetSaver){
		this.tweetSaver = tweetSaver;
	}
	
	@RabbitListener
	public void handleMessage(QueriedTweet queriedTweet) {
		tweetSaver.save(queriedTweet);
	}
}
