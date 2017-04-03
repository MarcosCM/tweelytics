package es.unizar.tmdad.tweelytics.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.social.twitter.api.StreamDeleteEvent;
import org.springframework.social.twitter.api.StreamListener;
import org.springframework.social.twitter.api.StreamWarningEvent;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.util.MimeTypeUtils;

import es.unizar.tmdad.tweelytics.domain.AnalyticsResponse;
import es.unizar.tmdad.tweelytics.domain.AnalyzedTweet;
import es.unizar.tmdad.tweelytics.domain.QueriedTweet;
import es.unizar.tmdad.tweelytics.domain.QueryAggregator;
import es.unizar.tmdad.tweelytics.domain.TextAnalyzer;
import es.unizar.tmdad.tweelytics.repository.AnalyzedTweetRepository;
import io.indico.api.results.BatchIndicoResult;

public class SimpleStreamListener implements StreamListener {

	private static final Logger logger = LoggerFactory.getLogger(SimpleStreamListener.class);
	
	private SimpMessageSendingOperations messageSendingOperations;
	private String query;
	private TextAnalyzer textAnalyzer;
	private AnalyzedTweetRepository analyzedTweetRepository;
	private QueryAggregator queryAggregator;
	
	public SimpleStreamListener(SimpMessageSendingOperations messageSendingOperations, String query, TextAnalyzer textAnalyzer, AnalyzedTweetRepository analyzedTweetRepository, QueryAggregator queryAggregator){
		this.messageSendingOperations = messageSendingOperations;
		this.query = query;
		this.textAnalyzer = textAnalyzer;
		this.analyzedTweetRepository = analyzedTweetRepository;
		this.queryAggregator = queryAggregator;
	}
	
	@Override
	public void onDelete(StreamDeleteEvent ev) {
		logger.info("Tweet onDelete event triggered on tweet whose ID is: " + ev.getTweetId());
	}

	@Override
	public void onLimit(int limit) {
		logger.info("Tweet onLimit event triggered, limit: " + limit);
	}

	@Override
	public void onTweet(Tweet tweet) {
		logger.info("Received tweet from query "+ query +": " + tweet.getText());
		// save tweet
		QueriedTweet queriedTweet = new QueriedTweet(tweet, query);
		
		BatchIndicoResult res = null;
		try {
			res = textAnalyzer.singleTextAnalysis(queriedTweet);
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		
		AnalyzedTweet analyzedTweet = new AnalyzedTweet();
		analyzedTweet.setQueriedTweet(queriedTweet);
		analyzedTweet.setIndicoResults(res);
		// save in persistent storage
		analyzedTweetRepository.save(analyzedTweet);
		
		// build response
		AnalyticsResponse analyticsResponse = new AnalyticsResponse();
		analyticsResponse.setAnalyzedTweet(analyzedTweet);
		analyticsResponse.setOverallAnalytics(queryAggregator.analyzeQuery(query));
		
		logger.info(analyticsResponse.getOverallAnalytics().toString());
		
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.put(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);
		// (destination, payload, headers)
		messageSendingOperations.convertAndSend("/queue/search/" + query, analyticsResponse, headers);
	}

	@Override
	public void onWarning(StreamWarningEvent ev) {
		logger.info("Tweet onWarning event triggered, code: " + ev.getCode()
										+ "\n\t\tMsg: " + ev.getMessage());
	}

}
