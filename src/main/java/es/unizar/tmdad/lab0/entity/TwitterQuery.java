package es.unizar.tmdad.lab0.entity;

public class TwitterQuery {
	
	private String query;
	
	public TwitterQuery(){
		
	}
	
	public TwitterQuery(String query){
		this.query = query;
	}
	
	public String getQuery(){
		return query;
	}
	
	public void setQuery(String query){
		this.query = query;
	}
}
