package es.unizar.tmdad.tweelytics.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import es.unizar.tmdad.tweelytics.domain.QueriedTweet;

public class QueriedTweetMessageHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(QueriedTweetMessageHandler.class);
	
	private TweetSaver tweetSaver;
	
	public QueriedTweetMessageHandler(TweetSaver tweetSaver){
		this.tweetSaver = tweetSaver;
	}
	
	@RabbitListener
	public void handleMessage(QueriedTweet queriedTweet) {
		/* Experiment: measuring time to save a tweet */
		/*long millis = System.currentTimeMillis();*/
		
		tweetSaver.save(queriedTweet);
		
		/*millis = System.currentTimeMillis() - millis;
		logger.info("Saved tweet in "+millis+" ms");*/
	}
}
