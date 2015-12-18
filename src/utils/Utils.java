
package utils;

import constant.Constant;
import controller.DBController;
import controller.ProcessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
            if(map.get(idDoc) == 1){
                retour = true ;
            }
            return retour ;
    }
}