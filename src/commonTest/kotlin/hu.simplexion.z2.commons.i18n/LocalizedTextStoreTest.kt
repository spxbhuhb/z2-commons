package hu.simplexion.z2.commons.i18n

import hu.simplexion.z2.commons.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class  LocalizedTextStoreTest {

    @Test
    fun test() {
        val store = object : LocalizedTextStore(UUID()) {
            val a by "A"
            val b by "B"
        }
        assertEquals("A", store.a.toString())
        assertEquals("B", store.b.toString())
    }
}