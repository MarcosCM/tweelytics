package es.unizar.tmdad.tweelytics.repository;

import java.math.BigInteger;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.social.twitter.api.Tweet;

public interface TweetRepository extends MongoRepository<Tweet, BigInteger>, TweetRepositoryCustom{

}
