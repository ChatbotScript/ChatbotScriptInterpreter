/*   This file is part of the ChatbotScript Interpreter
*
* 	 This work is licensed under the Creative Commons Attribution-NonCommercial
*    4.0 International License. 
*    
*    To view a copy of this license, visit 
*    http://creativecommons.org/licenses/by-nc/4.0/.
*/

package net.richardsprojects.nlptest;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.text.DefaultStyledDocument.ElementSpec;

import net.richardsprojects.nlptest.core.ChatbotMode;
import net.richardsprojects.nlptest.core.Patterns;
import net.richardsprojects.nlptest.core.Preprocessor;
import net.richardsprojects.nlptest.core.objects.ProcessedSentence;
import net.richardsprojects.nlptest.modules.ClausIEModule;
import net.richardsprojects.nlptest.modules.GreetingsModule;
import net.richardsprojects.nlptest.modules.Module;
import net.richardsprojects.nlptest.modules.QuestionModule;
import net.richardsprojects.nlptest.scripting.Chatbot;
import net.richardsprojects.nlptest.triples.CBSObject;
import net.richardsprojects.nlptest.triples.Category;
import net.richardsprojects.nlptest.triples.Relationship;
import net.richardsprojects.nlptest.triples.Triple;

import de.mpii.clausie.ClausIE;
import de.mpii.clausie.Clause;
import de.mpii.clausie.Proposition;

public class Main {
	
	public static String CHATBOT_NAME = "chatbot";
	
	public static ArrayList<Category> categories = new ArrayList<Category>();	
	public static ArrayList<Triple> triples = new ArrayList<Triple>();
	
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
	                	Patterns patterns = new Patterns(CHATBOT_NAME + File.separator + "responses" + File.separator + responseFile);
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
        
        // load categories
		try {
			File categoriesFile = new File(CHATBOT_NAME + File.separator + "categories.txt");
			BufferedReader br = new BufferedReader(new FileReader(categoriesFile));  
			String line = null;
			
			boolean readingSubCategories = false;
			ArrayList<String> currentSubcategories = new ArrayList<String>();
			String categoryName = "";
			
			while ((line = br.readLine()) != null)  
			{
				if(!readingSubCategories) {
					if(line.contains("  - ")) {
						String subcategory = line.replace("  - ", "");
						currentSubcategories.add(subcategory);
					} else {
						categoryName = line;
						readingSubCategories = true;
					}
				} else {
					if(!line.contains("  - ")) {
						Category category = new Category(categoryName, currentSubcategories);
						Main.categories.add(category);
						currentSubcategories.clear();
						categoryName = line;
						readingSubCategories = true;
					}
					if(line.contains("  - ")) {
						String subcategory = line.replace("  - ", "");
						currentSubcategories.add(subcategory);
					}
				}
			}
			
			// handle end of file
			if(categoryName != "" && currentSubcategories.size() > 0) {
				Category category = new Category(categoryName, currentSubcategories);
				Main.categories.add(category);
				currentSubcategories.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		System.out.println("Loaded " + Main.categories.size() + " categories...");
        
        // Load triples
		try {
			// loop through files in knowledge folder
			File dir = new File(CHATBOT_NAME + File.separator + "knowledge");
			File[] directoryListing = dir.listFiles();
			if (directoryListing != null) {
				for (File child : directoryListing) {
					
					// load triples from this file
					int lineNumber = 1;
					BufferedReader br = new BufferedReader(new FileReader(child));  
					String line = null;
					
					while ((line = br.readLine()) != null)  
					{
						if(!(line.equals("") || line.equals(" ") || line.startsWith("#"))) {
							String error = "There is an invalid triple on line " + lineNumber + " in " + child.getName();
							if(line.split(":").length == 3) {
								String[] elements = line.split(":");
								
								CBSObject object = null;
								Relationship relationship = null;
								Category category = null;
								
								// determine CBSObject
								if(elements[0].contains("(") && elements[0].contains(")")) {
									// has a plural
									String plural;
									String regular;
									
									int openingParentheses = elements[0].indexOf("(");
									regular = elements[0].substring(0, openingParentheses);
									
									String pluralStr = elements[0].replaceFirst(regular, "");
									pluralStr = pluralStr.replace("(", "");
									pluralStr = pluralStr.replace(")", "");
									plural = pluralStr;
									
									object = new CBSObject(regular, plural);
								} else {
									object = new CBSObject(elements[0], elements[0]);
								}
								
								// TODO: Add other relationship types in the future
								// determine relation
								if(elements[1].equalsIgnoreCase("isA")) {
									relationship = Relationship.IS;
								}
								
								// determine category
								for(Category c : categories) {
									if(elements[2].equalsIgnoreCase(c.getName())) {
										category = c;
									}
								}
								
								if(object != null && relationship != null && category != null) {
									Triple triple = new Triple(object, relationship, category);
									triples.add(triple);
								} else {
									System.out.println(error);
								}
								
							} else {
								System.out.println(error);
							}
								
							lineNumber++;
						}
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
        
		System.out.println("Loaded " + Main.triples.size() + " triplets...");
    }
}
