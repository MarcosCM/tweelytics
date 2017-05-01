package es.unizar.tmdad.tweelytics.entities;

import es.unizar.tmdad.tweelytics.domain.QueriedTweet;

public interface TweetSaver {

	public void save(QueriedTweet tweet);
}
