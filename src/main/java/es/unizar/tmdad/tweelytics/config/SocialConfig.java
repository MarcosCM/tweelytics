package es.unizar.tmdad.tweelytics.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.social.TwitterAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

import es.unizar.tmdad.tweelytics.repository.MongoConnectionTransformers;
import es.unizar.tmdad.tweelytics.repository.MongoUsersConnectionRepository;

@Configuration
@EnableSocial
@EnableAutoConfiguration(exclude={TwitterAutoConfiguration.class})
public class SocialConfig implements SocialConfigurer{
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
    @Value("${twitter.consumerKey}")
    private String consumerKey;
 
    @Value("${twitter.consumerSecret}")
    private String consumerSecret;
    
	@Override
	public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment environment) {
		connectionFactoryConfigurer.addConnectionFactory(new TwitterConnectionFactory(consumerKey, consumerSecret));
	}

	@Override
	public UserIdSource getUserIdSource() {
		return new AuthenticationNameUserIdSource();
	}

	@Override
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
		return new MongoUsersConnectionRepository(mongoTemplate, connectionFactoryLocator, new MongoConnectionTransformers(connectionFactoryLocator, Encryptors.noOpText()));
	}

}