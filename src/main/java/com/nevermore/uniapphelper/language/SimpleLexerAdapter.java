package com.nevermore.uniapphelper.language;

import com.intellij.lexer.FlexAdapter;

/**
 * @author nevermore
 * @since
 */
public class SimpleLexerAdapter extends FlexAdapter {
    public SimpleLexerAdapter() {
        super(new SimpleLexer(null));

    }
}
