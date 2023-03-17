package com.example.dataflow.pipeline

import org.apache.beam.sdk.Pipeline
import org.apache.beam.sdk.coders.StringUtf8Coder
import org.apache.beam.sdk.options.PipelineOptionsFactory
import org.apache.beam.sdk.transforms.Create
import org.apache.beam.sdk.transforms.ParDo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

class DemoFeedPipeline {
    fun createDemoFeedPipeline(options: DemoFeedOptions) {
        logger.info("Start DemoFeedPipeline with options: $options")
        val pipeline: Pipeline = Pipeline.create(options)
        logger.info("DemoFeedPipeline created")

        val words: List<String> = listOf("Sample", "Example")

        pipeline
            .apply(
                "Create collection",
                Create.of(words)
            )
            .setCoder(StringUtf8Coder.of())
            .apply(
            "Transform - Append to string",
                ParDo.of(DemoFunction())
            )
            .apply(
                "Log output",
                ParDo.of(LogFunction())
            )

        pipeline.run()
        logger.info("End Demo Feed Pipeline")
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(DemoFeedPipeline::class.java)
    }
}


fun main(args: Array<String>) {

    val properties = ClassLoader.getSystemClassLoader().getResourceAsStream("pipeline/dev/demo.properties").use {
        Properties().apply {
            load(it)
        }
    }

    val arguments = properties.map { "--${it.key}=${it.value}" }.toTypedArray()
    val options: DemoFeedOptions = PipelineOptionsFactory.fromArgs(*arguments).withValidation().`as`(
        DemoFeedOptions::class.java
    )
    DemoFeedPipeline().createDemoFeedPipeline(options)
}
