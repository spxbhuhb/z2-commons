package hu.simplexion.z2.commons.protobuf

internal object ProtoBoolean : ProtoDecoder<Boolean>,ProtoEncoder<Boolean> {

    override fun decodeFrom(record: ProtoRecord): Boolean =
        record.bool()

    override fun encodeInto(writer: ProtoBufferWriter, data: Boolean) {
        writer.bool(data)
    }

}

object ProtoInt : ProtoDecoder<Int>,ProtoEncoder<Int> {

    override fun decodeFrom(record: ProtoRecord): Int =
        record.int32()

    override fun encodeInto(writer: ProtoBufferWriter, data: Int) {
        writer.int32(data)
    }

}

object ProtoString : ProtoDecoder<String>,ProtoEncoder<String> {

    override fun decodeFrom(record: ProtoRecord): String =
        record.string()

    override fun encodeInto(writer: ProtoBufferWriter, data: String) {
        writer.string(data)
    }

}

class ProtoListEncoder<T>(
    val itemEncoder : ProtoEncoder<T>
) : ProtoEncoder<List<T>> {

    override fun encodeInto(writer: ProtoBufferWriter, data: List<T>) {
        val subWriter = ProtoBufferWriter()
        for (item in data) {
            itemEncoder.encodeInto(subWriter, item)
        }
        writer.bytes(subWriter.data)
        // TODO performance enhancement of proto writer
        // this moves data around way too much, the buffer list used by the writers
        // and the sub-writers should be shared, something to do later
    }

}


class ProtoListDecoder<T>(
    val itemDecoder : ProtoDecoder<T>
) : ProtoDecoder<List<T>> {

    override fun decodeFrom(record: ProtoRecord): List<T> {
        check(record is LenProtoRecord)
        val list = mutableListOf<T>()
        for (itemRecord in ProtoBufferReader(record).records()) {
            list += itemDecoder.decodeFrom(itemRecord)
        }
        return list
    }

}