package es.unizar.tmdad.tweelytics.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import es.unizar.tmdad.tweelytics.domain.TwitterQuery;
import es.unizar.tmdad.tweelytics.entities.Analyzer;
import es.unizar.tmdad.tweelytics.entities.EmotionAnalyzer;
import es.unizar.tmdad.tweelytics.entities.PoliticalAnalyzer;
import es.unizar.tmdad.tweelytics.entities.TwitterEngagementAnalyzer;
import es.unizar.tmdad.tweelytics.service.TwitterLookupService;

@Controller
public class SearchController {
	
    @Autowired
    private TwitterLookupService twitter;
    
	@Autowired
	private EmotionAnalyzer emotionAnalyzer;
	
	@Autowired
	private PoliticalAnalyzer politicalAnalyzer;
	
	@Autowired
	private TwitterEngagementAnalyzer twitterEngagementAnalyzer;
    
    @MessageMapping("/search")
    public void search(TwitterQuery twitterQuery) {
    	List<Analyzer> abstractAnalyzers = new ArrayList<Analyzer>();
    	abstractAnalyzers.add(emotionAnalyzer);
    	twitter.search(twitterQuery.getQuery(), abstractAnalyzers);
    }
}