package es.unizar.tmdad.tweelytics.entities;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;

import es.unizar.tmdad.tweelytics.domain.QueriedTweet;

import org.slf4j.Logger;

import io.indico.Indico;
import io.indico.api.Api;
import io.indico.api.results.BatchIndicoResult;
import io.indico.api.text.Emotion;
import io.indico.api.utils.IndicoException;

public class IndicoTextAnalyzer implements TextAnalyzer {
	
	private static final Logger logger = LoggerFactory.getLogger(IndicoTextAnalyzer.class);
	
	private Indico indico;
	
	/**
	 * Creates a new text analyzer using the Indico API
	 * @param apiKey API key given by Indico
	 * @throws IndicoException 
	 */
	public IndicoTextAnalyzer(String apiKey) throws IndicoException{
		this.indico = new Indico(apiKey);
	}
	
	public Map<String, Double> singleTextAnalysis(QueriedTweet queriedTweet){
		Map<Emotion, Double> emotionResult = null;
		try {
			emotionResult = indicoApiBatchTextAnalysis(Collections.singletonList(queriedTweet),
					new Api[]{Api.Emotion}).getEmotion().get(0);
		} catch (IndicoException | IOException e) {
			logger.info(e.getMessage());
			return null;
		}
		
		// parse to return type
		Map<String, Double> res = new HashMap<String, Double>();
		for(Emotion emotion : emotionResult.keySet()){
			res.put("emotion."+emotion.name(), emotionResult.get(emotion));
		}
		
		return res;
	}
	
	public Map<QueriedTweet, Map<String, Double>> batchTextAnalysis(List<QueriedTweet> queriedTweets) {
		List<Map<Emotion, Double>> emotionResult = null;
		try {
			emotionResult = indicoApiBatchTextAnalysis(queriedTweets,
					new Api[]{Api.Emotion}).getEmotion();
		} catch (IndicoException | IOException e) {
			logger.info(e.getMessage());
			return null;
		}
		
		int i=0;
		// parse to return type
		Map<QueriedTweet, Map<String, Double>> res = new HashMap<QueriedTweet, Map<String, Double>>();
		Map<String, Double> queriedTweetRes = null;
		for(Map<Emotion, Double> singleEmotionResult : emotionResult){
			queriedTweetRes = new HashMap<String, Double>();
			for(Emotion emotion : singleEmotionResult.keySet()){
				queriedTweetRes.put("emotion."+emotion.name(), singleEmotionResult.get(emotion));
			}
			res.put(queriedTweets.get(i), queriedTweetRes);
			i++;
		}
		
		return res;
	}
	
	private BatchIndicoResult indicoApiBatchTextAnalysis(List<QueriedTweet> queriedTweets, Api[] apiList) throws IndicoException, IOException{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("apis", apiList);
		return indico.text
				.predict(queriedTweets.stream()
					.map(myTw -> myTw.getOriginalText())
					.collect(Collectors.toList()), params);
	}
}
