package hu.simplexion.z2.commons.protobuf

interface ProtoDecoder<T> {

    fun decodeFrom(record : ProtoRecord) : T {
        check(record is LenProtoRecord)
        return decodeFrom(ProtoBufferReader(record).records())
    }

    fun decodeFrom(records : List<ProtoRecord>) : T
}