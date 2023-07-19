package hu.simplexion.z2.commons.protobuf

interface ProtoEncoder<T> {

    fun encodeInto(writer : ProtoBufferWriter, fieldNumber : Int, value : T) {
        val subWriter = ProtoBufferWriter()
        encodeInto(subWriter, value)
        writer.bytes(fieldNumber, subWriter.pack())
    }

    fun encodeInto(writer : ProtoBufferWriter, value : T)

}