/*   This file is part of the ChatbotScript Interpreter
*
* 	 This work is licensed under the Creative Commons Attribution-NonCommercial
*    4.0 International License. 
*    
*    To view a copy of this license, visit 
*    http://creativecommons.org/licenses/by-nc/4.0/.
*/

package net.richardsprojects.nlptest.scripting;

import net.richardsprojects.nlptest.Main;
import net.richardsprojects.nlptest.core.ChatbotMode;

public class Chatbot {

	public void setMode(String mode) {
		if(mode.equalsIgnoreCase("Waiting_Response")) {
			Main.mode = ChatbotMode.WAITING_RESPONSE;
		} else if(mode.equalsIgnoreCase("Default")) {
			Main.mode = ChatbotMode.DEFAULT;
			Main.responseFile = "";
		}
	}
	
	public void setResponseFile(String patterns) {
		Main.responseFile = patterns;
	}
	
}
