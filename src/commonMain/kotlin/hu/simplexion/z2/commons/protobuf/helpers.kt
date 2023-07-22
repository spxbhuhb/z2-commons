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

object ProtoOneBooleanList : ProtoDecoder<List<Boolean>> {
    override fun decodeProto(message: ProtoMessage?): List<Boolean> =
        message?.booleanList(1) ?: emptyList()
}

object ProtoOneIntList : ProtoDecoder<List<Int>> {
    override fun decodeProto(message: ProtoMessage?): List<Int> =
        message?.intList(1) ?: emptyList()
}

object ProtoOneLongList : ProtoDecoder<List<Long>> {
    override fun decodeProto(message: ProtoMessage?): List<Long> =
        message?.longList(1) ?: emptyList()
}

object ProtoOneStringList : ProtoDecoder<List<String>> {
    override fun decodeProto(message: ProtoMessage?): List<String> =
        message?.stringList(1) ?: emptyList()
}

object ProtoOneByteArrayList : ProtoDecoder<List<ByteArray>> {
    override fun decodeProto(message: ProtoMessage?): List<ByteArray> =
        message?.byteArrayList(1) ?: emptyList()
}

object ProtoOneUuidList : ProtoDecoder<List<UUID<Any>>> {
    override fun decodeProto(message: ProtoMessage?): List<UUID<Any>> =
        message?.uuidList(1) ?: emptyList()
}

class ProtoOneInstanceList<T>(val decoder : ProtoDecoder<T>) : ProtoDecoder<List<T>> {
    override fun decodeProto(message: ProtoMessage?): List<T> =
        message?.instanceList(1, decoder) ?: emptyList()
}
