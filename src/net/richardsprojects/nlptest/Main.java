/*   This file is part of the ChatbotScript Interpreter
*
* 	 This work is licensed under the Creative Commons Attribution-NonCommercial
*    4.0 International License. 
*    
*    To view a copy of this license, visit 
*    http://creativecommons.org/licenses/by-nc/4.0/.
*/

package net.richardsprojects.nlptest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import net.richardsprojects.nlptest.core.ChatbotMode;
import net.richardsprojects.nlptest.core.Patterns;
import net.richardsprojects.nlptest.core.Preprocessor;
import net.richardsprojects.nlptest.core.objects.ProcessedSentence;
import net.richardsprojects.nlptest.modules.ClausIEModule;
import net.richardsprojects.nlptest.modules.GreetingsModule;
import net.richardsprojects.nlptest.modules.Module;
import net.richardsprojects.nlptest.modules.QuestionModule;
import net.richardsprojects.nlptest.scripting.Chatbot;

import de.mpii.clausie.ClausIE;
import de.mpii.clausie.Clause;
import de.mpii.clausie.Proposition;

public class Main {
	
	public static boolean isRunning = true;
	public static ClausIE clausIE;
	
	public static ChatbotMode mode = ChatbotMode.DEFAULT;
	/* the file for patterns if it is looking for a specific response */
	public static String responseFile = "";
	
	public static String responseType = "";
	public static Chatbot chatbot = new Chatbot();
	
	private static Module[] modules = new Module[3];
	
    public static void main(String[] args) throws IOException {       
        setup();    	
    	
        while(isRunning) {
        	String sentence = "";
        	if(sentence.equals("")) {
        		System.out.print("User: ");
                Scanner sc = new Scanner(System.in);
                sentence = sc.nextLine();
                String completeResponse = null;
                
                long start = System.currentTimeMillis();
                
                // create the arraylist of inputs
                ArrayList<ProcessedSentence> inputs = Preprocessor.run(sentence);
                
                // calculate response to each sentence by going through modules
                for(ProcessedSentence ps : inputs) {
                	String result = null;
                	
	                if(mode == ChatbotMode.DEFAULT) {
		                
	                	// loop through modules
	                	for(int i = 0; i < modules.length; i++) {
	                		Module module = modules[i];
	                		result = module.getResult(ps.getProcessedSentence());
	                		if(result != null) {
	                			if(completeResponse == null) {
	                				completeResponse = result;
	                			} else {
	                				completeResponse = completeResponse + " " + result;
	                			}	                			
	                			break;
	                		}
	                	}
	                	
	                } else if(mode == ChatbotMode.WAITING_RESPONSE) {
	                	Patterns patterns = new Patterns("resources" + File.separator + "responses" + File.separator + responseFile);
	            		try {
	            			patterns.load();
	            			String response = patterns.getResult(ps.getProcessedSentence());
	            			if(response != null) {
	            				completeResponse = response;
	            			}	
	            		} catch (IOException e) {
	            			System.out.println("Could not find specified file");
	            		}
	                }
                }
                long end = System.currentTimeMillis();
                
                // Print response
                String time = (end - start) / 1000. + "s";
                System.out.println("Bot: " + completeResponse + " (" + time + ")");
        	}
        }
    }
    
    private static void setup() {
    	// initialize clausIE
    	clausIE = new ClausIE();
        clausIE.initParser();
        clausIE.getOptions();

        // add modules
        modules[0] = new GreetingsModule();
        modules[1] = new QuestionModule();
        modules[2] = new ClausIEModule();
        
        // TODO: Implement way of loading triplets at start of program
    }
}
