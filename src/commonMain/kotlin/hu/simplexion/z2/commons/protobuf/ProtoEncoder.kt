package hu.simplexion.z2.commons.protobuf

interface ProtoEncoder<T> {
    fun encodeInto(writer : ProtoBufferWriter, data : T)
}