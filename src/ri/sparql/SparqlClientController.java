package ri.sparql;

import java.util.ArrayList;
import java.util.Map;

import ri.utils.Utils;

public class SparqlClientController {
	
    public void init(SparqlClient sparqlClient) {
        String query = "ASK WHERE { ?s ?p ?o }";
        boolean serverIsUp = sparqlClient.ask(query);
        if (serverIsUp) {
            System.out.println("Sparql server is UP");
        } else {
            System.out.println("Sparql server is DOWN");
        }
    }
	
    public ArrayList<String> reformulate(SparqlClient sparqlClient, String query){
    	// split query into sub queries (which can have more than one token)
    	String[] motsClesInitials = query.split(", ");
    	int i; 
    	ArrayList<String> listeMotsClesPeuplee = new ArrayList<String>();
    	for(i=0;i < motsClesInitials.length ; i++){
            ArrayList<String> synonymeList = getSynonyme(sparqlClient, motsClesInitials[i]);
            for(String a : synonymeList){
            	listeMotsClesPeuplee.add(Utils.formatString(a));
            }
    	}
    	return listeMotsClesPeuplee ;
    }
    
    public void nbPersonnesParPiece(SparqlClient sparqlClient) {
        String query = "PREFIX : <http://www.lamaisondumeurtre.fr#>\n"
                    + "SELECT ?piece (COUNT(?personne) AS ?nbPers) WHERE+ "
                    + "{\n"
                    + "    ?personne :personneDansPiece ?piece.\n"
                    + "}\n"
                    + "GROUP BY ?piece\n";
            Iterable<Map<String, String>> results = sparqlClient.select(query);
            System.out.println("nombre de personnes par pièce:");
            for (Map<String, String> result : results) {
                System.out.println(result.get("piece") + " : " + result.get("nbPers"));
            }
    }
    
    public ArrayList<String> getSynonyme(SparqlClient sparqlClient, String label){
    	String query = "PREFIX : <http://ontologies.alwaysdata.net/space#>\n"
    		+ "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
    		+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n "
    		+ "PREFIX owl:  <http://www.w3.org/2002/07/owl#>\n "
    		+ "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>\n "
    		+ "SELECT ?class ?all_label\n"
    		+ "WHERE {\n"
    		+ "?class rdfs:label ?label.\n"
    		+ "filter(STR(?label) = \""+label+"\").\n"
    		+ "?class rdfs:label ?all_label\n"
    		+ "}\n"
    		;
    	ArrayList<String> synonymeList = new ArrayList<String>();
        Iterable<Map<String, String>> results = sparqlClient.select(query);
        for (Map<String, String> result : results) {
        	//System.out.println(" * "+result.get("all_label"));
        	if(!label.equals(result)){
        		String synonyme = result.get("all_label") ;
        		String formated_result = Utils.formatString(synonyme);
        		synonymeList.add(formated_result);
        	}
        }
        return synonymeList ;
    }
    
    public void getLabelContainingPrix(SparqlClient sparqlClient){
    	String query = "PREFIX : <http://ontologies.alwaysdata.net/space#>\n"
    		+ "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
    		+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n "
    		+ "PREFIX owl:  <http://www.w3.org/2002/07/owl#>\n "
    		+ "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>\n "
    		
			+ "SELECT ?class ?label\n"
			+ "    				WHERE {\n"
			+ "    				  ?class rdfs:label ?label.\n"
			+ "    				  filter(contains(?label, \"prix\")).\n"
			+ "    				  ?class rdfs:label ?all_label\n"
			+ "    				}\n"
    		;
        Iterable<Map<String, String>> results = sparqlClient.select(query);
        System.out.println("all the lables:");
        for (Map<String, String> result : results) {
            System.out.println(result.get("class") + " : " + result.get("label"));
        }
    }
    
    public ArrayList<String> populateListUsingProperty(SparqlClient sparqlClient, String original_query){
    	String[] subqueries = original_query.split(", ");
    	ArrayList<String> newTokenList = new ArrayList<String>();

    	int size = subqueries.length;
    	int i = 0 ;
    	int j = 0 ;

    	// take every pair that is not repeated
    	for(i=0;i<size;i++){
    		for(j=i+1;j<size;j++){
    			
    			System.out.println(subqueries[i]);
    			System.out.println(subqueries[j]);
    			
    			// first sujet, then property
    	    	String query = "PREFIX : <http://ontologies.alwaysdata.net/space#>\n"
    	    			+ "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
    	    		+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
    	    		+ "PREFIX owl:  <http://www.w3.org/2002/07/owl#>\n"
    	    		+ "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>\n"
    				+ "SELECT ?sujet ?property\n"
    				+ "WHERE {\n"
    	    		+ "?sujet rdfs:label ?label.\n"
    	    		+ "?property rdfs:label ?label1.\n"    	    		
    	    		+ "filter(STR(?label) = \""+subqueries[i]+"\").\n"
    	    		+ "filter(STR(?label1) = \""+subqueries[j]+"\").\n"
    	    		+ "}\n";
    	        Iterable<Map<String, String>> results = sparqlClient.select(query);

    	        for (Map<String, String> result : results) {
    	        	System.out.println(" * "+result.get("all_label"));
    	        	newTokenList.add(result.get("all_label"));
    	        }
    	        
    			// first property, then sujet
    	    	String query_inverse = "PREFIX : <http://ontologies.alwaysdata.net/space#>\n"
    	    			+ "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
    	    		+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
    	    		+ "PREFIX owl:  <http://www.w3.org/2002/07/owl#>\n"
    	    		+ "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>\n"
    				+ "SELECT ?sujet ?property\n"
    				+ "WHERE {\n"
    	    		+ "?sujet rdfs:label ?label.\n"
    	    		+ "?property rdfs:label ?label1.\n"
    	    		+ "filter(STR(?label) = \""+subqueries[j]+"\").\n"
    	    		+ "filter(STR(?label1) = \""+subqueries[i]+"\").\n"
    	    		+ "}\n";
    	        Iterable<Map<String, String>> results_inverse = sparqlClient.select(query_inverse);

    	        for (Map<String, String> result_inverse : results_inverse) {
    	        	System.out.println(" * "+result_inverse.get("all_label"));
    	        	 newTokenList.add(result_inverse.get("all_label"));
    	        }
    		}
    	}
    	return newTokenList ;
    }
    
    public void getLieuNaissanceOmarSy(SparqlClient sparqlClient){
    	String query = "PREFIX : <http://ontologies.alwaysdata.net/space#>\n"
    		+ "PREFIX rdf:  <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
    		+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n "
    		+ "PREFIX owl:  <http://www.w3.org/2002/07/owl#>\n "
    		+ "PREFIX xsd:  <http://www.w3.org/2001/XMLSchema#>\n "	
			+ "SELECT ?sujet ?property ?resource ?all_label\n"
			+ "  				WHERE {\n"
			+ "    				  ?property rdfs:label ?label.\n"
			+ "    				  filter(?label = \"lieu naissance\"@fr).\n"
			
			+ "    				  ?sujet ?property ?resource.\n"
			+ "    				  ?resource rdfs:label ?all_label\n"
			+ "    				}\n"
	    		;
        Iterable<Map<String, String>> results = sparqlClient.select(query);
        System.out.println("all the lables:");
        for (Map<String, String> result : results) {
            System.out.println(result.get("sujet") + " : " + result.get("all_label"));
        }
    }
    
    }
    
