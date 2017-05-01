package es.unizar.tmdad.tweelytics.entities;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.unizar.tmdad.tweelytics.domain.QueriedTweet;

public class MockTweetSaver implements TweetSaver {

	private static final Logger logger = LoggerFactory.getLogger(RepositoryTweetSaver.class);
	
	Set<QueriedTweet> tweetRepository = new HashSet<QueriedTweet>();
	
	public void save(QueriedTweet tweet){
		logger.info("Got tweet: "+tweet.getId()+", by :"+tweet.getFromUser());
		tweetRepository.add(tweet);
	}
}
