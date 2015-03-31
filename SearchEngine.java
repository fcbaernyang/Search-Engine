import java.io.*;
import java.util.*;
import java.util.Map.Entry;


public class SearchEngine {
	
	static Map<String,Term> map = new HashMap<String,Term>();
	static Map<String, Integer> docs = new HashMap<String, Integer>();
	//static Map<String, Double> b25 = new HashMap<String, Double>();
	static Map<String, Double> QL = new HashMap<String, Double>();
	
	public static boolean ASC = true;
    public static boolean DESC = false;
	
	static double k1=1.2;
	static double b=.75;
	static double k2=100;
	
	static void printMap(Map<String,Term> map){

		System.out.println("*****START*****");
		for (String name: map.keySet()){

			String key = name.toString();
			String value = map.get(name).toString();  
			System.out.println(key + " " + map.get(key).getTotOccurance());  


		}
		System.out.println("*****END****");
	}
	
	
	static int getTotWords(){
		int tot=0;
		for (String name: map.keySet()){
			String key = name.toString();
			tot+=map.get(key).getTotOccurance();
		}
		return tot;
	}
	
	static double QL(String query, String current, int mu){
		String [] q = query.split(" ");
		
		double fqid=0;
		double cqi=0;
		double lcl= getTotWords();
		double ldl=docs.get(current);
		
		double total=0;
		double log=0;
		
		for(int i=0; i<q.length; i++){
			if(map.get(q[i])!=null){
				fqid=(double)map.get(q[i]).getDocOccurances(current);
				cqi = map.get(q[i]).getTotOccurance();
			}
			else{
				fqid=0;
				cqi=0.000000000000000001;
			}
			double top=(double)fqid+(mu*(cqi/lcl));
			double bottom=ldl+mu;
			double divide=top/bottom;
			if(divide!=0)
				log=Math.log(divide);
			else{
				log=0;
			}
			total=total+log;
		}
		
		return total;
	}
	
	static double B25(String query, String current){
		String[] q=query.split(" ");
		
		double ranking=0;
		double totalvar=0;
		int f1,f2;
		double avdl = getTotWords()/docs.size();
		double dlavdl = docs.get(current)/avdl;
		
		double K = 1.2*(.25+.75*dlavdl);
		
		
		for (String word: q) {
			totalvar=0;
			
			if(map.get(word)!=null){
				f1 = map.get(word).getDocOccurances(current);
				f2 = map.get(word).docs.size();
			}
			else{
				f1=0;
				f2=0;
			}
	
			double var1 = 1/((f2+0.5)/(docs.size()-f2+0.5));
			var1 = Math.log(var1);
			double var2 = ((1.2+1)*f1)/(K+f1);
			double var3 = ((k2+1)*1)/(k2+1);
			
			if (var2==0)
				totalvar=var1*var3;
			else if (var3==0)
				totalvar=var1*var2;
			else
				totalvar=var1*var2*var3;
			
			ranking+=totalvar;
			//System.out.println(ranking);
			
		}
		return ranking;
	}
	
	private static Map<String, Double> sortByComparator(Map<String, Double> b252, final boolean order){
        List<Entry<String, Double>> list = new LinkedList<Entry<String, Double>>(b252.entrySet());
        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Double>>()
        {
            public int compare(Entry<String, Double> o1,
                    Entry<String, Double> o2)
            {
                if (order)
                    return o1.getValue().compareTo(o2.getValue());
                else
                    return o2.getValue().compareTo(o1.getValue());
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
        for (Entry<String, Double> entry : list){
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

	public static void main(String[] args) throws Exception{

		BufferedReader read = new BufferedReader(new FileReader("tccorpus.txt"));
        
		String line = read.readLine();
		while(line!=null && line.startsWith("#")){
			String[] docID=line.split(" ");
			docs.put(docID[1],0);

			line=read.readLine();
			while(line!=null && !line.startsWith("#")){
				String[] tokens = line.split(" ");
				docs.put(docID[1],docs.get(docID[1])+tokens.length);
				
				for(String a : tokens){
					Term word = map.get(a);
					if (word==null)
						word = new Term(a);
					word.addOccurance(docID[1]);
					map.put(word.getTerm(), word);
				}
				line=read.readLine();
			}
		}
		
		//printMap(map); 
		System.out.println("Enter a value for mu");
		Scanner in = new Scanner(System.in);
		int mu = Integer.parseInt(in.nextLine());
		
		System.out.println("Enter a query");
		Scanner sc = new Scanner(System.in);
		String query = sc.nextLine();
		
		File f = new File("output.txt");
        FileWriter fr = new FileWriter(f);
        BufferedWriter br  = new BufferedWriter(fr);
        
        int count =1;
		while(query!=null && !query.equals("")){

			for (String name: docs.keySet()){
				String key = name.toString();
				double rank = QL(query, key, mu);
				QL.put(name,rank);
				//b25.put(name, rank);
				//System.out.println(key+ " " +rank);
			}
			
			Map<String, Double> sortedMapDesc = sortByComparator(QL, DESC);
	        //Map<String, Double> sortedMapDesc = sortByComparator(b25, DESC);
	
	        int counter=1;
	        
	        
	       for(String key: sortedMapDesc.keySet()){
	    	   String a =count+" Q0 " + key + " " +counter+ " " + sortedMapDesc.get(key) + " my_computer\n";
	        	System.out.println(a);
	        	br.write(a);
	        	if(counter==10)
	        		break;
	        	counter++;
	        }
	        
	        System.out.println("\nEnter a query");
	        sc = new Scanner(System.in);
			query = sc.nextLine();
			count++;
			
		}
		
		System.out.println("Done.");
		br.close();
		System.exit(0);
	
		
	}

}
