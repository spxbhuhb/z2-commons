package hu.simplexion.z2.commons.i18n

class BasicLocalizedText(
    val value : String
) : LocalizedText {
    override fun toString(): String = value
}