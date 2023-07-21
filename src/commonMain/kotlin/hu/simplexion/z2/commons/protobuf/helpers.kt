package hu.simplexion.z2.commons.protobuf

import hu.simplexion.z2.commons.util.UUID

fun ULong.bool() = (this != 0UL)

fun ULong.int64(): Long = this.toLong()

fun ULong.int32(): Int = this.toInt()

fun ULong.sint32(): Int = (this shr 1).toInt() xor -(this and 1UL).toInt()

fun ULong.sint64(): Long = (this shr 1).toLong() xor -(this and 1UL).toLong()

object ProtoOneBoolean : ProtoDecoder<Boolean> {
    override fun decodeProto(message: ProtoMessage?): Boolean =
        message?.boolean(1) ?: false
}

object ProtoOneInt : ProtoDecoder<Int> {
    override fun decodeProto(message: ProtoMessage?): Int =
        message?.int(1) ?: 0
}

object ProtoOneLong : ProtoDecoder<Long> {
    override fun decodeProto(message: ProtoMessage?): Long =
        message?.long(1) ?: 0L
}

object ProtoOneString : ProtoDecoder<String> {
    override fun decodeProto(message: ProtoMessage?): String =
        message?.string(1) ?: ""
}

object ProtoOneByteArray : ProtoDecoder<ByteArray> {
    override fun decodeProto(message: ProtoMessage?): ByteArray =
        message?.byteArray(1) ?: ByteArray(0)
}

object ProtoOneUuid : ProtoDecoder<UUID<Any>> {
    override fun decodeProto(message: ProtoMessage?): UUID<Any> =
        message?.uuid(1) ?: UUID()
}
