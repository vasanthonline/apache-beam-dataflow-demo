package com.example.dataflow.pipeline

import org.apache.beam.sdk.transforms.DoFn
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LogFunction: DoFn<String, String>() {

    @ProcessElement
    fun processElement(@Element recordName: String, context: ProcessContext) {
        logger.info("LogFunction processElement :: $recordName")
        context.output(recordName)
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(DemoFunction::class.java)
    }
}