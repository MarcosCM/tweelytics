package es.unizar.tmdad.tweelytics.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import es.unizar.tmdad.tweelytics.domain.QueriedTweet;

public class MockTextAnalyzer implements TextAnalyzer{

	private static String[] analyzedEmotions = {"anger", "joy", "fear", "sadness", "surprise"};
	
	@Override
	public Map<String, Double> singleTextAnalysis(QueriedTweet queriedTweet) {
		Random r = new Random();
		
		Map<String, Double> res = new HashMap<String, Double>();
		double max = 1.0;
		double n = 0.0;
		// generate random values for the queried tweet
		for(int i=0; i<analyzedEmotions.length-1; i++){
			n = max * r.nextDouble();
			res.put(analyzedEmotions[i], n);
			max -= n;
		}
		// last value is the remaining till sum is 1.0
		res.put(analyzedEmotions[analyzedEmotions.length-1], max - n);
		
		return res;
	}

	@Override
	public Map<QueriedTweet, Map<String, Double>> batchTextAnalysis(List<QueriedTweet> queriedTweets) {
		Random r = new Random();
		
		Map<QueriedTweet, Map<String, Double>> res = new HashMap<QueriedTweet, Map<String, Double>>(); 
		Map<String, Double> queriedTweetRes;
		double max , min;
		double n;
		for(QueriedTweet queriedTweet : queriedTweets){
			queriedTweetRes = new HashMap<String, Double>();
			max = 1.0; min = 0.0;
			// generate random values for the queried tweet
			for(int i=0; i<analyzedEmotions.length; i++){
				n = min + (max - min) * r.nextDouble();
				queriedTweetRes.put(analyzedEmotions[i], n);
				max = 1 - n;
			}
			// add to result
			res.put(queriedTweet, queriedTweetRes);
		}
		
		return res;
	}

}
