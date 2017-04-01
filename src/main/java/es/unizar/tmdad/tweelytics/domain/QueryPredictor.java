package es.unizar.tmdad.tweelytics.domain;

import org.springframework.beans.factory.annotation.Autowired;

import es.unizar.tmdad.tweelytics.repository.TweetRepository;

public class QueryPredictor {

	@Autowired
	private TweetRepository tweetRepository;
	
	private String query;
	
	public QueryPredictor(String query){
		this.query = query;
	}
	
	public String getQuery(){
		return this.query;
	}
	
	public void setQuery(String query){
		this.query = query;
	}
	
	
}
