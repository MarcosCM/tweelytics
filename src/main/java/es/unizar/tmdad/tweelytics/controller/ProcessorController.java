package es.unizar.tmdad.tweelytics.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import es.unizar.tmdad.tweelytics.entities.EmotionAnalyzer;
import es.unizar.tmdad.tweelytics.entities.PoliticalAnalyzer;
import es.unizar.tmdad.tweelytics.entities.TwitterEngagementAnalyzer;

@Controller
public class ProcessorController {
	
	@Autowired
	private EmotionAnalyzer emotionAnalyzer;
	
	@Autowired
	private PoliticalAnalyzer politicalAnalyzer;
	
	@Autowired
	private TwitterEngagementAnalyzer twitterEngagementAnalyzer;
	
	@RequestMapping(value = "/config/{processorId}", method = RequestMethod.POST)
    public void configProcessor(@PathVariable("processorId") String processorId, @RequestParam Map<String, String> params) {
		Map<String, Object> parsedParams = new HashMap<String, Object>();
		// parse to config analyzer format
		params.keySet().stream()
			.filter(k -> k.contains("[type]"))
			.forEach(k -> {
				String newK = k.substring(0, k.length() - "[type]".length());
				String value = newK + "[value]";
				if (params.get(k).toString().equals("int")) parsedParams.put(newK, Integer.parseInt(params.get(value).toString()));
				else if (params.get(k).toString().equals("float")) parsedParams.put(newK, Float.parseFloat(params.get(value).toString()));
				else if (params.get(k).toString().equals("double")) parsedParams.put(newK, Double.parseDouble(params.get(value).toString()));
				else if (params.get(k).toString().equals("boolean")) parsedParams.put(newK, Boolean.parseBoolean(params.get(value).toString()));
				else parsedParams.put(newK, params.get(value).toString());
			});
		// set params
    	if (processorId.equals("Emotion")) emotionAnalyzer.configAnalyzer(parsedParams);
    	else if (processorId.equals("Political")) politicalAnalyzer.configAnalyzer(parsedParams);
    	else if (processorId.equals("TwitterEngagement")) twitterEngagementAnalyzer.configAnalyzer(parsedParams);
    }
}
