package hu.simplexion.z2.commons.protobuf

abstract class ProtoList<T>(
    val fieldNumber: Int,
    val packed: Boolean = false,
    val packedType: Int = 0
) : ProtoEncoder<List<T>>, ProtoDecoder<List<T>> {

    abstract fun encodeItemInto(writer: ProtoBufferWriter, value: T)

    override fun encodeInto(writer: ProtoBufferWriter, value: List<T>) {
        if (packed) {
            val subWriter = ProtoBufferWriter()
            for (item in value) {
                encodeItemInto(subWriter, item)
            }
            writer.bytes(fieldNumber, subWriter.pack())
        } else {
            for (item in value) {
                val subWriter = ProtoBufferWriter()
                encodeItemInto(subWriter, item)
                writer.bytes(fieldNumber, subWriter.pack())
            }
        }
    }

    abstract fun decodeItemFrom(record: ProtoRecord): T

    override fun decodeFrom(records: List<ProtoRecord>): List<T> {
        val list = mutableListOf<T>()

        for (record in records) {
            if (record.fieldNumber != fieldNumber) continue

            if (packed && record is LenProtoRecord) {
                for (value in ProtoBufferReader(record).packedRecords(fieldNumber, packedType)) {
                    list += decodeItemFrom(value)
                }
            } else {
                list += decodeItemFrom(record)
            }
        }

        return list
    }
}

class ProtoBooleanList(fieldNumber: Int) : ProtoList<Boolean>(fieldNumber, true) {

    override fun encodeItemInto(writer: ProtoBufferWriter, value: Boolean) {
        writer.bool(value)
    }

    override fun decodeItemFrom(record: ProtoRecord): Boolean =
        record.bool()

}

class ProtoIntList(fieldNumber: Int) : ProtoList<Int>(fieldNumber, true) {

    override fun encodeItemInto(writer: ProtoBufferWriter, value: Int) {
        writer.int32(value)
    }

    override fun decodeItemFrom(record: ProtoRecord): Int =
        record.int32()

}

class ProtoStringList(fieldNumber: Int) : ProtoList<String>(fieldNumber, true, LEN) {

    override fun encodeItemInto(writer: ProtoBufferWriter, value: String) {
        writer.string(value)
    }

    override fun decodeItemFrom(record: ProtoRecord): String =
        record.string()

}

class ProtoListGen<T>(
    fieldNumber: Int,
    val decoder: ProtoDecoder<T>,
    val encoder: ProtoEncoder<T>
) : ProtoList<T>(fieldNumber) {

    override fun encodeItemInto(writer: ProtoBufferWriter, value: T) {
        encoder.encodeInto(writer, value)
    }

    override fun decodeItemFrom(record: ProtoRecord): T =
        decoder.decodeFrom(record)

}
