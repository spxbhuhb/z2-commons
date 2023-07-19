package hu.simplexion.z2.commons.protobuf

class ProtoRecords(
    val records : List<ProtoRecord>
) {

    operator fun get(fieldNumber: Int) : ProtoRecord =
        records.first { it.fieldNumber == fieldNumber }

    fun boolean(fieldNumber : Int) : Boolean =
        records.firstOrNull { it.fieldNumber == fieldNumber }?.bool() ?: false

    fun int(fieldNumber : Int) : Int =
        records.firstOrNull { it.fieldNumber == fieldNumber }?.int32() ?: 0

    fun string(fieldNumber : Int) : String =
        records.firstOrNull { it.fieldNumber == fieldNumber }?.string() ?: ""

    fun asBoolean() : Boolean {
        check(records.size == 1)
        return records.first().bool()
    }

    fun asInt() : Int {
        check(records.size == 1)
        return records.first().int32()
    }

    fun asString() : String {
        check(records.size == 1)
        return records.first().string()
    }
}