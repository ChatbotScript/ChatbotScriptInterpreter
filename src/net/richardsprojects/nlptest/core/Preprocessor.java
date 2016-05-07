/*   This file is part of the ChatbotScript Interpreter
*
* 	 This work is licensed under the Creative Commons Attribution-NonCommercial
*    4.0 International License. 
*    
*    To view a copy of this license, visit 
*    http://creativecommons.org/licenses/by-nc/4.0/.
*/

package net.richardsprojects.nlptest.core;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import net.richardsprojects.nlptest.core.objects.ProcessedSentence;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.process.DocumentPreprocessor;

public class Preprocessor {
	
	public static ArrayList<ProcessedSentence> run(String input) {
		// split into sentences		
		ArrayList<ProcessedSentence> sentences = new ArrayList<ProcessedSentence>();
		
		Reader reader = new StringReader(input);
		DocumentPreprocessor dp = new DocumentPreprocessor(reader);
		
		for(List<HasWord> sentence : dp) {
			String sentenceString = Sentence.listToString(sentence);
			
			String raw = sentenceString.toString();
			String processed = processSentence(raw);
			
			sentences.add(new ProcessedSentence(raw, processed));
		}
		
		return sentences;
	}
	
	public static String processSentence(String input) {
		// remove punctuation
		input = input.replace("'", " ");
		input = input.replace(" ,", "");
		input = input.replace(" ?", "");
		input = input.replace(" .", "");
		input = input.replace(" ,", "");
		input = input.replace(" !", "");
		input = input.replace(" '", "  ");
		input = input.replace(",", "");
		input = input.replace("?", "");
		input = input.replace(".", "");
		input = input.replace(",", "");
		input = input.replace("!", "");
		input = input.replace("{", "");
		input = input.replace("}", "");
		
		// TODO: Replace select words with brackets for their type
		
		// TODO: Add support for recognizing dates using the Natty library
		// http://natty.joestelmach.com/
		
		return input;
	}

}
