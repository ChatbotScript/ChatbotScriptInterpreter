/*   This file is part of the ChatbotScript Interpreter
*
* 	 This work is licensed under the Creative Commons Attribution-NonCommercial
*    4.0 International License. 
*    
*    To view a copy of this license, visit 
*    http://creativecommons.org/licenses/by-nc/4.0/.
*/

package net.richardsprojects.nlptest.modules;

import java.util.ArrayList;

import net.richardsprojects.nlptest.Main;
import de.mpii.clausie.Proposition;

public class ClausIEModule extends Module {

	@Override
	public String getResult(String input) {
		String response = "";
        Main.clausIE.parse(input);
        Main.clausIE.detectClauses();
        Main.clausIE.generatePropositions();

        // generate propositions
        response = response + "Input: ";
        String sep = "";
        ArrayList<Proposition> inputProps = new ArrayList<Proposition>();
        ArrayList<Proposition> patternProps = new ArrayList<Proposition>();
        for (Proposition prop : Main.clausIE.getPropositions()) {
        	inputProps.add(prop);
            response = response + sep + prop.toString();
            for(int i = 0; i > prop.noArguments(); i++) {
            	String text = prop.argument(i);
            	
            	/*Parser parser = new Parser();
            	List groups = parser.parse("the day before next thursday");
            	for(DateGroup group:groups) {
            	  List dates = group.getDates();
            	  int line = group.getLine();
            	  int column = group.getPosition();
            	  String matchingValue = group.getText();
            	  String syntaxTree = group.getSyntaxTree().toStringTree();
            	  Map> parseMap = group.getParseLocations();
            	  boolean isRecurreing = group.isRecurring();
            	  Date recursUntil = group.getRecursUntil();
            	}*/
            }
        }
        response = response + "\n";
        //
        return response;
	}

}
