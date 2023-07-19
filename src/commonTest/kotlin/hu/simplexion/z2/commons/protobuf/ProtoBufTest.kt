package hu.simplexion.z2.commons.protobuf

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class ProtoBufTest {

    fun <T> testSinglePrimitive(expected: T, write: ProtoBufferWriter.(value: T) -> Unit, read: ProtoRecord.() -> T) {
        val writer = ProtoBufferWriter()
        writer.write(expected)
        val message = writer.pack()

        val records = ProtoBufferReader(message).records()
        assertEquals(1, records.size)
        val actual = records.first().read()
        if (expected is ByteArray && actual is ByteArray) {
            assertContentEquals(expected, actual)
        } else {
            assertEquals(expected, records.first().read())
        }
    }

    @Test
    fun testPrimitives() {
        testSinglePrimitive(true, { bool(1, it) }) { bool() }
        testSinglePrimitive(false, { bool(1, it) }) { bool() }

        testSinglePrimitive(123, { int32(1, it) }) { int32() }
        testSinglePrimitive(- 123, { int32(1, it) }) { int32() }

        testSinglePrimitive("hello", { string(1, it) }) { string() }
        testSinglePrimitive("hello".encodeToByteArray(), { bytes(1, it) }) { bytes() }
    }

    @Test
    fun testBooleanList() {
        val expected = listOf(true, false, true)
        val writer = ProtoBufferWriter()
        ProtoBooleanList(1).encodeInto(writer, expected)
        val message = writer.pack()

        val records = ProtoBufferReader(message).records()
        val actual = ProtoBooleanList(1).decodeFrom(records)

        assertContentEquals(expected, actual)
    }

    @Test
    fun testIntList() {
        val expected = listOf(1, 2, 3)
        val writer = ProtoBufferWriter()
        ProtoIntList(1).encodeInto(writer, expected)
        val message = writer.pack()

        val records = ProtoBufferReader(message).records()
        val actual = ProtoIntList(1).decodeFrom(records)

        assertContentEquals(expected, actual)
    }

    @Test
    fun testStringList() {
        val expected = listOf("a", "b", "c")
        val writer = ProtoBufferWriter()
        ProtoStringList(1).encodeInto(writer, expected)
        val message = writer.pack()

        val records = ProtoBufferReader(message).records()
        val actual = ProtoStringList(1).decodeFrom(records)

        assertContentEquals(expected, actual)
    }

    data class A(
        var b: Boolean = false,
        var i: Int = 0,
        var s: String = "",
        var l : MutableList<Int> = mutableListOf()
    ) {
        companion object: ProtoEncoder<A>, ProtoDecoder<A> {
            override fun decodeFrom(records: List<ProtoRecord>): A {
                val a = A()
                for (record in records) {
                    when (record.fieldNumber) {
                        1 -> a.b = record.bool()
                        2 -> a.i = record.int32()
                        3 -> a.s = record.string()
                        4 -> a.l += ProtoIntList(4).decodeFrom(record)
                    }
                }
                return a
            }

            override fun encodeInto(writer: ProtoBufferWriter, value: A) {
                writer.bool(1, value.b)
                writer.int32(2, value.i)
                writer.string(3, value.s)
                ProtoIntList(4).encodeInto(writer, value.l)
            }
        }
    }

    @Test
    fun testSimpleClass() {
        val expected = A(true, 12, "hello", mutableListOf(4,5,6))
        val writer = ProtoBufferWriter()
        A.encodeInto(writer, expected)
        val message = writer.pack()

        val records = ProtoBufferReader(message).records()
        val actual = A.decodeFrom(records)

        assertEquals(expected, actual)
    }

    data class B(
        var a: A = A(),
        var s: String = ""
    ) {
        companion object : ProtoEncoder<B>, ProtoDecoder<B> {

            override fun decodeFrom(records: List<ProtoRecord>): B {
                val b = B()
                for (record in records) {
                    when (record.fieldNumber) {
                        1 -> b.a = A.decodeFrom(record)
                        2 -> b.s = record.string()
                    }
                }
                return b
            }

            override fun encodeInto(writer: ProtoBufferWriter, value: B) {
                A.encodeInto(writer, 1, value.a)
                writer.string(2, value.s)
            }

        }
    }

    @Test
    fun testNestedClass() {
        val expected = B(A(true, 123, "a", mutableListOf(4,5,6)), "b")

        val writer = ProtoBufferWriter()
        B.encodeInto(writer, expected)
        val message = writer.pack()

        val records = ProtoBufferReader(message).records()
        val actual = B.decodeFrom(records)

        assertEquals(expected, actual)
    }

    @Test
    fun testListOfNestedClasses() {
        val expected = listOf(
            B(A(true, 123, "a", mutableListOf(1,2,3)), "aa"),
            B(A(false, 456, "b", mutableListOf(4,5,6)), "bb"),
            B(A(true, 789, "c", mutableListOf(7,8,9)), "cc")
        )

        val writer = ProtoBufferWriter()
        ProtoListGen(2, B, B).encodeInto(writer, expected)
        val message = writer.pack()

        val records = ProtoBufferReader(message).records()
        val actual = ProtoListGen(2, B, B).decodeFrom(records)

        assertContentEquals(expected, actual)
    }

}