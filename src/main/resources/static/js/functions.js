/* STOMP, SockJS, WebSockets */
var webSocketEndpoint = '/twitterSearch';
var subscriptionEndpointPrefix = '/queue/search/';

var stompClient = null;
var subscription = null;
var qTextInput = null;
var resultsBlock = null;
var analyticsResults = null;

$(document).ready(function() {
	qTextInput = $('input#q');
	resultsBlock = $("#resultsBlock");
	analyticsResults = $("#analyticsResults");
    // Create websocket
    connectWebSocket();
	registerSearch();
});

function registerSearch() {
	$("#search").submit(function(event){
		// Clean tweets
		resultsBlock.empty();
		subscribeTweetQuery(qTextInput.val());

		event.preventDefault();
	});
}

function connectWebSocket() {
	stompClient = Stomp.over(new SockJS(webSocketEndpoint));
	stompClient.connect({}, function(frame) {
		console.log('Connected: ' + frame);
	});
}

function subscribeTweetQuery(tweetQuery) {
    console.log("TweetQuery: " + tweetQuery);
	// Unsubscribe previous query
	if (subscription != null) subscription.unsubscribe();
	// Subscribe new query
	subscription = stompClient.subscribe(subscriptionEndpointPrefix+tweetQuery, function(tweet){
		console.log('Received: ' + tweet.body);

        // Convert int to string before parsing => avoid loss of accuracy
		var response = tweet.body.replace(/\"id\":(\d+)/g, function(s, match){
			return "\"id\":\""+match+"\"";
		});
		response = JSON.parse(response);
		var tweetContent = '';
		tweetContent +='<div class="row panel panel-default">'
				+ '<div class="panel-heading">'
				+ '		<a href="https://twitter.com/'+ response.analyzedTweet.queriedTweet.fromUser +'" target="_blank"><b>@'+ response.analyzedTweet.queriedTweet.fromUser +'</b></a>'
				+ '		<div class="pull-right">'
				+ '			<a href="https://twitter.com/'+ response.analyzedTweet.queriedTweet.fromUser +'/status/'+ response.analyzedTweet.queriedTweet.id +'" target="_blank"><span class="glyphicon glyphicon-link"></span></a>'
				+ '		</div>'
				+ '</div>'
				+ '<div class="panel-body">'+ response.analyzedTweet.queriedTweet.text +'</div>'
				+ '</div>';

        var totalEmotions = response.overallAnalytics.anger + response.overallAnalytics.joy
                + response.overallAnalytics.fear + response.overallAnalytics.sadness + response.overallAnalytics.surprise;
        var angerNormalized = response.overallAnalytics.anger / totalEmotions;
        var joyNormalized = response.overallAnalytics.joy / totalEmotions;
        var fearNormalized = response.overallAnalytics.fear / totalEmotions;
        var sadnessNormalized = response.overallAnalytics.sadness / totalEmotions;
        var surpriseNormalized = response.overallAnalytics.surprise / totalEmotions;

		// Parse analytics results
		var analyticsContent = '';
		analyticsContent +='<div class="row text-center">'
				+ '		<div class="col-xs-12"><h2>Sentiment analysis (total)</h2></div>'
				+ '		<div class="col-xs-12">Anger: '+response.overallAnalytics.anger+'</div>'
				+ '		<div class="col-xs-12">Joy: '+response.overallAnalytics.joy+'</div>'
				+ '		<div class="col-xs-12">Fear: '+response.overallAnalytics.fear+'</div>'
				+ '		<div class="col-xs-12">Sadness: '+response.overallAnalytics.sadness+'</div>'
				+ '		<div class="col-xs-12">Surprise: '+response.overallAnalytics.surprise+'</div>'
				+ '</div>';
        analyticsContent += '<div class="row text-center">'
                + '     <div class="col-xs-12"><h2>Sentiment analysis (normalized)</h2></div>'
                + '     <div class="col-xs-12">Anger: '+(angerNormalized*100).toFixed(2)+'%</div>'
                + '     <div class="col-xs-12">Joy: '+(joyNormalized*100).toFixed(2)+'%</div>'
                + '     <div class="col-xs-12">Fear: '+(fearNormalized*100).toFixed(2)+'%</div>'
                + '     <div class="col-xs-12">Sadness: '+(sadnessNormalized*100).toFixed(2)+'%</div>'
                + '     <div class="col-xs-12">Surprise: '+(surpriseNormalized*100).toFixed(2)+'%</div>'
                + '</div>';
		// Set content
		analyticsResults.html(analyticsContent);
		resultsBlock.prepend(tweetContent);
	}, function(error){
		// Error connecting to the endpoint
		console.log('Error: ' + error);
	});

	console.log("sending tweet query to /app/search");
	// Request search stream over the query
	stompClient.send("/app/search", {}, JSON.stringify({'query' : tweetQuery}));
}