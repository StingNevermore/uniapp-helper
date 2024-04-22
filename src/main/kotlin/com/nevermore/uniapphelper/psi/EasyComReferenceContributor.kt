package com.nevermore.uniapphelper.psi

import com.intellij.lang.javascript.psi.JSImplicitElementProvider
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.StandardPatterns.string
import com.intellij.patterns.XmlPatterns
import com.intellij.psi.*
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StubIndex
import com.intellij.psi.xml.XmlTag
import com.intellij.util.ProcessingContext
import com.intellij.util.Processor
import org.jetbrains.vuejs.index.VUE_COMPONENTS_INDEX_KEY

/**
 * @author nevermore
 * @since
 */
class EasyComReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            XmlPatterns.xmlTag().withName(string().startsWith("uni").andOr(string().startsWith("Uni"))),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement, context: ProcessingContext
                ): Array<PsiReference> {
                    val value = element.asXmlTagToGetTagName()
                    return arrayOf(EasyComReference(element, TextRange(0, value.length)))
                }

            })
    }
}

class EasyComReference(element: PsiElement, textRange: TextRange) : PsiReferenceBase<PsiElement>(element, textRange) {

    private val tagName: String = element.asXmlTagToGetTagName()

    override fun resolve(): PsiElement? {
        val result = mutableListOf<ResolveResult>()
        StubIndex.getInstance().processElements(VUE_COMPONENTS_INDEX_KEY,
            tagName,
            element.project,
            GlobalSearchScope.everythingScope(element.project),
            JSImplicitElementProvider::class.java,
            Processor { provider: JSImplicitElementProvider ->
                result.add(PsiElementResolveResult(provider))
                return@Processor true
            })
        return result.firstOrNull()?.element
    }
}

private fun PsiElement.asXmlTagToGetTagName() = (this as XmlTag).name