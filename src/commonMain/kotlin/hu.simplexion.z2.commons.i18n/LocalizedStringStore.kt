package hu.simplexion.z2.commons.i18n

import hu.simplexion.z2.commons.util.UUID
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class LocalizedStringStore(
    val uuid : UUID<LocalizedStringStore>
) {
    val map: MutableMap<String, String> = mutableMapOf()

    class StringsDelegate : ReadOnlyProperty<LocalizedStringStore, String> {
        override fun getValue(thisRef: LocalizedStringStore, property: KProperty<*>): String {
            return thisRef.map[property.name] !!
        }
    }

    operator fun String.provideDelegate(thisRef: LocalizedStringStore, prop: KProperty<*>): ReadOnlyProperty<LocalizedStringStore, String> {
        thisRef.map[prop.name] = this
        return StringsDelegate()
    }

}