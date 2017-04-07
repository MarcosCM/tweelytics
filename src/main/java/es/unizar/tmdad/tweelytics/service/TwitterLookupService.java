package es.unizar.tmdad.tweelytics.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.social.twitter.api.FilterStreamParameters;
import org.springframework.social.twitter.api.Stream;
import org.springframework.social.twitter.api.StreamListener;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;

import es.unizar.tmdad.tweelytics.entities.EmotionAnalyzer;
import es.unizar.tmdad.tweelytics.entities.PoliticalAnalyzer;
import es.unizar.tmdad.tweelytics.entities.TweetSaver;
import es.unizar.tmdad.tweelytics.entities.TwitterEngagementAnalyzer;
import es.unizar.tmdad.tweelytics.service.SimpleStreamListener;

@Service
public class TwitterLookupService {
	
	@Autowired
	private TweetSaver tweetSaver;
	
	@Autowired
	private SimpMessageSendingOperations messagingTemplate;
	
	@Autowired
	private TwitterTemplate twitterTemplate;
	
	@Autowired
	private EmotionAnalyzer emotionAnalyzer;
	
	@Autowired
	private PoliticalAnalyzer politicalAnalyzer;
	
	@Autowired
	private TwitterEngagementAnalyzer twitterEngagementAnalyzer;
	
	@Value("${twitter.consumerKey}")
	private String consumerKey;
	
	@Value("${twitter.consumerSecret}")
	private String consumerSecret;
	
	@Value("${twitter.accessToken}")
	private String accessToken;
	
	@Value("${twitter.accessTokenSecret}")
	private String accessTokenSecret;
	
	private static final int MAX_STREAMS = 10;
	
	// Queried streams
	// LinkedHashMap to guarantee FIFO replacement when max number of streams is reached (insertion-ordered)
	private LinkedHashMap<String, Stream> streams = new LinkedHashMap<String, Stream>(MAX_STREAMS){
		// Remove oldest entry when max number of streams is reached
		protected boolean removeEldestEntry(Map.Entry<String, Stream> eldest){
			return this.size() > MAX_STREAMS;
		}
	};
	
	public void search(String query) {
		FilterStreamParameters fsp = new FilterStreamParameters();
		fsp.track(query);
		
		List<StreamListener> l = new ArrayList<StreamListener>();
		l.add(new SimpleStreamListener(messagingTemplate, query, emotionAnalyzer, tweetSaver));
		l.add(new SimpleStreamListener(messagingTemplate, query, politicalAnalyzer, tweetSaver));
		l.add(new SimpleStreamListener(messagingTemplate, query, twitterEngagementAnalyzer, tweetSaver));
		
		streams.putIfAbsent(query, twitterTemplate.streamingOperations()
				.filter(fsp, l));
    }
}
