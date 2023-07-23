package hu.simplexion.z2.commons.protobuf

import hu.simplexion.z2.commons.util.UUID
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class ProtoBuiltinTest {

    val booleanVal = true
    val intVal = 123
    val longVal = 1234L
    val stringVal = "abc"
    val byteArrayVal = byteArrayOf(9, 8, 7)
    val uuidVal = UUID<Any>()
    val instanceVal = A(true, 12, "hello")

    val booleanListVal = listOf(true, false, true)
    val intListVal = listOf(1, 2, 3)
    val longListVal = listOf(1L, 2L, 3L, 4L)
    val stringListVal = listOf("a", "b", "c")
    val byteArrayListVal = listOf(byteArrayOf(1), byteArrayOf(2), byteArrayOf(3))
    val uuidListVal = listOf(UUID<Any>(), UUID(), UUID())

    val instanceListVal = listOf(
        B(A(true, 123, "a", mutableListOf(1, 2, 3)), "AA"),
        B(A(false, 456, "b", mutableListOf(4, 5, 6)), "BB"),
        B(A(true, 789, "c", mutableListOf(7, 8, 9)), "CC")
    )

    @Test
    fun testBuiltins() {
        var fieldNumber = 1

        val builder = ProtoMessageBuilder()
            .boolean(fieldNumber++, booleanVal)
            .int(fieldNumber++, intVal)
            .long(fieldNumber++, longVal)
            .string(fieldNumber++, stringVal)
            .byteArray(fieldNumber++, byteArrayVal)
            .uuid(fieldNumber++, uuidVal)
            .instance(fieldNumber++, A, instanceVal)

            .booleanList(fieldNumber++, emptyList())
            .booleanList(fieldNumber++, booleanListVal)

            .intList(fieldNumber++, emptyList())
            .intList(fieldNumber++, intListVal)

            .longList(fieldNumber++, emptyList())
            .longList(fieldNumber++, longListVal)

            .stringList(fieldNumber++, emptyList())
            .stringList(fieldNumber++, stringListVal)

            .byteArrayList(fieldNumber++, emptyList())
            .byteArrayList(fieldNumber++, byteArrayListVal)

            .uuidList(fieldNumber++, emptyList())
            .uuidList(fieldNumber++, uuidListVal)

            .instanceList(fieldNumber++, B, emptyList())
            .instanceList(fieldNumber, B, instanceListVal)

        val wireformat = builder.pack()
        val message = ProtoMessage(wireformat)
        println(wireformat.dumpProto())

        fieldNumber = 1

        assertEquals(booleanVal, message.boolean(fieldNumber++))
        assertEquals(intVal, message.int(fieldNumber++))
        assertEquals(longVal, message.long(fieldNumber++))
        assertEquals(stringVal, message.string(fieldNumber++))
        assertContentEquals(byteArrayVal, message.byteArray(fieldNumber++))
        assertEquals(uuidVal, message.uuid(fieldNumber++))
        assertEquals(instanceVal, message.instance(fieldNumber++, A))

        assertContentEquals(emptyList(), message.booleanList(fieldNumber++))
        assertContentEquals(booleanListVal, message.booleanList(fieldNumber++))

        assertContentEquals(emptyList(), message.intList(fieldNumber++))
        assertContentEquals(intListVal, message.intList(fieldNumber++))

        assertContentEquals(emptyList(), message.longList(fieldNumber++))
        assertContentEquals(longListVal, message.longList(fieldNumber++))

        assertContentEquals(emptyList(), message.stringList(fieldNumber++))
        assertContentEquals(stringListVal, message.stringList(fieldNumber++))

        assertContentEquals(emptyList(), message.byteArrayList(fieldNumber++))
        message.byteArrayList(fieldNumber++).forEachIndexed { index, bytes ->
            assertContentEquals(byteArrayListVal[index], bytes)
        }

        assertContentEquals(emptyList<UUID<Any>>(), message.uuidList(fieldNumber++))
        assertContentEquals(uuidListVal, message.uuidList(fieldNumber++))

        assertContentEquals(emptyList(), message.instanceList(fieldNumber++, B))
        assertContentEquals(instanceListVal, message.instanceList(fieldNumber, B))
    }

    fun <T> ProtoDecoder<T>.decode(build: ProtoMessageBuilder.() -> Unit): T =
        decodeProto(ProtoMessage(ProtoMessageBuilder().apply { build() }.pack()))

    @Test
    fun oneTest() {
        assertEquals(booleanVal, ProtoOneBoolean.decode { boolean(1, booleanVal) })
        assertEquals(intVal, ProtoOneInt.decode { int(1, intVal) })
        assertEquals(longVal, ProtoOneLong.decode { long(1, longVal) })
        assertEquals(stringVal, ProtoOneString.decode { string(1, stringVal) })
        assertContentEquals(byteArrayVal, ProtoOneByteArray.decode { byteArray(1, byteArrayVal) })
        assertEquals(uuidVal, ProtoOneUuid.decode { uuid(1, uuidVal) })
        assertEquals(instanceVal, ProtoOneInstance<A>(A).decode { instance(1, A, instanceVal) })

        assertContentEquals(booleanListVal, ProtoOneBooleanList.decode { booleanList(1, booleanListVal) })
        assertContentEquals(intListVal, ProtoOneIntList.decode { intList(1, intListVal) })
        assertContentEquals(longListVal, ProtoOneLongList.decode { longList(1, longListVal) })
        assertContentEquals(stringListVal, ProtoOneStringList.decode { stringList(1, stringListVal) })
        ProtoOneByteArrayList.decode { byteArrayList(1, byteArrayListVal) }.forEachIndexed { index, bytes ->
            assertContentEquals(byteArrayListVal[index], bytes)
        }
        assertContentEquals(uuidListVal, ProtoOneUuidList.decode { uuidList(1, uuidListVal) })

        assertContentEquals(instanceListVal, ProtoOneInstanceList(B).decode { instanceList(1, B, instanceListVal) })
    }
}