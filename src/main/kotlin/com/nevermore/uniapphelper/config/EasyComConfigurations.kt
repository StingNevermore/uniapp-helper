package com.nevermore.uniapphelper.config

import com.google.gson.Gson
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.FilenameIndex.getVirtualFilesByName
import com.intellij.psi.search.GlobalSearchScope.projectScope
import com.intellij.util.asSafely
import java.io.InputStreamReader
import java.util.regex.Pattern


/**
 * @author nevermore
 * @since
 */
typealias EasycomConfigurations = List<EasycomConfiguration>

private const val EASYCOM_NODE = "easycom"
private const val CUSTOM_NODE = "custom"
private const val PAGES_JSON_FILE_NAME = "pages.json"

data class EasycomConfiguration(
    val vueTagPatternString: String,
    val vueFilePatternString: String,
    val pagesJsonFile: VirtualFile
) {
    private val pattern: Pattern = Pattern.compile(vueTagPatternString)

    companion object {
        val configurations: EasycomConfigurations by lazy { initializeEasycomConfigurations() }
    }

    fun isEasycomTag(tagName: String) = pattern.matcher(tagName).matches()
}

private fun initializeEasycomConfigurations() =
    ProjectManager.getInstance().openProjects
        .flatMap { thisProject -> getVirtualFilesByName(PAGES_JSON_FILE_NAME, projectScope(thisProject)) }
        .mapNotNull(::toConfiguration)
        .flatten()

private fun toConfiguration(jsonFile: VirtualFile) =
    Gson().fromJson<Map<String, Any>>(InputStreamReader(jsonFile.inputStream), Map::class.java)[EASYCOM_NODE]
        ?.asSafely<Map<String, Any>>()
        ?.get(CUSTOM_NODE)
        ?.asSafely<Map<String, String>>()
        ?.map { EasycomConfiguration(it.key, it.value, jsonFile) }
