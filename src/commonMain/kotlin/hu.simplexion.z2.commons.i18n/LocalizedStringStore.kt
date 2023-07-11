package hu.simplexion.z2.commons.i18n

import hu.simplexion.z2.commons.util.PublicApi
import hu.simplexion.z2.commons.util.UUID
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@PublicApi
open class LocalizedStringStore(
    @PublicApi
    val uuid : UUID<LocalizedStringStore>
) {
    val map: MutableMap<String, LocalizedText> = mutableMapOf()

    class LocalizedTextDelegate : ReadOnlyProperty<LocalizedStringStore, LocalizedText> {
        override fun getValue(thisRef: LocalizedStringStore, property: KProperty<*>): LocalizedText {
            return thisRef.map[property.name] !!
        }
    }

    operator fun String.provideDelegate(thisRef: LocalizedStringStore, prop: KProperty<*>): ReadOnlyProperty<LocalizedStringStore, LocalizedText> {
        thisRef.map[prop.name] = BasicLocalizedText(this)
        return LocalizedTextDelegate()
    }
}