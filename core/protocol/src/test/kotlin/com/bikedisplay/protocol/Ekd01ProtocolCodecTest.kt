package com.bikedisplay.protocol

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class Ekd01ProtocolCodecTest {
    @Test
    fun `encode and decode round-trip`() {
        val frame = Ekd01Frame(
            command = Ekd01Command.NAVIGATION_UPDATE.commandByte,
            payload = byteArrayOf(0x10, 0x20, 0x30)
        )

        val encoded = Ekd01ProtocolCodec.encode(frame)
        val decoded = Ekd01ProtocolCodec.decode(encoded)

        assertEquals(frame.command, decoded.command)
        assertContentEquals(frame.payload, decoded.payload)
    }

    @Test
    fun `decode fails on bad checksum`() {
        val valid = Ekd01ProtocolCodec.encode(Ekd01Frame(0x01, byteArrayOf(0x01, 0x02)))
        val tampered = valid.copyOf()
        tampered[tampered.size - 2] = 0x00

        assertFailsWith<IllegalArgumentException> {
            Ekd01ProtocolCodec.decode(tampered)
        }
    }
}
