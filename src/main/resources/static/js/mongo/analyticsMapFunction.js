function(){
	for(var key in this.analyticsResults){
		emit(key, this.analyticsResults[key]);
	}
}