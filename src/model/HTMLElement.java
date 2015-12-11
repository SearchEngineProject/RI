package model;

public class HTMLElement {
	
	private int degreeImportance ;
	private String element ;
	
	public HTMLElement(int degreeImportance, String element){
		this.degreeImportance = degreeImportance ;
		this.element = element ;
	}

	public int getDegreeImportance() {
		return degreeImportance;
	}

	public void setDegreeImportance(int degreeImportance) {
		this.degreeImportance = degreeImportance;
	}

	public String getElement() {
		return element;
	}

	public void setElement(String element) {
		this.element = element;
	}
}