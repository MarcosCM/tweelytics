package es.unizar.tmdad.tweelytics.service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.social.twitter.api.FilterStreamParameters;
import org.springframework.social.twitter.api.Stream;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;

import es.unizar.tmdad.tweelytics.entities.TextAnalyzer;
import es.unizar.tmdad.tweelytics.entities.QueryAggregator;
import es.unizar.tmdad.tweelytics.repository.AnalyzedTweetRepository;
import es.unizar.tmdad.tweelytics.service.SimpleStreamListener;

@Service
public class TwitterLookupService {
	
	private static final Logger logger = LoggerFactory.getLogger(TwitterLookupService.class);
	
	@Autowired
	private AnalyzedTweetRepository analyzedTweetRepository;
	
	@Autowired
	private QueryAggregator queryAggregator;
	
	@Autowired
	private SimpMessageSendingOperations messagingTemplate;
	
	@Autowired
	private TwitterTemplate twitterTemplate;
	
	@Autowired
	private TextAnalyzer textAnalyzer;
	
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
		logger.info("TwitterLUService search called with query: " + query);
		FilterStreamParameters fsp = new FilterStreamParameters();
		fsp.track(query);
		//fsp.addLocation(-180, -90, 180, 90);
		
        streams.putIfAbsent(query, twitterTemplate.streamingOperations().filter(fsp, Collections.singletonList(new SimpleStreamListener(messagingTemplate, query, textAnalyzer, analyzedTweetRepository, queryAggregator))));
        logger.info("TwitterLUService added stream for query: " + query);
    }
}
