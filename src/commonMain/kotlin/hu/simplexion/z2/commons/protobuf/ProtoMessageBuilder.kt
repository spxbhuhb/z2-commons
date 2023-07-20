package hu.simplexion.z2.commons.protobuf

class ProtoMessageBuilder {

    val writer = ProtoBufferWriter()

    fun pack() = writer.pack()

    // ----------------------------------------------------------------------------
    // Boolean
    // ----------------------------------------------------------------------------

    fun boolean(fieldNumber: Int, value: Boolean): ProtoMessageBuilder {
        writer.bool(fieldNumber, value)
        return this
    }

    fun booleanList(fieldNumber: Int, values: List<Boolean>): ProtoMessageBuilder {
        sub(fieldNumber) {
            for (value in values) {
                it.bool(value)
            }
        }
        return this
    }

    // ----------------------------------------------------------------------------
    // Int
    // ----------------------------------------------------------------------------

    fun int(fieldNumber: Int, value: Int): ProtoMessageBuilder {
        writer.sint32(fieldNumber, value)
        return this
    }

    fun intList(fieldNumber: Int, values: List<Int>): ProtoMessageBuilder {
        sub(fieldNumber) {
            for (value in values) {
                it.sint32(value)
            }
        }
        return this
    }

    // ----------------------------------------------------------------------------
    // Long
    // ----------------------------------------------------------------------------

    fun long(fieldNumber: Int, value: Long): ProtoMessageBuilder {
        writer.sint64(fieldNumber, value)
        return this
    }

    fun longList(fieldNumber: Int, values: List<Long>): ProtoMessageBuilder {
        sub(fieldNumber) {
            for (value in values) {
                it.sint64(value)
            }
        }
        return this
    }

    // ----------------------------------------------------------------------------
    // String
    // ----------------------------------------------------------------------------

    fun string(fieldNumber: Int, value: String): ProtoMessageBuilder {
        writer.string(fieldNumber, value)
        return this
    }

    fun stringList(fieldNumber: Int, values: List<String>): ProtoMessageBuilder {
        sub(fieldNumber) {
            for (value in values) {
                it.string(value)
            }
        }
        return this
    }

    // ----------------------------------------------------------------------------
    // ByteArray
    // ----------------------------------------------------------------------------

    fun byteArray(fieldNumber: Int, value: ByteArray): ProtoMessageBuilder {
        writer.bytes(fieldNumber, value)
        return this
    }

    fun byteArrayList(fieldNumber: Int, values: List<ByteArray>): ProtoMessageBuilder {
        sub(fieldNumber) {
            for (value in values) {
                it.bytes(value)
            }
        }
        return this
    }

    // ----------------------------------------------------------------------------
    // Non-Scalar List
    // ----------------------------------------------------------------------------

    fun <T> list(fieldNumber: Int, encoder: ProtoEncoder<T>, values: List<T>): ProtoMessageBuilder {
        for (value in values) {
            writer.bytes(fieldNumber, encoder.encode(value))
        }
        return this
    }

    // ----------------------------------------------------------------------------
    // Non-Primitive
    // ----------------------------------------------------------------------------

    fun <T> instance(fieldNumber: Int, encoder: ProtoEncoder<T>, value: T): ProtoMessageBuilder {
        writer.bytes(fieldNumber, encoder.encode(value))
        return this
    }

    // ----------------------------------------------------------------------------
    // Utility
    // ----------------------------------------------------------------------------

    fun sub(fieldNumber: Int, block: (sub: ProtoBufferWriter) -> Unit) {
        val sub = ProtoBufferWriter()
        block(sub)
        writer.bytes(fieldNumber, sub.pack())
    }
}