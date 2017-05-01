package es.unizar.tmdad.tweelytics.entities;

import java.util.HashSet;
import java.util.Set;

import es.unizar.tmdad.tweelytics.domain.QueriedTweet;

public class MockTweetSaver implements TweetSaver {

	Set<QueriedTweet> tweetRepository = new HashSet<QueriedTweet>();
	
	public void save(QueriedTweet tweet){
		tweetRepository.add(tweet);
	}
}
