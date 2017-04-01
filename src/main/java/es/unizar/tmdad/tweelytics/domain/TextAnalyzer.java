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
	 * @param myTweet Tweet being analyzed
	 * @param apiList APIs indicating which predictions to be performed over the tweet
	 * @return Result of the predictions by the Indico API
	 * @throws IndicoException
	 * @throws IOException
	 */
	public BatchIndicoResult singleTextAnalysis(MyTweet myTweet, Api[] apiList) throws IndicoException, IOException{
		return batchTextAnalysis(Collections.singletonList(myTweet), apiList);
	}
	
	/**
	 * Analyzes a set of predictions over the text of a list of tweets
	 * @param myTweet Tweets being analyzed
	 * @param apiList APIs indicating which predictions to be performed over the tweets
	 * @return Result of the predictions by the Indico API
	 * @throws IndicoException
	 * @throws IOException
	 */
	public BatchIndicoResult batchTextAnalysis(List<MyTweet> myTweets, Api[] apiList) throws IndicoException, IOException{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("apis", apiList);
		return indico.text
				.predict(myTweets.stream()
					.map(myTw -> myTw.getText())
					.collect(Collectors.toList()), params);
	}
}
