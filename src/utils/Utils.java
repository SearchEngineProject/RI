
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

}