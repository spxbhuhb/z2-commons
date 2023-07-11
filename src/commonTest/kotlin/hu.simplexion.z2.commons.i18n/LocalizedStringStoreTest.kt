package hu.simplexion.z2.commons.i18n

import hu.simplexion.z2.commons.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class  LocalizedStringStoreTest {

    @Test
    fun test() {
        val store = object : LocalizedStringStore(UUID()) {
            val a by "A"
            val b by "B"
        }
        assertEquals("A", store.a.toString())
        assertEquals("B", store.b.toString())
    }
}