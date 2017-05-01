package es.unizar.tmdad.tweelytics.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import es.unizar.tmdad.tweelytics.domain.QueriedTweet;
import es.unizar.tmdad.tweelytics.repository.TweetRepository;

public class RepositoryTweetSaver implements TweetSaver{

	private static final Logger logger = LoggerFactory.getLogger(RepositoryTweetSaver.class);
	
	@Autowired
	private TweetRepository tweetRepository;
	
	public RepositoryTweetSaver(){
		
	}
	
	public void save(QueriedTweet tweet){
		logger.info("Got tweet: "+tweet.getId()+", by :"+tweet.getFromUser());
		tweetRepository.save(tweet);
	}
}
