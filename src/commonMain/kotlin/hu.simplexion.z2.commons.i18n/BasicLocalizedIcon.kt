package hu.simplexion.z2.commons.i18n

class BasicLocalizedIcon(
    private val value : String
) : LocalizedIcon {
    override fun toString(): String = value
}