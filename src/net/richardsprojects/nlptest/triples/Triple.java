/*   This file is part of the ChatbotScript Interpreter
*
* 	 This work is licensed under the Creative Commons Attribution-NonCommercial
*    4.0 International License. 
*    
*    To view a copy of this license, visit 
*    http://creativecommons.org/licenses/by-nc/4.0/.
*/

package net.richardsprojects.nlptest.triples;

public class Triple {
	
	private CBSObject object;
	private Category category;
	private Relationship relationship;
	
	public Triple(CBSObject noun, Relationship relation, Category category) {
		object = noun;
		relationship = relation;
		this.category = category; 
	}
	
	public CBSObject getObject() {
		return object;
	}

	public Relationship getRelationship() {
		return relationship;
	}

	public Category getCategory() {
		return category;
	}
}
