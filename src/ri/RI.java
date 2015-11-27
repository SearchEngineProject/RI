/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ri;

import org.jsoup.*;
import java.io.IOException;
import java.io.File;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.text.Normalizer;
import java.util.ArrayList;
//import org.w3c.dom.*;
/**
 *
 * @author yanchao
 */
public class RI {

    
    private static String stop_list_path;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here org.jsoup.jsoup.connect("http://example.com/").get();
        try{
            //org.jsoup.nodes.Document doc = Jsoup.connect("http://www.google.com").get();
            //String title = doc.title();
            //System.out.print(title);
            String currentPath = System.getProperty("user.dir");
            stop_list_path = currentPath+ "/dist/stopliste.txt";
            processFile(currentPath + "/dist/CORPUS/D1.html");
        }catch(Exception e)
        {
            
        }
        
    }
    
    public static void processFile(String path)
    {
        try{
            File input = new File(path);
            //System.out.print(path);
            
            
            Document doc = Jsoup.parse(input, "UTF-8","");
            
            //title
            ProcessText(doc.title(), 1, "title");
            
            //keyword meta in head
            Elements keyword_meta = doc.select("meta[name=\"keywords\"]");
            for (Element e : keyword_meta){
                System.out.print(e.attr("content"));
            }
        }catch(Exception e)
        {
            System.out.print("IO Error!");
        }
    }
    
    public static void ProcessText(String rawText,  int docID, String balise){
        try{
            String s = Normalizer.normalize(rawText, Normalizer.Form.NFD);
            s = s.replaceAll("[^a-zA-Z0-9 ]", "");
            String[] word_list = s.split(" ");
            //TODO: add every word into db with (WORD, docID, Balise)
            //add2DB(word, docid, balise);
        }catch(Exception e)
        {
            
        }
            

    }
    
}
