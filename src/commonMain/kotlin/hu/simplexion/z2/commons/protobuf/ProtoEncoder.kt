package hu.simplexion.z2.commons.protobuf

interface ProtoEncoder<T> {

    fun encode(value: T) : ByteArray

}