package es.unizar.tmdad.tweelytics.entities;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.unizar.tmdad.tweelytics.domain.QueriedTweet;
import io.indico.api.Api;
import io.indico.api.text.PoliticalClass;
import io.indico.api.utils.IndicoException;

public class IndicoPoliticalAnalyzer extends AbstractIndicoTextAnalyzer implements PoliticalAnalyzer{

	private static final Logger logger = LoggerFactory.getLogger(IndicoPoliticalAnalyzer.class);
	
	private static final Api indicoQueriedApi = Api.Political;
	
	/**
	 * Creates a new text analyzer using the Indico API
	 * @param apiKey API key given by Indico
	 * @throws IndicoException 
	 */
	public IndicoPoliticalAnalyzer(String apiKey) throws IndicoException{
		super(apiKey, new Api[]{indicoQueriedApi});
	}
	
	public Map<QueriedTweet, Map<String, Double>> batchAnalysis(List<QueriedTweet> queriedTweets) {
		Map<QueriedTweet, Map<String, Double>> res = new HashMap<QueriedTweet, Map<String, Double>>();
		
		List<Map<PoliticalClass, Double>> politicalClassResult = null;
		try {
			politicalClassResult = indicoApiBatchTextAnalysis(queriedTweets, this.params).getPolitical();
		} catch (IndicoException | IOException e) {
			logger.info(e.getMessage());
			return null;
		}
		
		int i=0;
		// parse to return type
		Map<String, Double> queriedTweetRes = null;
		for(Map<PoliticalClass, Double> singlePoliticalClassResult : politicalClassResult){
			queriedTweetRes = new HashMap<String, Double>();
			for(PoliticalClass politicalClass : singlePoliticalClassResult.keySet()){
				queriedTweetRes.put(indicoQueriedApi.name()+"."+politicalClass.name(), singlePoliticalClassResult.get(politicalClass));
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
