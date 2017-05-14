package es.unizar.tmdad.tweelytics.domain;

import java.util.Date;
import java.util.Map;

import org.springframework.social.twitter.api.Entities;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;

public class QueriedTweet {

	private CustomTweet customTweet;
	private String myQuery;
	private String text;

	public QueriedTweet(){}
	
	public QueriedTweet(Tweet tweet) {
		this.customTweet = new CustomTweet(tweet);
	}
	
	public QueriedTweet(Tweet tweet, String myQuery) {
		this.customTweet = new CustomTweet(tweet.getId(), String.valueOf(tweet.getId()), tweet.getText(), tweet.getCreatedAt(),
				tweet.getFromUser(), tweet.getProfileImageUrl(), tweet.getToUserId(), tweet.getFromUserId(), tweet.getLanguageCode(), tweet.getSource());
		this.myQuery = myQuery;
		this.text = tweet.getText();
	}
	
	public String getMyQuery(){
		return this.myQuery;
	}
	
	public void setMyQuery(String myQuery){
		this.myQuery = myQuery;
	}
	
	public Map<String, Object> getExtraData() {
		return customTweet.getExtraData();
	}

	public String getOriginalText() {
		return customTweet.getText();
	}
	
	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text != null ? text : customTweet.getText();
	}

	public Date getCreatedAt() {
		return customTweet.getCreatedAt();
	}

	public String getFromUser() {
		return customTweet.getFromUser();
	}

	public void setFromUser(String fromUser) {
		customTweet.setFromUser(fromUser);
	}

	public long getId() {
		return customTweet.getId();
	}

	public String getProfileImageUrl() {
		return customTweet.getProfileImageUrl();
	}

	public void setProfileImageUrl(String profileImageUrl) {
		customTweet.setProfileImageUrl(profileImageUrl);
	}

	public Long getToUserId() {
		return customTweet.getToUserId();
	}

	public void setToUserId(Long toUserId) {
		customTweet.setToUserId(toUserId);
	}

	public long getFromUserId() {
		return customTweet.getFromUserId();
	}

	public void setInReplyToStatusId(Long inReplyToStatusId) {
		customTweet.setInReplyToStatusId(inReplyToStatusId);
	}

	public Long getInReplyToStatusId() {
		return customTweet.getInReplyToStatusId();
	}

	public void setFromUserId(long fromUserId) {
		customTweet.setFromUserId(fromUserId);
	}

	public String getLanguageCode() {
		return customTweet.getLanguageCode();
	}

	public void setLanguageCode(String languageCode) {
		customTweet.setLanguageCode(languageCode);
	}

	public String getSource() {
		return customTweet.getSource();
	}

	public void setSource(String source) {
		customTweet.setSource(source);
	}

	public void setRetweetCount(Integer retweetCount) {
		customTweet.setRetweetCount(retweetCount);
	}

	public Integer getRetweetCount() {
		return customTweet.getRetweetCount();
	}

	public void setRetweeted(boolean retweeted) {
		customTweet.setRetweeted(retweeted);
	}

	public boolean isRetweeted() {
		return customTweet.isRetweeted();
	}

	public void setFavorited(boolean favorited) {
		customTweet.setFavorited(favorited);
	}

	public boolean isFavorited() {
		return customTweet.isFavorited();
	}

	public void setFavoriteCount(Integer favoriteCount) {
		customTweet.setFavoriteCount(favoriteCount);
	}

	public Integer getFavoriteCount() {
		return customTweet.getFavoriteCount();
	}

	public Entities getEntities() {
		return customTweet.getEntities();
	}

	public void setEntities(Entities ent) {
		customTweet.setEntities(ent);
	}

	public TwitterProfile getUser() {
		return customTweet.getUser();
	}

	public void setUser(TwitterProfile prof) {
		customTweet.setUser(prof);
	}

	public Long getInReplyToUserId() {
		return customTweet.getInReplyToUserId();
	}

	public void setInReplyToUserId(Long inReplyToUserId) {
		customTweet.setInReplyToUserId(inReplyToUserId);
	}

	public String getInReplyToScreenName() {
		return customTweet.getInReplyToScreenName();
	}

	public void setInReplyToScreenName(String inReplyToScreenName) {
		customTweet.setInReplyToScreenName(inReplyToScreenName);
	}

	@Override
	public boolean equals(Object o) {
		return customTweet.equals(o);
	}

	@Override
	public int hashCode() {
		return customTweet.hashCode();
	}

	@Override
	public String toString() {
		return customTweet.toString();
	}

	public CustomTweet getCustomTweet() {
		return customTweet;
	}
	
	public void setCustomTweet(CustomTweet customTweet){
		this.customTweet = customTweet;
	}

	public void setTweet(Tweet tweet) {
		this.customTweet = new CustomTweet(tweet.getId(), String.valueOf(tweet.getId()), tweet.getText(), tweet.getCreatedAt(),
				tweet.getFromUser(), tweet.getProfileImageUrl(), tweet.getToUserId(), tweet.getFromUserId(), tweet.getLanguageCode(), tweet.getSource());
	}
	
}
