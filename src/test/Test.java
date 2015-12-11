/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import controller.ProcessController;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import controller.HTMLController;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import utils.Utils;

/**
 *
 * @author yuanbo
 */
public class Test {
    
        public static void main(String[] args) {
            /*
            String currentPath = System.getProperty("user.dir");
            String stop_list_path = currentPath+ "/dist/stopliste.txt";
            System.out.println(stop_list_path);
            ArrayList<String> a = new ArrayList<String>();
            ProcessController pc = new ProcessController();
            a = pc.ProcessStoplist(stop_list_path);
                   
            String s = "haéé9çutilisÈes";
            System.out.println(LangUtils.formatString(s));
             */
            
            String currentPath = System.getProperty("user.dir");
            
            /*
            for( int i = 1 ; i<= 138; i++)
            {
                processController.processFile(currentPath + "/dist/CORPUS/D" +i + ".html", i);
                //System.out.println(currentPath + "/dist/CORPUS/D" +i + ".html");
            }
            
           
            File input = new File(currentPath + "/dist/CORPUS/D" +1 + ".html");
            try {
                Document doc = Jsoup.parse(input, "UTF-8","");
                System.out.println(doc.select("td"));
            } catch (IOException ex) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }
                    */
            
            String a = "title" ;
            HTMLController c = new HTMLController();
            int t = c.getDegreeImportance(a);
            System.out.println(t);
        }
    
}