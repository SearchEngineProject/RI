package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.Utils;

public class ProcessController {
    
    public void processFile(String path, int docId)
    {
        try{
            File input = new File(path);
            Document doc = Jsoup.parse(input, "UTF-8","");

            //process title
            ProcessText(doc.title(), docId, "title");
            
            //process meta keyword
            Elements keyword_meta = doc.select("meta[name=\"keywords\"]");
            for (Element e : keyword_meta){
                ProcessText(e.attr("Content"), docId, "meta-keyword");
            }
            
            //process meta-description
            Elements meta_desc = doc.select("meta[name=\"description\"]");
            for (Element e : meta_desc){
                
                ProcessText(e.attr("Content"), docId, "meta-description");
            }
            //process meta-classification
            Elements meta_class = doc.select("meta[name=\"classification\"]");
            for (Element e : meta_class){
                ProcessText(e.attr("Content"), docId, "meta-classification");
            }
            
            //process h1
            Elements h1_elements = doc.select("h1");
            for (Element e : h1_elements){
                //System.out.println(e + e.text());
                ProcessText(e.text(), docId, "h1");
            }
            
            //process h2
            Elements h2_elements = doc.select("h2");
            for (Element e : h2_elements){
                //System.out.println(e + e.text());
                ProcessText(e.text(), docId, "h2");
            }
            
            //process h3
            Elements h3_elements = doc.select("h3");
            for (Element e : h3_elements){
                //System.out.println(e + e.text());
                //System.exit(0);
                ProcessText(e.text(), docId, "h3");
            }
                        
            //process em
            Elements em_elements = doc.select("em");
            for (Element e : em_elements){
                //ystem.out.println(e + e.text());
                //System.exit(0);
                ProcessText(e.text(), docId, "em");
            }
            
            
            //process li
            Elements li_elements = doc.select("li");
            for (Element e : li_elements){
                //System.out.println(e + e.text());
                //System.exit(0);
                ProcessText(e.text(), docId, "li");
            }            
            
            //process b
            Elements b_elements = doc.select("b");
            System.out.println("Beginning B");
            for (Element e : b_elements){
                //System.out.println(e + e.text());
                //System.exit(0);
                ProcessText(e.text(), docId, "b");
            }  
            
            //process p
            Elements p_elements = doc.select("p");
            System.out.println("Beginning P");
            for (Element e : p_elements){
                //ProcessText(e.text(), docId, "p");
            }              
        }catch(Exception e)
        {
            System.out.print(e);
            System.out.print("IO Error!");
        }
    }
    
    public void ProcessText(String rawText, int docID, String balise){
        try{
            String s = Normalizer.normalize(rawText, Normalizer.Form.NFD);
            s = s.replaceAll("[^a-zA-Z0-9 ]", "");
            String[] word_list = s.split(" ");
            DBController dbo = new controller.DBController();
            dbo.connect();
            for (String word : word_list)
            {
                // if the word is not empty and not appear in stoplist
                if(word.length() != 0 && !Utils.isInStoplist(word)){
                    dbo.insert_term(word, docID, balise);
                    //System.out.println(word);
                }
            }
            dbo.disconnect();
        }catch(Exception e)
        {
            
        }
    }
    
    // return a list of all stoplist words
    public ArrayList<String> ProcessStoplist(String stoplistFile){
        ArrayList<String> stoplist = new ArrayList<String>();
        try {
            File f = new File(stoplistFile);
            Scanner s = new Scanner(new FileInputStream(f),"ISO-8859-1");
            while(s.hasNextLine()){
                stoplist.add(s.nextLine());
            }
            s.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ProcessController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stoplist ;
    }
    
    // TODO: Complete this
    public ArrayList<String> ProcessRequest(String request){
        
        return null ;
    }
    
    public void ChercherDocs(String query)
    {
        String[] keyword_list = query.split(" ");
        HashMap<Integer, Integer> result = new HashMap<Integer, Integer> ();
    }
    
    public HashMap ProcessQuery(String query)
    {
        DBController dbo = new DBController();
        dbo.connect();
        
        //term should be seperated by empty space
        String[] words = query.split(" ");
        HashMap<Integer, Double> results = new HashMap<Integer, Double> ();
        for(String word : words)
        {
        HashMap<Integer, Double> single_results = dbo.single_term_query(word);

        for(int doc_id : single_results.keySet())
        {

            if(results.containsKey(doc_id))
            {
                double o_value =  results.get(doc_id);
                double n_value = o_value+ single_results.get(doc_id);
                results.put(doc_id, n_value);
            }
            else
            {
                results.put(doc_id, single_results.get(doc_id));
           }
        }
        dbo.disconnect();
    }
    return results;
}
            
    public void ProcessEvaluation(){
        
        
    }
    
    public void ImportQrel(String path){
        
        ArrayList<String> qrel_ref = new ArrayList<String>();
        try {
            File f = new File(path);
            Scanner s = new Scanner(new FileInputStream(f),"ISO-8859-1");
            while(s.hasNextLine()){
                String[] ref = s.nextLine().split(" ");
                
                
                qrel_ref.add(s.nextLine());
            }
            s.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ProcessController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}