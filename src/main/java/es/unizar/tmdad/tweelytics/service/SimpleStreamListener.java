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

import es.unizar.tmdad.tweelytics.domain.AnalyzedTweet;
import es.unizar.tmdad.tweelytics.domain.QueriedTweet;
import es.unizar.tmdad.tweelytics.entities.TweetSaver;
import es.unizar.tmdad.tweelytics.entities.Analyzer;

public class SimpleStreamListener implements StreamListener {

	private static final Logger logger = LoggerFactory.getLogger(SimpleStreamListener.class);
	
	private SimpMessageSendingOperations messageSendingOperations;
	private String query;
	private Analyzer analyzer;
	private TweetSaver tweetSaver;
	
	public SimpleStreamListener(SimpMessageSendingOperations messageSendingOperations, String query, Analyzer analyzer, TweetSaver tweetSaver){
		this.messageSendingOperations = messageSendingOperations;
		this.query = query;
		this.analyzer = analyzer;
		this.tweetSaver = tweetSaver;
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
		// save in persistent storage
		tweetSaver.save(tweet);
		// save tweet
		QueriedTweet queriedTweet = new QueriedTweet(tweet, query);
		
		Map<String, Double> res = null;
		try {
			res = analyzer.singleAnalysis(queriedTweet);
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		
		AnalyzedTweet analyzedTweet = new AnalyzedTweet();
		analyzedTweet.setQueriedTweet(queriedTweet);
		analyzedTweet.setAnalyticsResults(res);
		
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.put(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);
		// (destination, payload, headers)
		messageSendingOperations.convertAndSend("/queue/search/" + analyzer.getType() + "/" + query, analyzedTweet, headers);
	}

	@Override
	public void onWarning(StreamWarningEvent ev) {
		logger.info("Tweet onWarning event triggered, code: " + ev.getCode()
										+ "\n\t\tMsg: " + ev.getMessage());
	}

}
