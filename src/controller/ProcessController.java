package controller;

import constant.Constant;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.Utils;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ProcessController {
    
    private DBController dbc ;
    
    public ProcessController(DBController dbc){
        this.dbc = dbc ;
    }
    
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
            
            /*
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
            for (Element e : p_elements){
                //ProcessText(e.text(), docId, "p");
            }              */

        }catch(Exception e)
        {
            System.out.print(e);
            System.out.print(path+"IO Error!");
        }
    }
    
    public void ProcessText(String rawText, int docID, String balise){
        try{
            String s = Normalizer.normalize(rawText, Normalizer.Form.NFD);
            s = s.replaceAll("[^a-zA-Z0-9 ]", "");
            String[] word_list = s.split(" ");
            for (String word : word_list)
            {
                // if the word is not empty and not appear in stoplist
                if(word.length() != 0 && !isInStoplist(word)){
                    dbc.insert_term(word, docID, balise);
                }
            }
        }catch(Exception e)
        {
            
        }
    }
    
    // check if a term is in stoplist
    public boolean isInStoplist(String term){
        boolean result = false ;
        ArrayList<String> stoplist = ProcessStoplist(Constant.getStoplistPath());
        for(String s : stoplist){
            if(Utils.formatString(term).equals(Utils.formatString(s)))
                result = true;
        }
        return result ;
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
        //term should be seperated by empty space
        String[] words = query.split(" ");
        HashMap<Integer, Double> results = new HashMap<Integer, Double> ();
        
        
        
        for(String word : words)
        {
            HashMap<Integer, Double> single_results = dbc.single_term_query(word);
            
            
            
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
        }
        return this.sortByComparator(results, false);
    }
    
    public HashMap ProcessQueryDice(String query)
    {
        String[] words = query.split(" ");
        HashMap<Integer, Double> results = new HashMap<Integer, Double> ();
        
        int lenght_query = words.length;
        for (int doc_id = 1; doc_id< 138; doc_id++)
        {
            int doc_term_length = dbc.get_total_term(doc_id);
            if (doc_term_length !=0)
            {
                double doc_query_match = 0;
                for (String word : words)
                {
                    HashMap<Integer, Double> single_results = dbc.single_term_query(word);
                    if (single_results.containsKey(doc_id))
                    {
                        doc_query_match += single_results.get(doc_id);
                    }
                }
                if(doc_query_match != 0)
                {
                    results.put(doc_id, (double)Math.round(200*doc_query_match / (lenght_query + doc_term_length))/100 );
                }
                
            }
        }
        return this.sortByComparator(results, false);
        
        
    }
            
    public void ProcessEvaluation(){
        
        
    }
    
    public HashMap<Integer,Integer> ProcessImportQrel(String path){
        
        HashMap<Integer,Integer> qrel_ref = new HashMap<Integer,Integer>();
        int id = 0 ;
        int ponderation = 0 ;
        
        try {
            File f = new File(path);
            Scanner s = new Scanner(new FileInputStream(f),"ISO-8859-1");
            while(s.hasNextLine()){
                String[] ref = s.nextLine().split("\\s+");
                String[] result = ref[0].split("\\.");
                String[] result2 = result[0].split("D");
                id = Integer.parseInt(result2[1]);
                if(ref[1].equals("0")){
                    ponderation = 0 ;
                }
                else{
                    ponderation = 1 ;
                }
                qrel_ref.put(id, ponderation);
            }
            s.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ProcessController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return qrel_ref;
    }
    
    public HashMap<Integer, Double> sortByComparator(Map<Integer, Double> unsortMap, final boolean order)
   {

       List<Entry<Integer, Double>> list = new LinkedList<Entry<Integer, Double>>(unsortMap.entrySet());

       // Sorting the list based on values
       Collections.sort(list, new Comparator<Entry<Integer, Double>>()
       {
           public int compare(Entry<Integer, Double> o1,
                   Entry<Integer, Double> o2)
           {
               if (order)
               {
                   return o1.getValue().compareTo(o2.getValue());
               }
               else
               {
                   return o2.getValue().compareTo(o1.getValue());

               }
           }
       });

       // Maintaining insertion order with the help of LinkedList
       HashMap<Integer, Double> sortedMap = new LinkedHashMap<Integer, Double>();
       for (Entry<Integer, Double> entry : list)
       {
           sortedMap.put(entry.getKey(), entry.getValue());
       }

       return sortedMap;
   }

}