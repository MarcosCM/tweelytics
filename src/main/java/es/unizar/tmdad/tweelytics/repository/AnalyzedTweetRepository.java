package es.unizar.tmdad.tweelytics.repository;

import java.math.BigInteger;

import org.springframework.data.mongodb.repository.MongoRepository;

import es.unizar.tmdad.tweelytics.domain.AnalyzedTweet;

public interface AnalyzedTweetRepository extends MongoRepository<AnalyzedTweet, BigInteger>, AnalyzedTweetRepositoryCustom{

}
