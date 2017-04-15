/* STOMP, SockJS, WebSockets */
var webSocketEndpoint = '/twitterSearch';
var subscriptionEndpointPrefix = '/queue/search';

var stompClient = null;
var subscriptions = {};
var qTextInput = null;
var resultsBlock = null;
var analyticsResults = {};
var searchFilters = null;
var total = {};
var normalized = {};
var configParamsNumber = 1;
var configForm = null;
var configParams = null;
var configAddParamBtn = null;

$(document).ready(function() {
	qTextInput = $('input#q');
	searchFilters = $(".search-filter input");
	resultsBlock = $("#resultsBlock");
	configForm = $('#config');
	configParams = $('.config-params');
	configAddParamBtn = $('#configAddParam');
	$("[id$='Results']").each(function(idx){
		var id = $(this).attr('id');
		analyticsResults[id] = $(this);
	});
    // Create websocket
    connectWebSocket();
	registerSearch();
	registerConfig();
});

function registerConfig() {
	configAddParamBtn.click(function(){
		var content = '<div class="col-xs-12">'
	        		+ '		<label for="param'+configParamsNumber+'_id">Parameter name</label><input type="text" name="param'+configParamsNumber+'_id" data-param-id="'+configParamsNumber+'" value="">'
	        		+ '</div>'
	        		+ '<div class="col-xs-12">'
	        		+ '		<label for="param'+configParamsNumber+'_val">Parameter value</label><input type="text" name="param'+configParamsNumber+'_val" value="">'
	        		+ '</div>'
	        		+ '<div class="col-xs-12">'
	        		+ '		<label for="param'+configParamsNumber+'_type">Parameter type (string|int|float|double|boolean)</label><input type="text" name="param'+configParamsNumber+'_type" value="">'
	        		+ '</div>';
	    configParams.append(content);
	    configParamsNumber+=1;
	});

	configForm.submit(function(event){
		var url = configForm.attr('action');
		var data = {};
		$('.config-params input[name$="_id"]').each(function(idx){
			var _ = $(this);
			var param_label = _.val();
			// check if input is empty
			if (param_label){
				var param_value = $('#config input[name="param'+_.attr('data-param-id')+'_val"]').val();
				var param_type = $('#config input[name="param'+_.attr('data-param-id')+'_type"]').val();
				data[param_label] = {};
				data[param_label]['value'] = param_value;
				data[param_label]['type'] = param_type;
			}
		});
		$.post(url, data);

		event.preventDefault();
	});
}

function registerSearch() {
	$("#search").submit(function(event){
		// Clean tweets
		resultsBlock.empty();
		$(analyticsResults).each(function(idx){
			$(this).empty();
		});
		subscribeTweetQuery();

		event.preventDefault();
	});
}

function connectWebSocket() {
	stompClient = Stomp.over(new SockJS(webSocketEndpoint));
	stompClient.connect({}, function(frame) {
		console.log('Connected: ' + frame);
	});
}

function subscribeTweetQuery() {
	var tweetQuery = qTextInput.val();
	// Subscribe new query
	searchFilters.each(function(idx){
		var filterName = $(this).attr('id');

		// Unsubscribe previous query
		if (subscriptions[filterName] != null){
			subscriptions[filterName].unsubscribe();
			subscriptions[filterName] = null;
		}

		// If filter param is checked then subscribe
		if ($(this).is(':checked')){
			subscriptions[filterName] = stompClient.subscribe(subscriptionEndpointPrefix+'/'+filterName+'/'+tweetQuery, function(tweet){
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

				$.each(response.analyticsResults, function(idx, val){
					if (total[idx] == null) total[idx] = val;
					else total[idx] += val;
					if (total[filterName] == null) total[filterName] = val;
					else total[filterName] += val;
				});

				// Parse analytics results
				var analyticsContent = '';
				analyticsContent += '<div class="row text-center">'
						+ '		<div class="col-xs-12"><h2>'+filterName+' analysis (total)</h2></div>';

				$.each(response.analyticsResults, function(idx, val){
					analyticsContent += '<div class="col-xs-12">'+idx+': '+total[idx]+'</div>';
				});

				analyticsContent += '</div>'
						+ '<div class="row text-center">'
		                + '     <div class="col-xs-12"><h2>'+filterName+' analysis (normalized)</h2></div>';

		        $.each(response.analyticsResults, function(idx, val){
		        	normalized[idx] = total[idx] / total[filterName];
					analyticsContent += '<div class="col-xs-12">'+idx+': '+normalized[idx]+'</div>';
				});

		        analyticsContent += '</div>';
				// Set content
				analyticsResults[filterName+'Results'].html(analyticsContent);
				resultsBlock.prepend(tweetContent);
			}, function(error){
				// Error connecting to the endpoint
				console.log('Error: ' + error);
			});
		}
	});

	// Request search stream over the query
	stompClient.send("/app/search", {}, JSON.stringify({'query' : tweetQuery}));
}