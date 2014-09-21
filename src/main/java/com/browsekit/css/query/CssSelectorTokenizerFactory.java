package com.browsekit.css.query;

public class CssSelectorTokenizerFactory {
	private CssSelectorTokenizerFactory(){}
	private static CssSelectorTokenizer tokenizer;

	public static synchronized CssSelectorTokenizer tokenizer() {
		if (tokenizer == null) {
			tokenizer = CssSelectorTokenizer.instance();
		}
		return tokenizer;
	}
}
