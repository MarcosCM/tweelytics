package es.unizar.tmdad.tweelytics.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import es.unizar.tmdad.tweelytics.domain.TwitterQuery;

@Controller
public class ProcessorController {

	private static final Logger logger = LoggerFactory.getLogger(ProcessorController.class);
	
	@RequestMapping("/config/a")
    public void search(TwitterQuery twitterQuery) {
    	logger.info("Accesing to config");
    }
}
