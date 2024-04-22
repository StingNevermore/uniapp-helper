package com.nevermore.uniapphelper.language.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.nevermore.uniapphelper.language.psi.SimpleNamedElement;
import org.jetbrains.annotations.NotNull;

/**
 * @author nevermore
 * @since
 */
public abstract class SimpleNamedElementImpl extends ASTWrapperPsiElement implements SimpleNamedElement {
    public SimpleNamedElementImpl(@NotNull ASTNode node) {
        super(node);
    }
}
