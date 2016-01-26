package ri.utils;

import ri.controller.ProcessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringUtils;

public class Utils {
    
    // remove accent and uncapitalize all letters
    public static String formatString(String input){
        String output = StringUtils.stripAccents(input).toLowerCase();
        return output ;
    }
    
    public static boolean isDocPertinent(int idDoc, int idQuery, ProcessController pc){
            boolean retour = false;
            String currentPath = System.getProperty("user.dir");
            HashMap<Integer,Integer> map = pc.ProcessImportQrel(currentPath + "/dist/qrels/qrelQ" +idQuery + ".txt");
            try{
            if(map.get(idDoc) == 1){
                retour = true ;
                //System.out.println(" #docid: "+idDoc);
            }
            }
            catch(Exception NullPointerException){
            	//System.out.println("idDoc "+idDoc+" does not exist in qrel"+idQuery);
            }
            return retour ;
    }
    
    public static double getPrecision(HashMap<Integer,Double> sortedMap, int level, int idQuery, ProcessController pc){  
        Iterator<Entry<Integer, Double>> it = sortedMap.entrySet().iterator();
        int nbDocPertinent = 0 ;
        int i = 0 ;
        double precision = 0 ;
        while (it.hasNext() && i < level) {
            HashMap.Entry<Integer, Double> pair = (HashMap.Entry<Integer, Double>)it.next();
            if(isDocPertinent((int)pair.getKey(), idQuery, pc)){
                nbDocPertinent++;
            }
            i++;
        }
        //System.out.println("level="+level);
        //System.out.println("nbDocPertinent="+nbDocPertinent);
        return (double)nbDocPertinent/level;
    }
    
	public static boolean wordExistInList(String word, ArrayList<String> list){
		boolean result = false ;
		for(String s : list){
			if(word.equals(s)){
				result = true ;
			}
		}
		return result ;
	}
    
}