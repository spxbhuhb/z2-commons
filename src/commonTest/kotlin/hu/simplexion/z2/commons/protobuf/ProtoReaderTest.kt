package hu.simplexion.z2.commons.protobuf

import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalStdlibApi::class)
class ProtoReaderTest {

    val String.ns
        get() = replace(" ", "")

    val String.int64
        get() = (ProtoReader(this.ns.hexToByteArray()).records().first() as VarintProtoRecord).int64()

    val String.sint64
        get() = (ProtoReader(this.ns.hexToByteArray()).records().first() as VarintProtoRecord).sint64()

    @Test
    fun varint() {
        // unsigned values, '10' is the tag (2) and the VARINT type
        assertEquals(1L, "10 01".int64)
        assertEquals(150L, "10 96 01".int64)
        assertEquals(18789L, "10 e5 92 01".int64)
        assertEquals(Long.MAX_VALUE, "10 ff ff ff ff ff ff ff ff 7f".int64)
        assertEquals(-1, "10 ff ff ff ff ff ff ff ff ff 01".int64)
        assertEquals(-2, "10 fe ff ff ff ff ff ff ff ff 01".int64)

        // signed values, ZigZag encoding, '10' is the tag (2) and the VARINT type
        assertEquals(1L, "10 02".sint64)
        assertEquals(2L, "10 04".sint64)
        assertEquals(-1L, "10 01".sint64)
        assertEquals(-2L, "10 03".sint64)
        assertEquals(150L, "10 ac 02".sint64)
        assertEquals(-150L, "10 ab 02".sint64)
        assertEquals(Long.MAX_VALUE, "10 fe ff ff ff ff ff ff ff ff 01".sint64)
        assertEquals(Long.MIN_VALUE, "10 ff ff ff ff ff ff ff ff ff 01".sint64)
    }
}