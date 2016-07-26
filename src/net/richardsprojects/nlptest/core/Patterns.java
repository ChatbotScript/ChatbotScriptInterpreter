/*   This file is part of the ChatbotScript Interpreter
*
* 	 This work is licensed under the Creative Commons Attribution-NonCommercial
*    4.0 International License. 
*    
*    To view a copy of this license, visit 
*    http://creativecommons.org/licenses/by-nc/4.0/.
*/

package net.richardsprojects.nlptest.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import net.richardsprojects.nlptest.Main;
import net.richardsprojects.nlptest.Utils;
import net.richardsprojects.nlptest.triples.Triple;

public class Patterns {

	String fileName;
	
	// TODO: Make a class that can store both a pattern and their code
	ArrayList<String> patterns = new ArrayList<String>();
	ArrayList<String> code = new ArrayList<String>();
	
	public Patterns(String fileName) {
		this.fileName = fileName;
	}
	
	public void load() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));  
		String line = null;  
		boolean readingPattern = false;
		StringBuilder currentCode = null;
		while ((line = br.readLine()) != null)  
		{
			// exit if there is a comment
			if(line.startsWith("\\#")) continue;
			
			if(readingPattern) {
				if(line.equals("END")) {
					readingPattern = false;
					code.add(currentCode.toString());
				} else {
					currentCode.append(System.getProperty("line.separator") + line);
				}
			}
			if(!readingPattern && line.contains(":")) {
				// load pattern
				String pattern = Utils.replaceLast(line, ":", "");
				patterns.add(pattern);
				readingPattern = true;
				currentCode = new StringBuilder();
			}
		}
	}
	
	public String getResult(String pattern) {		
		int i = 0;
		for(String p: patterns) {
			if(p.equalsIgnoreCase(pattern)) {
				String returnValue = "";
				String javascript = code.get(i);
				
				// exucute javascript
				ScriptEngineManager factory = new ScriptEngineManager();
				ScriptEngine engine = factory.getEngineByName("JavaScript");
				
				try {
					String output = "";
					engine.put("output", output);
					engine.put("chatbot", Main.chatbot);
					engine.eval(javascript);
					output = engine.get("output").toString();
					return output;
				} catch (ScriptException e) {
					return null;
				}
			}
			i++;
		}
		return null;
	}
	
}
