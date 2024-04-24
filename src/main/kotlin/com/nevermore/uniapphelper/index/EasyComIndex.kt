package com.nevermore.uniapphelper.index

import com.intellij.lang.javascript.psi.JSImplicitElementProvider
import com.intellij.psi.stubs.StubIndexKey
import org.jetbrains.vuejs.index.VueIndexBase

/**
 * @author nevermore
 * @since
 */
class EasyComIndex : VueIndexBase<JSImplicitElementProvider>(EASY_COM_INDEX_KEY)

val EASY_COM_INDEX_KEY: StubIndexKey<String, JSImplicitElementProvider> = StubIndexKey.createIndexKey("easy.com.index")

