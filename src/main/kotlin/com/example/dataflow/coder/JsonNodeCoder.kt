package com.example.dataflow.coder

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.beam.sdk.coders.CustomCoder
import org.apache.beam.sdk.coders.ListCoder
import org.apache.beam.sdk.coders.StringUtf8Coder
import java.io.InputStream
import java.io.OutputStream


class JsonNodeCoder<T: JsonNode> : CustomCoder<T>() {

    override fun encode(node: T, outStream: OutputStream) {
        val mapper = ObjectMapper()
        val nodeString: String = mapper.writeValueAsString(node)
        outStream.write(nodeString.toByteArray());
    }

    override fun decode(inStream: InputStream): T {
        return StringUtf8Coder.of().decode(inStream) as T
    }

    companion object {
        private val JSON_NODE_CODER = JsonNodeCoder<JsonNode>()
        fun of(): JsonNodeCoder<JsonNode> {
            return JSON_NODE_CODER
        }
    }
}