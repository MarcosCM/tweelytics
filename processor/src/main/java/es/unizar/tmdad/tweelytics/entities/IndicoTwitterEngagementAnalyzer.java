package es.unizar.tmdad.tweelytics.entities;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.unizar.tmdad.tweelytics.domain.QueriedTweet;
import io.indico.api.Api;
import io.indico.api.utils.IndicoException;

public class IndicoTwitterEngagementAnalyzer extends AbstractIndicoTextAnalyzer implements TwitterEngagementAnalyzer{

	private static final Logger logger = LoggerFactory.getLogger(IndicoTwitterEngagementAnalyzer.class);
	
	private static final Api indicoQueriedApi = Api.TwitterEngagement;
	
	/**
	 * Creates a new text analyzer using the Indico API
	 * @param apiKey API key given by Indico
	 * @throws IndicoException 
	 */
	public IndicoTwitterEngagementAnalyzer(String apiKey) throws IndicoException{
		super(apiKey, new Api[]{indicoQueriedApi});
	}
	
	public Map<QueriedTweet, Map<String, Double>> batchAnalysis(List<QueriedTweet> queriedTweets) {
		Map<QueriedTweet, Map<String, Double>> res = new HashMap<QueriedTweet, Map<String, Double>>();
		
		List<Double> twitterEngagementResult = null;
		try {
			twitterEngagementResult = indicoApiBatchTextAnalysis(queriedTweets, this.getComponentConfig().getParams()).getTwitterEngagement();
		} catch (IndicoException | IOException e) {
			logger.info(e.getMessage());
			return null;
		}
		
		int i=0;
		// parse to return type
		Map<String, Double> queriedTweetRes = null;
		for(Double singleTwitterEngagementResult : twitterEngagementResult){
			queriedTweetRes = new HashMap<String, Double>();
			queriedTweetRes.put(indicoQueriedApi.name()+"."+indicoQueriedApi.name(), singleTwitterEngagementResult);
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
