package es.unizar.tmdad.tweelytics.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Tweet;

import es.unizar.tmdad.tweelytics.repository.TweetRepository;

public class TweetSaver {

	@Autowired
	private TweetRepository tweetRepository;
	
	public TweetSaver(){
		
	}
	
	public void save(Tweet tweet){
		tweetRepository.save(tweet);
	}
}
