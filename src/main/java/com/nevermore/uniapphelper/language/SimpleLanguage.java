package com.nevermore.uniapphelper.language;

import com.intellij.lang.Language;

/**
 * @author nevermore
 * @since
 */
public class SimpleLanguage extends Language {

    public static final SimpleLanguage INSTANCE = new SimpleLanguage();

    protected SimpleLanguage() {
        super("Simple");
    }
}
