package ri.controller;

import java.util.ArrayList;
import java.util.HashMap;

import ri.sparql.SparqlClient;
import ri.utils.Utils;

public class MainController {
	
	DBController dbc ;
	HTMLController htmlc ;
	ProcessController processController ;
	SparqlClient sparqlClient ;
	
	public MainController(){
		dbc = new DBController() ;
		htmlc = new HTMLController();
		processController = new ProcessController(dbc);
	}
	
	public void initDB(){
        dbc.connect();
	}
	
	public void initSparqlClient(String endpointUri) {
		sparqlClient = new SparqlClient(endpointUri);
		processController.setSparqlClient(sparqlClient);
	}
	
	public void populateDatabase(){
        String currentPath = System.getProperty("user.dir");
        for( int i = 1 ; i<= 138; i++)
        {
            processController.processFile(currentPath + "/dist/CORPUS/D" +i + ".html", i);
            System.out.println(currentPath + "/dist/CORPUS/D" +i + ".html");
        }
	}
	
	public double getQueryPrecision(int precision_level, String query, int idQuery, int similarity_type, 
			int indexation_type, int sparql_synonyme, int sparql_property){
		HashMap<Integer,Double> map = new HashMap<Integer,Double>();
		double precision;
		map = processController.ProcessQuery(query, similarity_type, indexation_type, 
				sparql_synonyme, sparql_property);
		precision = Utils.getPrecision(map,precision_level,idQuery,processController);
		return precision ;
	}
}