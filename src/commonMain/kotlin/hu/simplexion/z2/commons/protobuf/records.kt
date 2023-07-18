package hu.simplexion.z2.commons.protobuf

interface ProtoRecord {
    val fieldNumber: Long
    val type : Int
}

class VarintProtoRecord(
    override val fieldNumber: Long,
    val value : ULong
) : ProtoRecord {

    override val type: Int
        get() = VARINT

    fun int64(): Long {
        return value.toLong()
    }

    fun sint64(): Long {
        return (value shr 1).toLong() xor - (value and 1UL).toLong()
    }
}

class I64ProtoRecord(
    override val fieldNumber: Long,
    val value : ULong
) : ProtoRecord {
    override val type: Int
        get() = I64
}

class LenProtoRecord(
    override val fieldNumber: Long,
    val length : Int,
    val offset : Int
) : ProtoRecord {
    override val type: Int
        get() = LEN
}

class I32ProtoRecord(
    override val fieldNumber: Long,
    val value : UInt
) : ProtoRecord {
    override val type: Int
        get() = I32
}