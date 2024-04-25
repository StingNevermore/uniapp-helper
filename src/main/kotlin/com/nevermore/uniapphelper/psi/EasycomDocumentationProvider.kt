package com.nevermore.uniapphelper.psi

import com.intellij.codeInsight.navigation.targetPresentation
import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.lang.documentation.DocumentationMarkup.*
import com.intellij.model.Pointer
import com.intellij.platform.backend.documentation.DocumentationResult
import com.intellij.platform.backend.documentation.DocumentationTarget
import com.intellij.platform.backend.documentation.PsiDocumentationTargetProvider
import com.intellij.platform.backend.presentation.TargetPresentation
import com.intellij.psi.PsiElement
import com.intellij.psi.presentation.java.SymbolPresentationUtil
import com.intellij.psi.xml.XmlToken
import com.intellij.refactoring.suggested.createSmartPointer
import com.nevermore.uniapphelper.config.isEasycomElement
import com.nevermore.uniapphelper.utils.EasycomDocumentationUtils
import org.jetbrains.vuejs.lang.html.VueLanguage
import java.util.regex.Pattern

/**
 * @author nevermore
 * @since
 */
class EasycomDocumentationProvider : AbstractDocumentationProvider() {
    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
        val filePathPresentation = SymbolPresentationUtil.getFilePathPresentation(element?.containingFile)
        return super.generateDoc(element, originalElement)
    }

//    override fun getCustomDocumentationElement(
//        editor: Editor,
//        file: PsiFile,
//        contextElement: PsiElement?,
//        targetOffset: Int
//    ): PsiElement? {
//        return contextElement?.takeIf { it.parent is HtmlTag }
//            ?: super.getCustomDocumentationElement(
//                editor,
//                file,
//                contextElement,
//                targetOffset
//            )
//    }
}

class EasycomDocumentationTargetProvider : PsiDocumentationTargetProvider {
    override fun documentationTarget(element: PsiElement, originalElement: PsiElement?): DocumentationTarget? {
        return if (isEasycomTag(originalElement)) EasycomDocumentationTarget(element, originalElement) else null
    }

    private fun isEasycomTag(originalElement: PsiElement?): Boolean {
        if (originalElement !is XmlToken?) return false
        if (originalElement?.context?.language?.`is`(VueLanguage.INSTANCE) == false) return false
        return originalElement?.parent?.isEasycomElement() ?: false
    }
}

class EasycomDocumentationTarget(private val element: PsiElement, private val originalElement: PsiElement?) :
    DocumentationTarget {

    private val definitionRegex = Pattern.compile("\\*\\s(.*)")
    private val descriptionRegex = Pattern.compile("\\*\\s(@description)*(.*)")

    override fun computePresentation(): TargetPresentation {
        return targetPresentation(element)
    }

    override fun createPointer(): Pointer<out DocumentationTarget> {
        val elementPtr = element.createSmartPointer()
        val originalElementPtr = originalElement?.createSmartPointer()
        return Pointer {
            val element = elementPtr.dereference() ?: return@Pointer null
            EasycomDocumentationTarget(element, originalElementPtr?.dereference())
        }
    }

    override fun computeDocumentation(): DocumentationResult {
        return DocumentationResult.documentation(computeLocalDocumentation())
    }

    private fun computeLocalDocumentation(): String {
        val documentationString = EasycomDocumentationUtils.findDocumentationComment(element).trim()
        val documentationStringArray = documentationString.split("\n").map { it.trim() }

        return buildString {
            append(DEFINITION_START)
            val definitionMatcher = definitionRegex.matcher(documentationStringArray[1])
            if (definitionMatcher.matches()) {
                append(definitionMatcher.group(1))
            }
            append(DEFINITION_END)
            val descriptionMatcher = descriptionRegex.matcher(documentationStringArray[2])
            if (descriptionMatcher.matches()) {
                append(descriptionMatcher.group(2))
            }
            append(CONTENT_START)

            append(CONTENT_END)

            append(SECTIONS_START)
            append("@Property {key} String")
            append(SECTIONS_END)


            append(GRAYED_START)
            append("test grayed")
            append(GRAYED_END)
        }
    }
}
