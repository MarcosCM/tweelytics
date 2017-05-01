package es.unizar.tmdad.tweelytics.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProcessorController {
	
	@Value("${rabbitmq.toProcessorsConfigExchangeName}")
	private String toProcessorsConfigExchangeName;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@RequestMapping(value = "/config", method = RequestMethod.POST)
    public void configProcessor(@RequestParam Map<String, String> params) {
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
		// send message to processors
		rabbitTemplate.convertAndSend(toProcessorsConfigExchangeName, "", parsedParams);
    }
}
