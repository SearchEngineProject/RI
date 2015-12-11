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
                ProcessText(e.attr("Content"), docId, "keywords");
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
}