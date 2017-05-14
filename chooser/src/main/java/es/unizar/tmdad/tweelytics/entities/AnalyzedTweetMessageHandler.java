package es.unizar.tmdad.tweelytics.entities;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.util.MimeTypeUtils;

import es.unizar.tmdad.tweelytics.domain.AnalyzedTweet;

public class AnalyzedTweetMessageHandler {

	private SimpMessageSendingOperations messageSendingOperations;
	private String highlightMode = "<strong>$1</strong>";
	
	public AnalyzedTweetMessageHandler(SimpMessageSendingOperations messageSendingOperations){
		this.messageSendingOperations = messageSendingOperations;
	}
	
	@RabbitListener
	public void handleMessage(AnalyzedTweet analyzedTweet) {
		analyzedTweet.getQueriedTweet().setText(analyzedTweet.getQueriedTweet().getText().replaceAll("(?i)("+analyzedTweet.getQueriedTweet().getMyQuery()+")", highlightMode));
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.put(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);
		messageSendingOperations.convertAndSend("/queue/search/" + analyzedTweet.getAnalyzedBy() + "/" + analyzedTweet.getQueriedTweet().getMyQuery(), analyzedTweet, headers);
	}
	
	public void setHighlightMode(String highlightMode){
		this.highlightMode = highlightMode;
	}
}
