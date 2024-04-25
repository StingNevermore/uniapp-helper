package com.nevermore.uniapphelper.psi

import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.codeInsight.daemon.impl.HighlightInfoFilter
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlToken
import com.intellij.util.asSafely
import com.intellij.xml.analysis.XmlAnalysisBundle
import com.nevermore.uniapphelper.config.EasycomConfiguration
import java.util.regex.Pattern

/**
 * @author nevermore
 * @since
 */
class EasycomHighlightInfoFilter : HighlightInfoFilter {
    private val unknownHtmlTagMessagePattern =
        Pattern.compile(XmlAnalysisBundle.message("xml.inspections.unknown.html.tag", ".*"))

    override fun accept(highlightInfo: HighlightInfo, file: PsiFile?): Boolean {
        if (file == null) return true
        val tagName = file.findElementAt(highlightInfo.startOffset)
            ?.asSafely<XmlToken>()
            ?.text
            ?: return true

        val filteredHighlightInfo = highlightInfo
            .takeIf { it.severity == HighlightSeverity.WARNING }
            .takeIf { thisInfo ->
                thisInfo?.description?.let { unknownHtmlTagMessagePattern.matcher(it).matches() } == true
            }

        if (filteredHighlightInfo == null) return true

        val checkConfigurationResult = EasycomConfiguration.configurations
            .filter { it.containsFileOf(file) }
            .any { it.isEasycomTag(tagName) }

        return !checkConfigurationResult
    }
}
