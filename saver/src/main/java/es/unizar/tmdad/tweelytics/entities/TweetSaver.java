package es.unizar.tmdad.tweelytics.entities;

import org.springframework.beans.factory.annotation.Autowired;

import es.unizar.tmdad.tweelytics.domain.QueriedTweet;
import es.unizar.tmdad.tweelytics.repository.TweetRepository;

public class TweetSaver {

	@Autowired
	private TweetRepository tweetRepository;
	
	public TweetSaver(){
		
	}
	
	public void save(QueriedTweet tweet){
		tweetRepository.save(tweet);
	}
}
