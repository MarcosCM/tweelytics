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

import es.unizar.tmdad.tweelytics.domain.MyTweet;
import es.unizar.tmdad.tweelytics.domain.TextAnalyzer;
import io.indico.api.Api;
import io.indico.api.results.BatchIndicoResult;
import io.indico.api.text.PoliticalClass;

public class SimpleStreamListener implements StreamListener {

	private static final Logger logger = LoggerFactory.getLogger(SimpleStreamListener.class);
	
	private SimpMessageSendingOperations messageSendingOperations;
	private String query;
	private TextAnalyzer textAnalyzer;
	
	public SimpleStreamListener(SimpMessageSendingOperations messageSendingOperations, String query, TextAnalyzer textAnalyzer){
		this.messageSendingOperations = messageSendingOperations;
		this.query = query;
		this.textAnalyzer = textAnalyzer;
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
		MyTweet myTweet = new MyTweet(tweet, query);
		
		Api[] apiList = {Api.SentimentHQ, Api.Political};
		BatchIndicoResult res;
		try {
			res = textAnalyzer.singleTextAnalysis(myTweet, apiList);
			Double sentimentHq = res.getSentimentHQ().get(0);
			Map<PoliticalClass, Double> politicalClasses = res.getPolitical().get(0);
			
			logger.info(String.format("Text: %s, SentimentHQ: %.5f, Libertarian: %.5f, Green: %.5f, Liberal: %.5f, Conservative: %.5f", myTweet.getText(), sentimentHq,
					politicalClasses.get(PoliticalClass.Libertarian), politicalClasses.get(PoliticalClass.Green), politicalClasses.get(PoliticalClass.Liberal), politicalClasses.get(PoliticalClass.Conservative)));
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.put(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);
		// (destination, payload, headers)
		messageSendingOperations.convertAndSend("/queue/search/" + query, myTweet, headers);
	}

	@Override
	public void onWarning(StreamWarningEvent ev) {
		logger.info("Tweet onWarning event triggered, code: " + ev.getCode()
										+ "\n\t\tMsg: " + ev.getMessage());
	}

}
