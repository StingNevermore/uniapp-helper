package com.nevermore.uniapphelper.utils

import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement

/**
 * @author nevermore
 * @since
 */
class EasycomDocumentationUtils {
    companion object {
        fun findDocumentationComment(element: PsiElement): String {
            var documentationElement: PsiElement = element
            while (documentationElement !is PsiComment) {
                documentationElement = documentationElement.prevSibling
            }
            return documentationElement.text
        }
    }
}
