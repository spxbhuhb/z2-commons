package hu.simplexion.z2.commons.i18n

class BasicLocalizedText(
    private val value : String
) : LocalizedText {
    override fun toString(): String = value
}