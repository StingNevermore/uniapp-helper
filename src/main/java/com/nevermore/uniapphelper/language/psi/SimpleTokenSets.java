package com.nevermore.uniapphelper.language.psi;

import com.intellij.psi.tree.TokenSet;

/**
 * @author nevermore
 * @since
 */
public interface SimpleTokenSets {
    TokenSet IDENTIFIERS = TokenSet.create(SimpleTypes.KEY);

    TokenSet COMMENTS = TokenSet.create(SimpleTypes.COMMENT);
}
