/*   This file is part of the ChatbotScript Interpreter
*
* 	 This work is licensed under the Creative Commons Attribution-NonCommercial
*    4.0 International License. 
*    
*    To view a copy of this license, visit 
*    http://creativecommons.org/licenses/by-nc/4.0/.
*/

package net.richardsprojects.nlptest.core.objects;

public class ProcessedSentence {

	private String original = "";
	private String processed = "";
	
	public ProcessedSentence(String o, String p) {
		this.original = o;
		this.processed = p;
	}
	
	public String getOriginalSentece() {
		return this.original;
	}
	
	public String getProcessedSentence() {
		return this.processed;
	}
}
