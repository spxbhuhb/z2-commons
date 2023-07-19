package hu.simplexion.z2.commons.protobuf

interface ProtoDecoder<T> {
    fun decodeFrom(record : ProtoRecord) : T
}