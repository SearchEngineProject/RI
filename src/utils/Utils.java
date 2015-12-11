
package utils;

import constant.Constant;
import controller.ProcessController;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;

public class Utils {
    
    // remove accent and uncapitalize all letters
    public static String formatString(String input){
        String output = StringUtils.stripAccents(input).toLowerCase();
        return output ;
    }
    
    
    // check if a term is in stoplist
    public static boolean isInStoplist(String term){
        boolean result = false ;
        ProcessController pc = new ProcessController();
        ArrayList<String> stoplist = pc.ProcessStoplist(Constant.getStoplistPath());
        for(String s : stoplist){
            if(Utils.formatString(term).equals(Utils.formatString(s)))
                result = true;
        }
        return result ;
    }
}