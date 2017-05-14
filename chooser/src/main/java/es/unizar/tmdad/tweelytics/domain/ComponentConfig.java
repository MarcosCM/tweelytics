package es.unizar.tmdad.tweelytics.domain;

import java.util.Map;

public class ComponentConfig {

	private Map<String, String> params;
	private String component;
	
	public ComponentConfig(){}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	
	public void setParam(String key, String value) {
		this.params.put(key, value);
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}
}
