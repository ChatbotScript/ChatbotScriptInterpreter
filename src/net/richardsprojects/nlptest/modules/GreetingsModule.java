/*   This file is part of the ChatbotScript Interpreter
*
* 	 This work is licensed under the Creative Commons Attribution-NonCommercial
*    4.0 International License. 
*    
*    To view a copy of this license, visit 
*    http://creativecommons.org/licenses/by-nc/4.0/.
*/

package net.richardsprojects.nlptest.modules;

import java.io.File;
import java.io.IOException;

import net.richardsprojects.nlptest.Main;
import net.richardsprojects.nlptest.core.Patterns;

public class GreetingsModule extends Module {

	@Override
	public String getResult(String input) {
		Patterns greetings = new Patterns(Main.CHATBOT_NAME + File.separator + "patterns" + File.separator + "greetings.cbs");
		
		try {
			greetings.load();
			String response = greetings.getResult(input);
			if(response != null) {
				return response;
			} else {
				return null;
			}			
		} catch (IOException e) {
			System.out.println("Could not find greetings file");
			return null;
		}		
	}

}
