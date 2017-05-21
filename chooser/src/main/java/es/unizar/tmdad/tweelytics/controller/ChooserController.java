package es.unizar.tmdad.tweelytics.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.unizar.tmdad.tweelytics.domain.QueriedTweet;
import es.unizar.tmdad.tweelytics.domain.TwitterQuery;
import es.unizar.tmdad.tweelytics.entities.TweetAccess;
import es.unizar.tmdad.tweelytics.service.TwitterLookupService;

@Controller
public class ChooserController {
	
    @Autowired
    private TwitterLookupService twitter;
    
    @Autowired
    private TweetAccess tweetAccess;
    
    @MessageMapping("/search")
    public void search(TwitterQuery twitterQuery) {
    	twitter.search(twitterQuery.getQuery());
    }
    
    @RequestMapping(value="/configChooser", method=RequestMethod.POST)
    public void configChooser(@RequestParam("key") String key, @RequestParam("value") String value){
    	twitter.setParam(key, value);
    }
    
    @ResponseBody
    @RequestMapping(value="/getSavedTweets", method=RequestMethod.GET)
    public List<QueriedTweet> getTweets(@RequestParam("keyword") String keyword, @RequestParam("limit") String limit){
    	return tweetAccess.getTweets(keyword, Integer.parseInt(limit));
    }
}