package es.unizar.tmdad.tweelytics.entities;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import es.unizar.tmdad.tweelytics.domain.QueriedTweet;
import es.unizar.tmdad.tweelytics.repository.TweetRepository;

public class TweetAccess {

	@Autowired
	private TweetRepository tweetRepository;
	
	public TweetAccess(){
		
	}
	
	public List<QueriedTweet> getTweets(String keyword, int limit){
		return tweetRepository.findByMyQuery(keyword, new PageRequest(0, limit));
	}
}
