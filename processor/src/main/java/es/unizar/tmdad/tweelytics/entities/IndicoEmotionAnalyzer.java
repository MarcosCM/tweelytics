package es.unizar.tmdad.tweelytics.entities;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

import es.unizar.tmdad.tweelytics.domain.QueriedTweet;

import org.slf4j.Logger;

import io.indico.api.Api;
import io.indico.api.text.Emotion;
import io.indico.api.utils.IndicoException;

public class IndicoEmotionAnalyzer extends AbstractIndicoTextAnalyzer implements EmotionAnalyzer{
	
	private static final Logger logger = LoggerFactory.getLogger(IndicoEmotionAnalyzer.class);
	
	private static final Api indicoQueriedApi = Api.Emotion;
	
	/**
	 * Creates a new text analyzer using the Indico API
	 * @param apiKey API key given by Indico
	 * @throws IndicoException 
	 */
	public IndicoEmotionAnalyzer(String apiKey) throws IndicoException{
		super(apiKey, new Api[]{indicoQueriedApi});
	}
	
	public Map<QueriedTweet, Map<String, Double>> batchAnalysis(List<QueriedTweet> queriedTweets) {
		Map<QueriedTweet, Map<String, Double>> res = new HashMap<QueriedTweet, Map<String, Double>>();
		
		List<Map<Emotion, Double>> emotionResult = null;
		try {
			emotionResult = indicoApiBatchTextAnalysis(queriedTweets, this.getComponentConfig().getParams()).getEmotion();
		} catch (IndicoException | IOException e) {
			logger.info(e.getMessage());
			return null;
		}
		
		int i=0;
		// parse to return type
		Map<String, Double> queriedTweetRes = null;
		for(Map<Emotion, Double> singleEmotionResult : emotionResult){
			queriedTweetRes = new HashMap<String, Double>();
			for(Emotion emotion : singleEmotionResult.keySet()){
				queriedTweetRes.put(this.getType()+"."+emotion.name(), singleEmotionResult.get(emotion));
			}
			res.put(queriedTweets.get(i), queriedTweetRes);
			i++;
		}
		
		return res;
	}

	@Override
	public String getType() {
		return indicoQueriedApi.name();
	}
}
