package es.unizar.tmdad.tweelytics.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import es.unizar.tmdad.tweelytics.domain.QueriedTweet;

public interface TweetRepository extends MongoRepository<QueriedTweet, BigInteger>, TweetRepositoryCustom{

	List<QueriedTweet> findByMyQuery(String myQuery, Pageable pageable);
}
