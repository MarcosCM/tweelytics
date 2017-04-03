package es.unizar.tmdad.tweelytics.domain;

import java.math.BigInteger;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.indico.api.results.BatchIndicoResult;

@Document(collection = "tweets")
public class AnalyzedTweet {

	@Id
	private BigInteger id;
	private QueriedTweet queriedTweet;
	private BatchIndicoResult indicoResults;
	
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

	public BatchIndicoResult getIndicoResults() {
		return indicoResults;
	}

	public void setIndicoResults(BatchIndicoResult indicoResults) {
		this.indicoResults = indicoResults;
	}

	public QueriedTweet getQueriedTweet() {
		return queriedTweet;
	}

	public void setQueriedTweet(QueriedTweet queriedTweet) {
		this.queriedTweet = queriedTweet;
	}
}
