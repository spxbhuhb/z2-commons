package hu.simplexion.z2.commons.protobuf

/**
 * A low level protobuf reader that decodes the protobuf wire
 * format into a list of protobuf records.
 *
 * At this level it is not possible to destructure the message
 * more than one level as the reader has no information about
 * the internal structure of LEN fields.
 *
 * @property   buffer  Data to read.
 * @property   offset  Offset of the first byte in [buffer] this reader should process.
 * @property   length  Number of bytes available for this reader. There may be more bytes
 *                     in the buffer, but the reader does not read over the length.
 */
class ProtoBufferReader(
    val buffer: ByteArray, val offset: Int = 0, val length: Int = buffer.size
) {

    constructor(record: LenProtoRecord) : this(record.byteArray, record.offset, record.length)

    /**
     * Convert the byte array into a list of [ProtoRecord]. The records, [LenProtoRecord]s
     * in particular, are backed by the buffer, you should not modify the buffer while
     * they are used.
     */
    fun records(): List<ProtoRecord> {
        val records = mutableListOf<ProtoRecord>()

        val readEnd = offset + length

        while (readOffset < readEnd) {
            val startOffset = readOffset
            val tag = varint()
            val fieldNumber = (tag shr 3).toLong()
            val type = (tag and 7UL).toInt()

            records += when (type) {
                VARINT -> VarintProtoRecord(fieldNumber, varint())
                I64 -> I64ProtoRecord(fieldNumber, i64())
                I32 -> I32ProtoRecord(fieldNumber, i32())
                LEN -> LenProtoRecord(fieldNumber, buffer, readOffset, varint().toInt())
                else -> throw IllegalArgumentException("unknown type $type at $startOffset")
            }
        }

        // the reader should read all data, not less, not more
        check(readOffset - offset == length) { "read length mismatch, structural problem in the message or software bug" }

        readOffset = offset // this makes `records` idempotent
        return records
    }

    private var readOffset = offset

    private fun get(): ULong {
        check(readOffset < length)
        return buffer[readOffset++].toULong() and 0xffUL
    }

    private fun i32(): ULong {
        var value = 0UL
        for (i in 0 until 4) {
            value = value or (get() shl (i * 8))
        }
        return value
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