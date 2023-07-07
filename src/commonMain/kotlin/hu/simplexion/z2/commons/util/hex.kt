package hu.simplexion.z2.commons.util

@PublicApi
fun ByteArray.toHexString(): String {
    val hex = CharArray(2 * this.size)

    this.forEachIndexed { i, byte ->
        val unsigned = 0xff and byte.toInt()
        hex[2 * i] = hexChars[unsigned / 16]
        hex[2 * i + 1] = hexChars[unsigned % 16]
    }

    return hex.joinToString("")
}