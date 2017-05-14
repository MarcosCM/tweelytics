package es.unizar.tmdad.tweelytics.entities;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.unizar.tmdad.tweelytics.domain.ComponentConfig;
import es.unizar.tmdad.tweelytics.domain.QueriedTweet;
import es.unizar.tmdad.tweelytics.repository.ConfigsRepository;

public abstract class AbstractAnalyzer implements Analyzer{
	
	protected ComponentConfig config;
	protected ConfigsRepository configsRepository;
	
	@Override
	public Map<String, Double> singleAnalysis(QueriedTweet queriedTweet){
		return batchAnalysis(Collections.singletonList(queriedTweet)).get(queriedTweet);
	};
	
	@Override
	public abstract Map<QueriedTweet, Map<String, Double>> batchAnalysis(List<QueriedTweet> queriedTweets);
	
	@Override
	public void configAnalyzer(Map<String, Object> params) {
		this.config.getParams().putAll(params);
		if (configsRepository != null) configsRepository.save(config);
	}
	
	public void setConfigsRepository(ConfigsRepository configsRepository){
		this.configsRepository = configsRepository;
	}
	
	public void fillComponentConfig(){
		if (config == null){
			config = configsRepository.findByComponent("processor");
			if (config == null){
				config = new ComponentConfig();
			}
		}
		if (config.getParams() == null) config.setParams(new HashMap<String, Object>());
		if (config.getComponent() == null) config.setComponent("chooser");
		if (config.getParams().get("highlightMode") == null) config.setParam("highlightMode", "<strong>$1</strong>");
	}
	
	public ComponentConfig getComponentConfig(){
		return config;
	}
}
