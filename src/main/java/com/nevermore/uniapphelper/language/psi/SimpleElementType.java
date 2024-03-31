package com.nevermore.uniapphelper.language.psi;

import com.intellij.psi.tree.IElementType;
import com.nevermore.uniapphelper.language.SimpleLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * @author nevermore
 * @since
 */
public class SimpleElementType extends IElementType {

    public SimpleElementType(@NotNull @NonNls String debugName) {
        super(debugName, SimpleLanguage.INSTANCE);
    }

}
