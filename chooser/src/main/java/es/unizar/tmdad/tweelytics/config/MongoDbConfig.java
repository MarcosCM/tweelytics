package es.unizar.tmdad.tweelytics.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@Configuration
@EnableMongoRepositories(basePackageClasses={es.unizar.tmdad.tweelytics.repository.TweetRepository.class,
		es.unizar.tmdad.tweelytics.repository.ConfigsRepository.class,
		es.unizar.tmdad.tweelytics.repository.MongoConnectionRepository.class})
@PropertySource(value = { "classpath:database.properties" })
public class MongoDbConfig {
	
	@Value("${mongo.host}")
	private String host;
	
	@Value("${mongo.port}")
	private String port;
	
	@Value("${mongo.db}")
	private String db;
	
	@Value("${mongo.user}")
	private String user;
	
	@Value("${mongo.pw}")
	private String pw;
	
	@Value("${mongo.maxConnectionsPerHost}")
	private String maxConnectionsPerHost;
	
	@Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        ServerAddress sv = new ServerAddress(host, Integer.parseInt(port));
        MongoCredential credential = MongoCredential.createCredential(user, db, pw.toCharArray());
        MongoClientOptions mongoClientOptions = MongoClientOptions.builder().connectionsPerHost(Integer.parseInt(maxConnectionsPerHost)).build();
        MongoClient mongoClient = new MongoClient(sv, Arrays.asList(credential), mongoClientOptions);
        return new SimpleMongoDbFactory(mongoClient, db);
    }
	
	@Bean
	public MongoTemplate mongoTemplate() throws Exception{
		return new MongoTemplate(mongoDbFactory());
	}
    
    public String getHost(){
    	return this.host;
    }
    
    public String getPort(){
    	return this.port;
    }
    
    public String getDb(){
    	return this.db;
    }
}