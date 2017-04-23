package es.unizar.tmdad.tweelytics.domain;

import java.io.Serializable;
import java.util.Date;

import org.springframework.social.twitter.api.Entities;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterObject;
import org.springframework.social.twitter.api.TwitterProfile;

/**
 * Needed to serialize & deserialize with Jackson 2 lib when using AMQP
 */
public class CustomTweet extends TwitterObject implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private String idStr;
	private String text;
	private Date createdAt;
	private String fromUser;
	private String profileImageUrl;
	private Long toUserId;
	private Long inReplyToStatusId;
	private Long inReplyToUserId;
	private String inReplyToScreenName;
	private long fromUserId;
	private String languageCode;
	private String source;
	private Integer retweetCount;
	private boolean retweeted;
	private boolean favorited;
	private Integer favoriteCount;
	private Entities entities;
	private TwitterProfile user;
	
	public CustomTweet(){}
	
	public CustomTweet(long id, String idStr, String text, Date createdAt, String fromUser, String profileImageUrl,
			Long toUserId, long fromUserId, String languageCode, String source) {
		this.id = id;
		this.idStr = idStr;
		this.text = text;
		this.createdAt = createdAt;
		this.fromUser = fromUser;
		this.profileImageUrl = profileImageUrl;
		this.toUserId = toUserId;
		this.fromUserId = fromUserId;
		this.languageCode = languageCode;
		this.source = source;
	}
	
	public CustomTweet(Tweet tweet){
		this.id = tweet.getId();
		this.idStr = tweet.getIdStr();
		this.text = tweet.getText();
		this.createdAt = tweet.getCreatedAt();
		this.fromUser = tweet.getFromUser();
		this.profileImageUrl = tweet.getProfileImageUrl();
		this.toUserId = tweet.getToUserId();
		this.fromUserId = tweet.getFromUserId();
		this.languageCode = tweet.getLanguageCode();
		this.source = tweet.getSource();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getIdStr() {
		return idStr;
	}

	public void setIdStr(String idStr) {
		this.idStr = idStr;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public Long getToUserId() {
		return toUserId;
	}

	public void setToUserId(Long toUserId) {
		this.toUserId = toUserId;
	}

	public Long getInReplyToStatusId() {
		return inReplyToStatusId;
	}

	public void setInReplyToStatusId(Long inReplyToStatusId) {
		this.inReplyToStatusId = inReplyToStatusId;
	}

	public Long getInReplyToUserId() {
		return inReplyToUserId;
	}

	public void setInReplyToUserId(Long inReplyToUserId) {
		this.inReplyToUserId = inReplyToUserId;
	}

	public String getInReplyToScreenName() {
		return inReplyToScreenName;
	}

	public void setInReplyToScreenName(String inReplyToScreenName) {
		this.inReplyToScreenName = inReplyToScreenName;
	}

	public long getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(long fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Integer getRetweetCount() {
		return retweetCount;
	}

	public void setRetweetCount(Integer retweetCount) {
		this.retweetCount = retweetCount;
	}

	public boolean isRetweeted() {
		return retweeted;
	}

	public void setRetweeted(boolean retweeted) {
		this.retweeted = retweeted;
	}

	public boolean isFavorited() {
		return favorited;
	}

	public void setFavorited(boolean favorited) {
		this.favorited = favorited;
	}

	public Integer getFavoriteCount() {
		return favoriteCount;
	}

	public void setFavoriteCount(Integer favoriteCount) {
		this.favoriteCount = favoriteCount;
	}

	public Entities getEntities() {
		return entities;
	}

	public void setEntities(Entities entities) {
		this.entities = entities;
	}

	public TwitterProfile getUser() {
		return user;
	}

	public void setUser(TwitterProfile user) {
		this.user = user;
	}
}
