package net.richardsprojects.nlptest.triples;

import java.util.ArrayList;

public class Category {

	private String name;
	private ArrayList<String> subcategories;
	
	public Category(String name, ArrayList<String> subcategories) {
		this.name = name;
		this.subcategories = subcategories;
	}
	
	public ArrayList<String> getSubcategories() {
		return this.subcategories;
	}
	
	public String getName() {
		return this.name;
	}
}
