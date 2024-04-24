package com.nevermore.uniapphelper.psi

import com.intellij.lang.javascript.psi.JSImplicitElementProvider
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.StandardPatterns
import com.intellij.patterns.XmlPatterns.string
import com.intellij.patterns.XmlPatterns.xmlTag
import com.intellij.psi.*
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StubIndex
import com.intellij.psi.xml.XmlTag
import com.intellij.util.ProcessingContext
import com.intellij.util.Processor
import com.nevermore.uniapphelper.config.EasycomConfiguration
import com.nevermore.uniapphelper.config.EasycomConfigurations
import com.nevermore.uniapphelper.index.EASY_COM_INDEX_KEY
import org.jetbrains.vuejs.lang.html.VueLanguage

/**
 * @author nevermore
 * @since
 */
class EasyComReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        val easycomConfigurations = EasycomConfiguration.configurations

        registrar.registerReferenceProvider(
            StandardPatterns.or(*easycomConfigurations.map(::toPattern).toTypedArray()),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement, context: ProcessingContext
                ): Array<PsiReference> {
                    val value = element.asXmlTagToGetTagName()
                    return arrayOf(EasyComReference(element, TextRange(1, value.length + 1), easycomConfigurations))
                }

            })
    }

    private fun toPattern(it: EasycomConfiguration) =
        xmlTag().withLanguage(VueLanguage.INSTANCE).withName(string().matches(it.vueTagPatternString))
}

class EasyComReference(
    element: PsiElement,
    textRange: TextRange,
    private val easycomConfigurations: EasycomConfigurations
) :
    PsiReferenceBase<PsiElement>(element, textRange) {

    private val tagName: String = element.asXmlTagToGetTagName()
    private val psiFile by lazy { element.containingFile }

    override fun resolve(): PsiElement? {
        val isEasycomElement = easycomConfigurations
            .filter { psiFile.virtualFile.path.startsWith(it.pagesJsonFile.parent.path) }
            .any { easycomConfiguration -> easycomConfiguration.isEasycomTag(element.asXmlTagToGetTagName()) }

        val result = mutableListOf<ResolveResult>()
        if (isEasycomElement) {
            StubIndex.getInstance().processElements(
                EASY_COM_INDEX_KEY,
                tagName,
                element.project,
                GlobalSearchScope.everythingScope(element.project),
                JSImplicitElementProvider::class.java,
                Processor { provider: JSImplicitElementProvider ->
                    result.add(PsiElementResolveResult(provider))
                    return@Processor true
                })
        }
        return result.firstOrNull()?.element
    }
}

private fun PsiElement.asXmlTagToGetTagName() = (this as XmlTag).name
