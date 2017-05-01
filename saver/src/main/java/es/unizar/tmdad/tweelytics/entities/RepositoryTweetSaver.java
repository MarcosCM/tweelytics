package es.unizar.tmdad.tweelytics.entities;

import org.springframework.beans.factory.annotation.Autowired;

import es.unizar.tmdad.tweelytics.domain.QueriedTweet;
import es.unizar.tmdad.tweelytics.repository.TweetRepository;

public class RepositoryTweetSaver implements TweetSaver{

	@Autowired
	private TweetRepository tweetRepository;
	
	public RepositoryTweetSaver(){
		
	}
	
	public void save(QueriedTweet tweet){
		tweetRepository.save(tweet);
	}
}
