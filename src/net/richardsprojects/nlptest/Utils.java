/*   This file is part of the ChatbotScript Interpreter
*
* 	 This work is licensed under the Creative Commons Attribution-NonCommercial
*    4.0 International License. 
*    
*    To view a copy of this license, visit 
*    http://creativecommons.org/licenses/by-nc/4.0/.
*/

package net.richardsprojects.nlptest;

public class Utils {

    public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)"+regex+"(?!.*?"+regex+")", replacement);
    }
	
}
