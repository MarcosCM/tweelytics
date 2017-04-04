package es.unizar.tmdad.tweelytics.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import es.unizar.tmdad.tweelytics.domain.QueriedTweet;
import io.indico.api.Api;

public class MockEmotionAnalyzer extends AbstractAnalyzer implements EmotionAnalyzer{

	private static final Api queriedApi = Api.Emotion;
	
	private static String[] analyzedEmotions = {"anger", "joy", "fear", "sadness", "surprise"};

	@Override
	public Map<QueriedTweet, Map<String, Double>> batchAnalysis(List<QueriedTweet> queriedTweets) {
		Map<QueriedTweet, Map<String, Double>> res = new HashMap<QueriedTweet, Map<String, Double>>();
		
		Random r = new Random();
		Map<String, Double> queriedTweetRes;
		List<Double> pseudoRNumbers;
		double max , n, total;
		for(QueriedTweet queriedTweet : queriedTweets){
			pseudoRNumbers = new ArrayList<Double>();
			queriedTweetRes = new HashMap<String, Double>();
			max = 1.0; total = 0.0;
			
			// generate random values for the queried tweet
			for(int i=0; i<analyzedEmotions.length; i++){
				n = max * r.nextDouble();
				pseudoRNumbers.add(n);
				total += n;
			}
			
			// set normalized values
			for(int i=0; i<analyzedEmotions.length; i++){
				queriedTweetRes.put(this.getType()+"."+analyzedEmotions[i], pseudoRNumbers.get(i)/total);
			}
			// add to result
			res.put(queriedTweet, queriedTweetRes);
		}
		
		return res;
	}
	
	@Override
	public String getType() {
		return queriedApi.name();
	}
}
