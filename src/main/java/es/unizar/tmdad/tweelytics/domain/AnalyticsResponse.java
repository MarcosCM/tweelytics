package es.unizar.tmdad.tweelytics.domain;

import java.util.Map;

public class AnalyticsResponse {

	private AnalyzedTweet analyzedTweet;
	
	private Map<String, Float> overallAnalytics;
	
	public AnalyticsResponse(){
		
	}

	public AnalyzedTweet getAnalyzedTweet() {
		return analyzedTweet;
	}

	public void setAnalyzedTweet(AnalyzedTweet analyzedTweet) {
		this.analyzedTweet = analyzedTweet;
	}

	public Map<String, Float> getOverallAnalytics() {
		return overallAnalytics;
	}

	public void setOverallAnalytics(Map<String, Float> overallAnalytics) {
		this.overallAnalytics = overallAnalytics;
	}
	
}
