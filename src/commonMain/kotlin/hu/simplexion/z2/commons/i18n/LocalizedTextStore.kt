package hu.simplexion.z2.commons.i18n

import hu.simplexion.z2.commons.util.PublicApi
import hu.simplexion.z2.commons.util.UUID
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@PublicApi
open class LocalizedTextStore(
    @PublicApi
    val uuid : UUID<LocalizedTextStore>
) {
    val map: MutableMap<String, LocalizedText> = mutableMapOf()

    class LocalizedTextDelegate : ReadOnlyProperty<LocalizedTextStore, LocalizedText> {
        override fun getValue(thisRef: LocalizedTextStore, property: KProperty<*>): LocalizedText {
            return thisRef.map[property.name] !!
        }
    }

    operator fun String.provideDelegate(thisRef: LocalizedTextStore, prop: KProperty<*>): ReadOnlyProperty<LocalizedTextStore, LocalizedText> {
        thisRef.map[prop.name] = BasicLocalizedText(this)
        return LocalizedTextDelegate()
    }
}