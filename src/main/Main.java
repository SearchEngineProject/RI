package main;

import controller.DBController;
import controller.ProcessController;
import java.util.HashMap;
import java.util.Iterator;

public class Main {
    
    public static void main(String[] args) {
        // TODO code application logic here org.jsoup.jsoup.connect("http://example.com/").get();
        try{
            
            ProcessController processController = new ProcessController();
            String currentPath = System.getProperty("user.dir");

            for( int i = 1 ; i<= 138; i++)
            {
                processController.processFile(currentPath + "/dist/CORPUS/D" +i + ".html", i);
                System.out.println(currentPath + "/dist/CORPUS/D" +i + ".html");
            }
            
            for(int i = 1 ; i<9 ; i ++){
                //processController.ProcessEvaluation(currentPath + "/dist/qrels/qrelQ" +i + ".txt", i);
            }
            
            HashMap<Integer,Double> map = new HashMap<Integer,Double>();
            map = processController.ProcessQuery("Nicolas");

            Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                HashMap.Entry pair = (HashMap.Entry)it.next();
                System.out.println(pair.getKey() + " = " + pair.getValue());
                it.remove(); // avoids a ConcurrentModificationException
            }
    
            // processController.processFile(currentPath + "/dist/CORPUS/D" +1 + ".html", 1);  
        }catch(Exception e)
        {
            
        }
    }
}
