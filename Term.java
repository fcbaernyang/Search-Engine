import java.util.*;


public class Term {
	
	private String term;
	private int occurance;
	//Set<String> docIDs;
	Map<String, Integer> docs = new HashMap<String,Integer>();
	
	public Term(String t){
		term=t;
		occurance=0;
	}

	String getTerm(){
		return term;
	}
	
	int getTotOccurance(){
		return occurance;
	}
	
	void addOccurance(String id){
		occurance++;
		if(docs.get(id)==null)
			docs.put(id, 1);
		else
			docs.put(id, docs.get(id)+1);
		
	}
	
	int getDocOccurances(String id){
		
		if(docs.get(id)==null)
			return 0;
		else
			return docs.get(id);
	}
	

}
