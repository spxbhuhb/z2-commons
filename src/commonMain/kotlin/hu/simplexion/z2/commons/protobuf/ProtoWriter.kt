package hu.simplexion.z2.commons.protobuf

import kotlin.experimental.or

@Suppress("SpellCheckingInspection")
class ProtoWriter{

    companion object {
        const val continuation = 0x80.toByte()
        const val valueMask = 0x7fUL
    }

    private var cptr = 0
    val buffer = ByteArray(100)

    private fun put(byte: Byte) {
        buffer[cptr++] = byte
    }

    val size
        get() = cptr

    fun bool(fieldNumber: Int, value : Boolean) {
        tag(fieldNumber, VARINT)
        if (value) varint(1UL) else varint(0UL)
    }

    fun int32(fieldNumber: Int, value : Int) {
        tag(fieldNumber, VARINT)
        varint(value.toULong())
    }

    fun sint32(fieldNumber: Int, value : Int) {
        tag(fieldNumber, VARINT)
        varint(((value shl 1) xor (value shr 31)).toULong())
    }

    fun uint32(fieldNumber: Int, value : UInt) {
        tag(fieldNumber, VARINT)
        varint(value.toULong())
    }

    fun fixed32(fieldNumber: Int, value: UInt) {
        tag(fieldNumber, I32)
        i32(value.toULong())
    }

    fun sfixed32(fieldNumber: Int, value: Int) {
        tag(fieldNumber, I32)
        i32(value.toULong())
    }

    fun int64(fieldNumber: Int, value : Long) {
        tag(fieldNumber, VARINT)
        varint(value.toULong())
    }

    fun sint64(fieldNumber: Int, value : Long) {
        tag(fieldNumber, VARINT)
        varint(((value shl 1) xor (value shr 63)).toULong())
    }

    fun uint64(fieldNumber: Int, value : ULong) {
        tag(fieldNumber, VARINT)
        varint(value)
    }

    fun fixed64(fieldNumber: Int, value: ULong) {
        tag(fieldNumber, I64)
        i64(value)
    }

    fun sfixed64(fieldNumber: Int, value: Long) {
        tag(fieldNumber, I64)
        i64(value.toULong())
    }

    fun float(fieldNumber: Int, value: Float) {
        tag(fieldNumber, I32)
        i32(value.toRawBits().toULong())
    }

    fun double(fieldNumber: Int, value : Double) {
        tag(fieldNumber, I64)
        i64(value.toRawBits().toULong())
    }

    fun string(fieldNumber : Int, value : String) {
        tag(fieldNumber, LEN)
        val bytes = value.encodeToByteArray()
        bytes.copyInto(buffer, cptr)
        cptr += bytes.size
    }

    fun bytes(fieldNumber : Int, value : ByteArray) {
        tag(fieldNumber, LEN)
        value.copyInto(buffer, cptr)
        cptr += value.size
    }

    private fun tag(fieldNumber: Int, type : Int) {
        val tag = (fieldNumber.toULong() shl 3) or type.toULong()
        varint(tag)
    }

    private fun i32(value : ULong): ULong {
        var remaining = value
        for (i in 0 until 4) {
            put((remaining and 0xffUL).toByte())
            remaining = remaining shr 8
        }
        return value
    }

    private fun i64(value : ULong): ULong {
        var remaining = value
        for (i in 0 until 8) {
            put((remaining and 0xffUL).toByte())
            remaining = remaining shr 8
        }
        return value
    }

    private fun varint(value : ULong) {
        var next = value and valueMask
        var remaining = value shr 7

        while (remaining != 0UL) {
            put(continuation or next.toByte())
            next = remaining and valueMask
            remaining = remaining shr 7
        }

        put(next.toByte())
    }


}