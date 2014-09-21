package com.browsekit.css.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.browsekit.css.query.grammar.*;
import com.browsekit.css.query.tokens.SelectorPhrase;
import com.browsekit.css.query.tokens.SelectorPhraseList;

public class CssSelectorTokenizer {
	private static CssSelectorTokenizer instance;
	private List<Part> parts;

	private CssSelectorTokenizer(){
		init();
	}
	
	private void init() {
		parts = 
				Collections.synchronizedList(
						new ArrayList<Part>(){
							private static final long serialVersionUID = 1349724310024159236L;

			{
				// these parts should be in the order they are desired to be found in
				// (simple while loop used to discover which applies, so order is important)
				// thus if two patterns can match the same text, the one more specific should be first

				// phrase or element separation
				add(new PhraseDeliminator());
				//TODO add(new DirectDescendant()); // /^\s*>\s*/
				add(new Descendant());

				// specificity 0,1,0,0
				add(new Id());

				// specificity 0,0,1,0
				add(new ClassName());
				add(new AttributeEquals());
				add(new AttributeBeginsWith());
				add(new AttributeEndsWith());
				add(new AttributeContains());
				add(new AttributeHas());

				// specificity 0,0,0,1
				add(new Element());
				add(new PseudoParamCss());
				//TODO add(new PseudoParam()); // /^\:([a-z0-9\-]+)\((.+?)\)/i
				//TODO add(new Pseudo()); // /^\:([a-z0-9\-]+)/i

			}
		});
	}
	
	/**
	 * Use the default Parts
	 * @param selector
	 * @return
	 * @throws CssQueryUnknownGrammarException
	 */
	public SelectorPhraseList parse(String selector) throws CssQueryUnknownGrammarException {
		return parse(this.parts, selector);
	}
	
	/**
	 * Use custom Parts
	 * @param parts
	 * @param selector
	 * @return
	 * @throws CssQueryUnknownGrammarException
	 */
	public SelectorPhraseList parse(List<Part> parts, String selector) throws CssQueryUnknownGrammarException {
		// setup with initial token
		SelectorPhraseList tokens = new SelectorPhraseList();
		SelectorPhrase currentToken = new SelectorPhrase();
		String selectorRemaining;

		tokens.add(currentToken);

		// parse
		int lastLength = selector.length();
		int currentLength;
		do{
			// loop through known phrases to parse the string and add tokens
			for(Part part : parts){
				selectorRemaining = part.match(selector, tokens);
				if(selectorRemaining != null){
					selector = selectorRemaining;
					break;
				}
			}
			currentLength = selector.length();
			if(lastLength == currentLength){
				// no known phrases were able to match the next section, unknown grammar
				throw new CssQueryUnknownGrammarException(selector);
			}
			lastLength = currentLength;
		} while(currentLength > 0);

		// remove any erroneous blank tokens (deep)
		tokens.clean();
		return tokens;
	}

	public static CssSelectorTokenizer instance() {
		if (instance == null) {
			instance = new CssSelectorTokenizer();
		}
		return instance;
	}

}
