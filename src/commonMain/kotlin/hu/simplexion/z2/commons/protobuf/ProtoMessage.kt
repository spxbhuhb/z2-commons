package hu.simplexion.z2.commons.protobuf

import hu.simplexion.z2.commons.util.UUID

/**
 * Parse Protocol Buffer messages.
 *
 * @param  wireFormat  The wire format message to parse. This buffer backs the parser, it should
 *                     not change until the message is in use.
 */
class ProtoMessage(
    wireFormat : ByteArray,
    offset : Int = 0,
    length : Int = wireFormat.size
) {

    val records: List<ProtoRecord> = ProtoBufferReader(wireFormat, offset, length).records()

    operator fun get(fieldNumber: Int): ProtoRecord? = records.lastOrNull { it.fieldNumber == fieldNumber }

    // -----------------------------------------------------------------------------------------
    // Boolean
    // -----------------------------------------------------------------------------------------

    fun boolean(fieldNumber: Int): Boolean = get(fieldNumber)?.let { it.value == 1UL } ?: false

    fun booleanList(fieldNumber: Int) = scalarList(fieldNumber, { value.bool() }, { varint().bool() })

    // -----------------------------------------------------------------------------------------
    // Int
    // -----------------------------------------------------------------------------------------

    fun int(fieldNumber: Int): Int = get(fieldNumber)?.value?.sint32() ?: 0

    fun intList(fieldNumber: Int) = scalarList(fieldNumber, { value.sint32() }, { varint().sint32() })

    // -----------------------------------------------------------------------------------------
    // Long
    // -----------------------------------------------------------------------------------------

    fun long(fieldNumber: Int): Long = get(fieldNumber)?.value?.sint64() ?: 0L

    fun longList(fieldNumber: Int) = scalarList(fieldNumber, { value.sint64() }, { varint().sint64() })

    // -----------------------------------------------------------------------------------------
    // String
    // -----------------------------------------------------------------------------------------

    fun string(fieldNumber: Int): String = get(fieldNumber)?.string() ?: ""

    fun stringList(fieldNumber: Int) = scalarList(fieldNumber, { string() }, { string() })

    // -----------------------------------------------------------------------------------------
    // ByteArray
    // -----------------------------------------------------------------------------------------

    fun byteArray(fieldNumber: Int): ByteArray = get(fieldNumber)?.bytes() ?: ByteArray(0)

    fun byteArrayList(fieldNumber: Int) = scalarList(fieldNumber, { bytes() }, { bytes() })

    // -----------------------------------------------------------------------------------------
    // UUID
    // -----------------------------------------------------------------------------------------

    fun <T> uuid(fieldNumber: Int): UUID<T> = get(fieldNumber)?.uuid() ?: UUID.nil()

    fun <T> uuidList(fieldNumber: Int) : List<UUID<T>> = scalarList(fieldNumber, { uuid() }, { uuid() })

    // -----------------------------------------------------------------------------------------
    // Non-Primitive
    // -----------------------------------------------------------------------------------------

    fun <T> instance(fieldNumber: Int, decoder: ProtoDecoder<T>) : T {
        val record = get(fieldNumber) ?: return decoder.decodeProto(null)
        check(record is LenProtoRecord)
        return decoder.decodeProto(record.message())
    }

    // -----------------------------------------------------------------------------------------
    // Non-Scalar List
    // -----------------------------------------------------------------------------------------

    fun <T> list(fieldNumber: Int, decoder: ProtoDecoder<T>): MutableList<T> {
        val list = mutableListOf<T>()
        for (record in records) {
            if (record.fieldNumber != fieldNumber) continue
            check(record is LenProtoRecord)
            list += decoder.decodeProto(record.message())
        }
        return list
    }

    // --------------------------------------------------------------------------------------
    // Helpers
    // --------------------------------------------------------------------------------------

    fun <T> scalarList(
        fieldNumber: Int,
        single: ProtoRecord.() -> T,
        item: ProtoBufferReader.() -> T
    ): MutableList<T> {
        val list = mutableListOf<T>()

        for (record in records) {
            if (record.fieldNumber != fieldNumber) continue

            if (record !is LenProtoRecord) {
                list += record.single()
            } else {
                list += ProtoBufferReader(record).packed(item)
            }
        }
        return list
    }

}