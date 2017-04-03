package es.unizar.tmdad.tweelytics.repository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;

import es.unizar.tmdad.tweelytics.util.FileHelper;

@Repository
public class AnalyzedTweetRepositoryImpl implements AnalyzedTweetRepositoryCustom {
	
	private static final Logger logger = LoggerFactory.getLogger(AnalyzedTweetRepositoryImpl.class);
	
	private static final String collection = "tweets";
		
	@Autowired
	private MongoTemplate mongoTemplate;
	
	private String mapFunction = null, reduceFunction = null;

	@Override
	public Map<String, Float> analyzeTweetsFromQuery(String query) {
		Map<String, Float> res = new HashMap<String, Float>();
		
		// Read functions from files
		if (mapFunction == null){
			try {
				mapFunction = FileHelper.readContent("static/js/mongo/analyticsMapFunction.js");
			}
			catch (IOException e) {
				logger.info(e.getMessage());
			}
		}
		
		if (reduceFunction == null){
			try {
				reduceFunction = FileHelper.readContent("static/js/mongo/analyticsReduceFunction.js");
			}
			catch (IOException e) {
				logger.info(e.getMessage());
			}
		}
			
		// Query the data => input of the map function
		DBObject mongoQuery = new BasicDBObject();
		mongoQuery.put("queriedTweet.myQuery", new BasicDBObject("$eq", query));
		
		// Execute Map-Reduce
		DBCollection collection = mongoTemplate.getDb().getCollection(AnalyzedTweetRepositoryImpl.collection);
		MapReduceCommand cmd = new MapReduceCommand(collection, mapFunction, reduceFunction, null, MapReduceCommand.OutputType.INLINE, mongoQuery);
		MapReduceOutput output = collection.mapReduce(cmd);
		
		// Convert the result
		Iterator<DBObject> iterator = output.results().iterator();
		DBObject currentObj;
		Float currentValue;
		while(iterator.hasNext()){
			currentObj = iterator.next();
			// Convert the values
			currentValue = Float.parseFloat(currentObj.get("value").toString());
			res.put((String) currentObj.get("_id"), currentValue);
		}
		
		return res;
	}
}
