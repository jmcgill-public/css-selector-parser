Usage
=====
Java code:

	String selector = "body div#main";
	CssSelectorTokenizer tokenizer = CssSelectorTokenizerFactory.tokenizer();
	SelectorPhraseList selectorTokens = tokenizer.parse(selector);
