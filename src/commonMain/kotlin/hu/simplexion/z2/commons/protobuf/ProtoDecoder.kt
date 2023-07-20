package hu.simplexion.z2.commons.protobuf

interface ProtoDecoder<T> {

    fun decode(message : ProtoMessage?) : T

}