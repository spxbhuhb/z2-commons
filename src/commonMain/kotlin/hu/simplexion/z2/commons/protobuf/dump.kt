package hu.simplexion.z2.commons.protobuf

import hu.simplexion.z2.commons.util.toDotString

fun ByteArray.protoDump(): String {
    val lines = mutableListOf<String>()
    protoDump(lines)
    return lines.joinToString("\n")
}

fun ByteArray.protoDump(lines: MutableList<String>, indent: String = "") {
    val message = ProtoMessage(this)

    for (record in message.records) {
        when (record) {
            is VarintProtoRecord -> record.protoDump(lines, indent)
            is I32ProtoRecord -> record.protoDump(lines, indent)
            is I64ProtoRecord -> record.protoDump(lines, indent)
            is LenProtoRecord -> record.protoDump(lines, indent)
        }
    }
}

fun VarintProtoRecord.protoDump(lines: MutableList<String>, indent: String = "") {
    lines += "${indent}VARINT  $fieldNumber  ${value.sint64()}"
}


fun I32ProtoRecord.protoDump(lines: MutableList<String>, indent: String = "") {
    lines += "${indent}I32     $fieldNumber  ${value.int32()}"
}


fun I64ProtoRecord.protoDump(lines: MutableList<String>, indent: String = "") {
    lines += "${indent}I64     $fieldNumber  ${value.int64()}"
}


@OptIn(ExperimentalStdlibApi::class)
fun LenProtoRecord.protoDump(lines: MutableList<String>, indent: String = "") {
    val recordBytes = byteArray.copyOfRange(offset, offset + length)

    lines += "${indent}LEN     $fieldNumber  $length  ${recordBytes.toDotString(limit = 100)}  ${recordBytes.toHexString()}"

    try {
        val localLines = mutableListOf<String>()
        recordBytes.protoDump(localLines, "$indent  ")
        lines += localLines
    } catch (ex : Exception) {
        // nothing to do here, probably not a valid record
    }
}