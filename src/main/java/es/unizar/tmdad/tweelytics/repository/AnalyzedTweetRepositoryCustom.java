package es.unizar.tmdad.tweelytics.repository;

import java.util.Map;

public interface AnalyzedTweetRepositoryCustom {

	Map<String, Float> analyzeTweetsFromQuery(String query);
}
