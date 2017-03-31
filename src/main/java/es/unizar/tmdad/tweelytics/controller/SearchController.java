package es.unizar.tmdad.tweelytics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import es.unizar.tmdad.tweelytics.domain.TwitterQuery;
import es.unizar.tmdad.tweelytics.service.TwitterLookupService;

@Controller
public class SearchController {
	
    @Autowired
    TwitterLookupService twitter;

    @MessageMapping("/search")
    public void search(TwitterQuery twitterQuery) {
    	twitter.search(twitterQuery.getQuery());
    }
}