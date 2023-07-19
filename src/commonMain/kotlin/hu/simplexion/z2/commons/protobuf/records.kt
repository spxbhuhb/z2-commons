package hu.simplexion.z2.commons.protobuf

interface ProtoRecord {

    val fieldNumber: Long
    val type : Int
    val value : ULong

    fun bool() = (value == 0UL)

    fun int64(): Long = value.toLong()

    fun int32(): Int {
        return (value shr 1).toInt() xor - (value and 1UL).toInt()
    }

    fun sint64(): Long {
        return (value shr 1).toLong() xor - (value and 1UL).toLong()
    }

    fun string() : String = throw IllegalStateException("string is not available for numeric records")
}

class VarintProtoRecord(
    override val fieldNumber: Long,
    override val value : ULong
) : ProtoRecord {

    override val type: Int
        get() = VARINT

}

class I64ProtoRecord(
    override val fieldNumber: Long,
    override val value : ULong
) : ProtoRecord {
    override val type: Int
        get() = I64
}

class LenProtoRecord(
    override val fieldNumber: Long,
    val byteArray : ByteArray,
    val offset : Int,
    val length : Int
) : ProtoRecord {
    override val type: Int
        get() = LEN

    override val value: ULong
        get() = throw IllegalStateException("long value is not available for LEN record")

    override fun string(): String =
        byteArray.decodeToString(offset, offset + length)
}

class I32ProtoRecord(
    override val fieldNumber: Long,
    override val value : ULong
) : ProtoRecord {
    override val type: Int
        get() = I32
}