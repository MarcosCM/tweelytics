package es.unizar.tmdad.tweelytics.repository;

import java.math.BigInteger;

import org.springframework.data.mongodb.repository.MongoRepository;

import es.unizar.tmdad.tweelytics.domain.QueriedTweet;

public interface TweetRepository extends MongoRepository<QueriedTweet, BigInteger>, TweetRepositoryCustom{

}
