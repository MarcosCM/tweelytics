package es.unizar.tmdad.tweelytics.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.social.ApiBinding;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.Twitter;

public class ApiBindingHelper {
	
	/**
	 * Gets the identifier for a specific API
	 * @param api Spring Social API
	 * @return String Identifier
	 */
	public static String getId(ApiBinding api){
		if (api instanceof Twitter) return ApiBindingHelper.getTwitterUserName((Twitter) api);
		else return null;
	}
	
	/**
	 * Gets the Twitter user email
	 * @param twitter Spring Social Twitter object
	 * @return User email
	 */
	public static String getTwitterUserName(Twitter twitter){
		Pattern p = Pattern.compile("http://twitter\\.com/(.*)");
		Matcher m = p.matcher(twitter.userOperations().getUserProfile().getProfileUrl());
		String userName = null;
		if (m.find()){
			userName = m.group(1);
		}
		return userName;
	}

	/**
	 * Gets the API name that the user has been authenticated through
	 * @param securityContext Security Context
	 * @param connectionRepository Connection Repository
	 * @return API name if authed with a social API, otherwise null
	 */
	public static String getAuthThrough(SecurityContext securityContext, ConnectionRepository connectionRepository){
		if (securityContext.getAuthentication() != null){
			if (!connectionRepository.findConnections(Twitter.class).isEmpty()){
				return "twitter";
			}
		}
		return null;
	}
	
	/**
	 * Gets a user identifier depending on the API that they used to be authenticated
	 * @param securityContext Security Context
	 * @param connectionRepository Connection Repository
	 * @return API identifier if authed with a social API, otherwise null
	 */
	public static String getAuthAs(SecurityContext securityContext, ConnectionRepository connectionRepository){
		switch(ApiBindingHelper.getAuthThrough(securityContext, connectionRepository)){
			case "twitter":
				return ApiBindingHelper.getTwitterUserName((Twitter) connectionRepository.getPrimaryConnection(Twitter.class).getApi());
			default:
				return null;
		}
	}
}