/* STOMP, SockJS, WebSockets */
var webSocketEndpoint = '/twitterSearch';
var subscriptionEndpointPrefix = '/queue/search/';

var stompClient = null;
var subscription = null;
var qTextInput = null;
var resultsBlock = null;
var analyticsResults = null;

// total to normalize emotions
var totalEmotions = 0.0;
// total of single emotion
var totalAnger = 0.0;
var totalJoy = 0.0;
var totalFear = 0.0;
var totalSadness = 0.0;
var totalSurprise = 0.0;

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
	subscription = stompClient.subscribe(subscriptionEndpointPrefix+'Emotion/'+tweetQuery, function(tweet){
        // Convert int to string before parsing => avoid loss of accuracy
		var response = tweet.body.replace(/\"id\":(\d+)/g, function(s, match){
			return "\"id\":\""+match+"\"";
		});
		response = JSON.parse(response);
		var tweetContent = '';
		tweetContent +='<div class="row panel panel-default">'
				+ '<div class="panel-heading">'
				+ '		<a href="https://twitter.com/'+ response.queriedTweet.fromUser +'" target="_blank"><b>@'+ response.queriedTweet.fromUser +'</b></a>'
				+ '		<div class="pull-right">'
				+ '			<a href="https://twitter.com/'+ response.queriedTweet.fromUser +'/status/'+ response.queriedTweet.id +'" target="_blank"><span class="glyphicon glyphicon-link"></span></a>'
				+ '		</div>'
				+ '</div>'
				+ '<div class="panel-body">'+ response.queriedTweet.text +'</div>'
				+ '</div>';

        totalEmotions += response.analyticsResults["Emotion.anger"] + response.analyticsResults["Emotion.joy"]
                + response.analyticsResults["Emotion.fear"] + response.analyticsResults["Emotion.sadness"] + response.analyticsResults["Emotion.surprise"];
        totalAnger += response.analyticsResults["Emotion.anger"];
        totalJoy += response.analyticsResults["Emotion.joy"];
        totalFear += response.analyticsResults["Emotion.fear"];
        totalSadness += response.analyticsResults["Emotion.sadness"];
        totalSurprise += response.analyticsResults["Emotion.surprise"];
        var angerNormalized = totalAnger / totalEmotions;
        var joyNormalized = totalJoy / totalEmotions;
        var fearNormalized = totalFear / totalEmotions;
        var sadnessNormalized = totalSadness / totalEmotions;
        var surpriseNormalized = totalSurprise / totalEmotions;

		// Parse analytics results
		var analyticsContent = '';
		analyticsContent +='<div class="row text-center">'
				+ '		<div class="col-xs-12"><h2>Sentiment analysis (total)</h2></div>'
				+ '		<div class="col-xs-12">Anger: '+totalAnger+'</div>'
				+ '		<div class="col-xs-12">Joy: '+totalJoy+'</div>'
				+ '		<div class="col-xs-12">Fear: '+totalFear+'</div>'
				+ '		<div class="col-xs-12">Sadness: '+totalSadness+'</div>'
				+ '		<div class="col-xs-12">Surprise: '+totalSurprise+'</div>'
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