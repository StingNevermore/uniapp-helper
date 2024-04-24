package com.nevermore.uniapphelper.index

import com.intellij.lang.javascript.index.FrameworkIndexingHandler
import com.intellij.lang.javascript.psi.stubs.JSImplicitElementStructure
import com.intellij.psi.stubs.IndexSink

/**
 * @author nevermore
 * @since
 */
class EasyComIndexingHandler : FrameworkIndexingHandler() {
    override fun indexImplicitElement(element: JSImplicitElementStructure, sink: IndexSink?): Boolean {
        element.name.takeIf { it.startsWith("uni") }
            ?.let { sink?.occurrence(EASY_COM_INDEX_KEY, it) }
        return super.indexImplicitElement(element, sink)
    }
}
