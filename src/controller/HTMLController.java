package controller;

import java.util.HashMap;

public class HTMLController {

    private HashMap<String,Integer> HTMLElementList ;
	
	public HTMLController(){
            HTMLElementList = new HashMap<String,Integer>();
            HTMLElementList.put("title", 100);
            HTMLElementList.put("meta-keyword", 90);
            HTMLElementList.put("meta-description", 90);
            HTMLElementList.put("meta-classification", 90);
            HTMLElementList.put("h1", 80);
            HTMLElementList.put("h2", 60);
            HTMLElementList.put("h3", 40);
            HTMLElementList.put("em", 30);
            HTMLElementList.put("b", 30);
            HTMLElementList.put("li", 30);
            HTMLElementList.put("p", 20);
	}

	public int getDegreeImportance(String element) {
            return this.getHTMLElementList().get(element);
	}

    public HashMap<String, Integer> getHTMLElementList() {
        return HTMLElementList;
    }

}