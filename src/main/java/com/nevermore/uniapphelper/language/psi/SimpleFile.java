package com.nevermore.uniapphelper.language.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.nevermore.uniapphelper.language.SimpleFileType;
import com.nevermore.uniapphelper.language.SimpleLanguage;
import org.jetbrains.annotations.NotNull;

/**
 * @author nevermore
 * @since
 */
public class SimpleFile extends PsiFileBase {

    public SimpleFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, SimpleLanguage.INSTANCE);
    }

    @Override
    public @NotNull FileType getFileType() {
        return SimpleFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Simple File";
    }
}
