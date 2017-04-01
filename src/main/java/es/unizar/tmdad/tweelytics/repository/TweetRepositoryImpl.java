package es.unizar.tmdad.tweelytics.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TweetRepositoryImpl implements TweetRepositoryCustom {

	private static final String collection = "tweets";
		
	@Autowired
	private MongoTemplate mongoTemplate;
}
