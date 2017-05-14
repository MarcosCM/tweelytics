package es.unizar.tmdad.tweelytics.domain;

import java.math.BigInteger;
import java.util.Map;

import org.springframework.data.annotation.Id;

public class ComponentConfig {

	@Id
	private BigInteger id;
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

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}
}
