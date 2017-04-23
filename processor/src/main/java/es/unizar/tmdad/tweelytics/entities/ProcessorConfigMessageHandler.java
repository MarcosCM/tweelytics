package es.unizar.tmdad.tweelytics.entities;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessorConfigMessageHandler {

	private static final Logger logger = LoggerFactory.getLogger(ProcessorConfigMessageHandler.class);
	
	private Analyzer analyzer;
	
	public ProcessorConfigMessageHandler(Analyzer analyzer){
		this.analyzer = analyzer;
	}
	
	public void handleMessage(Map<String, Object> params) {
		logger.info("Processor got config params: " + params.toString());
		analyzer.configAnalyzer(params);
	}
}
