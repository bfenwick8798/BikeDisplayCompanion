package com.bikedisplay.protocol

data class Ekd01Frame(
    val command: Byte,
    val payload: ByteArray
)

object Ekd01ProtocolCodec {
    private const val START: Byte = 0x7E
    private const val END: Byte = 0x7F

    fun encode(frame: Ekd01Frame): ByteArray {
        val length = frame.payload.size
        require(length <= 255) { "Payload too large" }

        val out = mutableListOf<Byte>()
        out += START
        out += frame.command
        out += length.toByte()
        out += frame.payload.toList()
        out += checksum(frame.command, frame.payload)
        out += END
        return out.toByteArray()
    }

    fun decode(raw: ByteArray): Ekd01Frame {
        require(raw.size >= 5) { "Frame too short" }
        require(raw.first() == START) { "Invalid frame start" }
        require(raw.last() == END) { "Invalid frame end" }

        val command = raw[1]
        val length = raw[2].toInt() and 0xFF
        val payloadStart = 3
        val payloadEnd = payloadStart + length
        require(payloadEnd + 2 == raw.size) { "Invalid frame length" }

        val payload = raw.copyOfRange(payloadStart, payloadEnd)
        val expected = checksum(command, payload)
        val actual = raw[payloadEnd]
        require(actual == expected) { "Checksum mismatch" }

        return Ekd01Frame(command = command, payload = payload)
    }

    private fun checksum(command: Byte, payload: ByteArray): Byte {
        val sum = (command.toInt() and 0xFF) + payload.sumOf { it.toInt() and 0xFF }
        return (sum and 0xFF).toByte()
    }
}

enum class Ekd01Command(val commandByte: Byte) {
    TELEMETRY_REQUEST(0x01),
    TELEMETRY_UPDATE(0x02),
    SET_ASSIST_LEVEL(0x03),
    NAVIGATION_UPDATE(0x04),
    HEARTBEAT(0x05)
}
