function(){
	for(var key in this.indicoResults.results.emotion[0]){
		emit(key, this.indicoResults.results.emotion[0][key]);
	}
}