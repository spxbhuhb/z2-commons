package hu.simplexion.z2.commons.protobuf

fun ULong.bool() = (this != 0UL)

fun ULong.int64(): Long = this.toLong()

fun ULong.int32(): Int = this.toInt()

fun ULong.sint32(): Int = (this shr 1).toInt() xor -(this and 1UL).toInt()

fun ULong.sint64(): Long = (this shr 1).toLong() xor -(this and 1UL).toLong()

