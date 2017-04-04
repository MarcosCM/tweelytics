package es.unizar.tmdad.tweelytics.domain;

import java.math.BigInteger;
import java.util.Map;

import org.springframework.data.annotation.Id;

public class AnalyzedTweet {

	@Id
	private BigInteger id;
	private QueriedTweet queriedTweet;
	private Map<String, Double> analyticsResults;
	
	public AnalyzedTweet(){
		
	}
	
	public BigInteger getId(){
		return this.id;
	}
	
	public void setId(BigInteger id){
		this.id = id;
	}

	public String getOriginalText() {
		return queriedTweet.getOriginalText();
	}

	public QueriedTweet getQueriedTweet() {
		return queriedTweet;
	}

	public void setQueriedTweet(QueriedTweet queriedTweet) {
		this.queriedTweet = queriedTweet;
	}

	public Map<String, Double> getAnalyticsResults() {
		return analyticsResults;
	}

	public void setAnalyticsResults(Map<String, Double> analyticsResults) {
		this.analyticsResults = analyticsResults;
	}
}
