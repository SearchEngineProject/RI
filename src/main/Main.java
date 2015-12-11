package main;

import controller.ProcessController;

public class Main {
    
    public static void main(String[] args) {
        // TODO code application logic here org.jsoup.jsoup.connect("http://example.com/").get();
        try{
            
            ProcessController processController = new ProcessController();
            String currentPath = System.getProperty("user.dir");

            for( int i = 1 ; i<= 138; i++)
            {
                processController.processFile(currentPath + "/dist/CORPUS/D" +i + ".html", i);
                //System.out.println(currentPath + "/dist/CORPUS/D" +i + ".html");
            }
            
            // processController.processFile(currentPath + "/dist/CORPUS/D" +1 + ".html", 1);  
        }catch(Exception e)
        {
            
        }
    }
}
