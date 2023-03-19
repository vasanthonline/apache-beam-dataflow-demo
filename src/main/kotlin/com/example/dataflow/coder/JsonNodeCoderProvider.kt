package com.example.dataflow.coder

import org.apache.beam.sdk.coders.Coder
import org.apache.beam.sdk.coders.CoderProvider
import org.apache.beam.sdk.values.TypeDescriptor

class JsonNodeCoderProvider: CoderProvider() {

    override fun <T : Any?> coderFor(typeDescriptor: TypeDescriptor<T>, componentCoders: MutableList<out Coder<*>>): Coder<T> {
        return JsonNodeCoder.of() as Coder<T>
    }

    companion object {
        private val JSON_NODE_CODER_PROVIDER = JsonNodeCoderProvider()
        fun of(): JsonNodeCoderProvider {
            return JSON_NODE_CODER_PROVIDER
        }
    }
}