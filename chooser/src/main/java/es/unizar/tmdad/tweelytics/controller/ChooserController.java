package es.unizar.tmdad.tweelytics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import es.unizar.tmdad.tweelytics.domain.TwitterQuery;
import es.unizar.tmdad.tweelytics.service.TwitterLookupService;

@Controller
public class ChooserController {
	
    @Autowired
    private TwitterLookupService twitter;
    
    @MessageMapping("/search")
    public void search(TwitterQuery twitterQuery) {
    	twitter.search(twitterQuery.getQuery());
    }
    
    @RequestMapping(value="/configChooser", method=RequestMethod.POST)
    public void configChooser(@RequestParam("key") String key, @RequestParam("value") String value){
    	twitter.setParam(key, value);
    }
}