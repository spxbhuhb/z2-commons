package hu.simplexion.z2.commons.protobuf

/**
 * A very simple protobuf reader that decodes the protobuf wire
 * format into a list of protobuf records.
 *
 * At this level it is not possible to destructure the message
 * more than one level as the reader has no information about
 * the internal structure of LEN fields.
 */
class ProtoReader(
    val buffer: ByteArray,
    offset: Int = 0,
    val length: Int = buffer.size
) {

    companion object {
        const val continuation = 0x80UL
        const val valueMask = 0x7fUL
    }

    private var cptr = offset

    private fun get(): ULong {
        check(cptr < length)
        return buffer[cptr ++].toULong() and 0xffUL
    }

    fun records(): List<ProtoRecord> {
        val records = mutableListOf<ProtoRecord>()

        while (cptr < length) {
            val sptr = cptr
            val tag = varint()
            val fieldNumber = (tag shr 3).toLong()
            val type = (tag and 7UL).toInt()

            records += when (type) {
                VARINT -> VarintProtoRecord(fieldNumber, varint())
                I64 -> I64ProtoRecord(fieldNumber, i64())
                I32 -> I32ProtoRecord(fieldNumber, i32())
                LEN -> {
                    val length = varint().toInt()
                    LenProtoRecord(fieldNumber, cptr, length)
                }

                else -> throw IllegalArgumentException("unknown type $type at $sptr")
            }
        }

        return records
    }

    private fun i32(): UInt {
        var value = 0UL
        for (i in 0 until 4) {
            value = value or (get() shl (i * 8))
        }
        return value.toUInt()
    }

    private fun i64(): ULong {
        var value = 0UL
        for (i in 0 until 8) {
            value = value or (get() shl (i * 8))
        }
        return value
    }

    private fun varint(): ULong {
        var next = get()
        var shift = 0
        var value = 0UL

        while ((next and continuation) != 0UL) {
            value = value or ((next and valueMask) shl shift)
            next = get()
            shift += 7
        }

        return value or ((next and valueMask) shl shift)
    }

}