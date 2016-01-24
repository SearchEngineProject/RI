package ri.main;

import ri.controller.*;
import ri.utils.*;
import java.util.HashMap;

public class Main {
    
    public static void main(String[] args) {
        // TODO code application logic here org.jsoup.jsoup.connect("http://example.com/").get();
        try{
        	
        	String query1 = "personnes, Intouchables";
        	String query2 = "lieu naissance, Omar Sy";
        	String query3 = "personnes recompensées, Intouchables";
        	String query4 = "palmarès, Globes de Cristal 2012";
        	String query5 = "membre jury, Globes de Cristal 2012";
        	String query6 = "prix, Omar Sy, Globes de Cristal 2012";
        	String query7 = "lieu, Globes Cristal 2012";
        	String query8 = "prix, Omar Sy";
        	String query9 = "acteur, joué avec, Omar Sy";
        	
        	MainController mainController = new MainController();
        	mainController.initDB();
        	mainController.initSparqlClient("localhost:3030/space");
        	
        	// mainController.populateDatabase();
            
        	/* 
        	 * Method :
        	 * 
        	 * public double getQueryPrecision(int precision_level, String query, int idQuery, int similarity_type, 
			int indexation_type, int sparql_synonyme, int sparql_property){
        	 * 
        	 * similarity_type:
        	 * if coef de Dice set "1", if inner product set "0"
        	 *  
        	 * indexation_type:
        	 * inverse or direct? if inverse set "1", if direct set "0" 
        	 * 
        	 * sparql_synonyme:
        	 * populate with sparql synonyme or not? if yes set "1", if no set "0"
        	 * 
        	 * sparql_property:
        	 * populate with sparql property tokens or not? if yes set "1", if no set "0"
        	 * 
        	 */

        	double precision = mainController.getQueryPrecision(10,query1,1,0,1,1,0);
        	System.out.println("precision is: " + precision);
        	
        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}