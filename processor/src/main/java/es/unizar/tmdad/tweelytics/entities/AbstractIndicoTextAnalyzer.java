package es.unizar.tmdad.tweelytics.entities;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import es.unizar.tmdad.tweelytics.domain.QueriedTweet;
import io.indico.Indico;
import io.indico.api.Api;
import io.indico.api.results.BatchIndicoResult;
import io.indico.api.utils.IndicoException;

public abstract class AbstractIndicoTextAnalyzer extends AbstractAnalyzer{

	protected Indico indico;
	
	/**
	 * Creates a new text analyzer using the Indico API
	 * @param apiKey API key given by Indico
	 * @throws IndicoException 
	 */
	public AbstractIndicoTextAnalyzer(String apiKey, Api[] indicoQueriedApis) throws IndicoException{
		this.indico = new Indico(apiKey);
		fillComponentConfig();
		this.getComponentConfig().getParams().put("apis", indicoQueriedApis);
	}
	
	/**
	 * Queries the Indico API in order to get the emotion predictions
	 * @param queriedTweets List of tweets
	 * @param params Parameters supplied to the Indico API
	 * @return Results for every tweet
	 * @throws IndicoException
	 * @throws IOException
	 */
	protected BatchIndicoResult indicoApiBatchTextAnalysis(List<QueriedTweet> queriedTweets, Map<String, Object> params) throws IndicoException, IOException{
		return indico.text
				.predict(queriedTweets.stream()
					.map(myTw -> myTw.getOriginalText())
					.collect(Collectors.toList()), params);
	}
}
