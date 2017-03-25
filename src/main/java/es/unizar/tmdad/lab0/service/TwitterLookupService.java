package es.unizar.tmdad.lab0.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.social.twitter.api.Stream;
import org.springframework.social.twitter.api.StreamListener;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;

@Service
public class TwitterLookupService {
	
	private static final Logger logger = LoggerFactory.getLogger(TwitterLookupService.class);
	
	private static final int MAX_STREAMS = 10;
	
	@Autowired
	private SimpMessageSendingOperations messagingTemplate;
	
	// Queried streams
	// LinkedHashMap to guarantee FIFO replacement when max number of streams is reached (insertion-ordered)
	private LinkedHashMap<String, Stream> streams = new LinkedHashMap<String, Stream>(MAX_STREAMS){
		// Remove oldest entry when max number of streams is reached
		protected boolean removeEldestEntry(Map.Entry<String, Stream> eldest){
			return this.size() > MAX_STREAMS;
		}
	};
	
	@Value("${twitter.consumerKey}")
	private String consumerKey;
	
	@Value("${twitter.consumerSecret}")
	private String consumerSecret;
	
	@Value("${twitter.accessToken}")
	private String accessToken;
	
	@Value("${twitter.accessTokenSecret}")
	private String accessTokenSecret;
	
	public void search(String query) {
		logger.info("TwitterLUService search called with query: " + query);
		// Do nothing if stream was already queried
		if (streams.containsKey(query)) return;
		
        Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
        List<StreamListener> list = new ArrayList<StreamListener>();
        list.add(new SimpleStreamListener(messagingTemplate, query));
        streams.put(query, twitter.streamingOperations().filter(query,  list));
        logger.info("TwitterLUService added stream for query: " + query);
    }
}
