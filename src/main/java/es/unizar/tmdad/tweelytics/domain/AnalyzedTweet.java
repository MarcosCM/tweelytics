package es.unizar.tmdad.tweelytics.domain;

import java.math.BigInteger;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.indico.api.results.BatchIndicoResult;

@Document(collection = "tweets")
public class AnalyzedTweet {

	@Id
	private BigInteger id;
	private MyTweet myTweet;
	private String tweetText;
	private BatchIndicoResult indicoResults;
	
	public AnalyzedTweet(){
		
	}
	
	public BigInteger getId(){
		return this.id;
	}
	
	public void setId(BigInteger id){
		this.id = id;
	}

	public String getTweetText() {
		return tweetText;
	}

	public void setTweetText(String tweetText) {
		this.tweetText = tweetText;
	}

	public BatchIndicoResult getIndicoResults() {
		return indicoResults;
	}

	public void setIndicoResults(BatchIndicoResult indicoResults) {
		this.indicoResults = indicoResults;
	}

	public MyTweet getMyTweet() {
		return myTweet;
	}

	public void setMyTweet(MyTweet myTweet) {
		this.myTweet = myTweet;
	}
}
