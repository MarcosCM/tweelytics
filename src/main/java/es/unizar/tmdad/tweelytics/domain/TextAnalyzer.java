package es.unizar.tmdad.tweelytics.domain;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.indico.Indico;
import io.indico.api.Api;
import io.indico.api.results.BatchIndicoResult;
import io.indico.api.utils.IndicoException;

public class TextAnalyzer {
	
	private Indico indico;
	
	/**
	 * Creates a new text analyzer using the Indico API
	 * @param apiKey API key given by Indico
	 * @throws IndicoException 
	 */
	public TextAnalyzer(String apiKey) throws IndicoException{
		this.indico = new Indico(apiKey);
	}
	
	/**
	 * Analyzes a set of predictions over the text of a tweet
	 * @param queriedTweet Tweet being analyzed
	 * @return Result of the predictions by the Indico API
	 * @throws IndicoException
	 * @throws IOException
	 */
	public BatchIndicoResult singleTextAnalysis(QueriedTweet queriedTweet) throws IndicoException, IOException{
		return batchTextAnalysis(Collections.singletonList(queriedTweet),
				//new Api[]{Api.SentimentHQ, Api.Emotion, Api.TwitterEngagement, Api.Political});
				new Api[]{Api.Emotion});
	}
	
	/**
	 * Analyzes a set of predictions over the text of a tweet
	 * @param queriedTweet Tweet being analyzed
	 * @param apiList APIs indicating which predictions to be performed over the tweet
	 * @return Result of the predictions by the Indico API
	 * @throws IndicoException
	 * @throws IOException
	 */
	public BatchIndicoResult singleTextAnalysis(QueriedTweet queriedTweet, Api[] apiList) throws IndicoException, IOException{
		return batchTextAnalysis(Collections.singletonList(queriedTweet), apiList);
	}
	
	/**
	 * Analyzes a set of predictions over the text of a list of tweets
	 * @param myTweet Tweets being analyzed
	 * @param apiList APIs indicating which predictions to be performed over the tweets
	 * @return Result of the predictions by the Indico API
	 * @throws IndicoException
	 * @throws IOException
	 */
	public BatchIndicoResult batchTextAnalysis(List<QueriedTweet> queriedTweets, Api[] apiList) throws IndicoException, IOException{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("apis", apiList);
		return indico.text
				.predict(queriedTweets.stream()
					.map(myTw -> myTw.getOriginalText())
					.collect(Collectors.toList()), params);
	}
}
