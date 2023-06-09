package com.example.dataflow.pipeline

import com.example.dataflow.coder.JsonNodeCoder
import com.example.dataflow.coder.JsonNodeCoderProvider
import com.example.dataflow.config.GCSConfig
import com.example.dataflow.config.GDriveConfig
import com.example.dataflow.models.DataFile
import com.example.dataflow.pipeline.gcs.UploadFunction
import com.example.dataflow.pipeline.gdrive.GetFilesFunction
import com.example.dataflow.pipeline.gdrive.ListFilesFunction
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonMapperBuilder
import org.apache.beam.sdk.Pipeline
import org.apache.beam.sdk.coders.ByteArrayCoder
import org.apache.beam.sdk.coders.ListCoder
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

        val words: List<String> = listOf("Sample Run")

        pipeline
            .apply(
                "Create collection",
                Create.of(words)
            )
            .setCoder(StringUtf8Coder.of())
            .apply(
            "Read Files from Google drive",
                ParDo.of(ListFilesFunction())
            )
            .setCoder(StringUtf8Coder.of())
            .apply(
                "Download File from Google drive",
                ParDo.of(GetFilesFunction())
            )
            .setCoder(StringUtf8Coder.of())
            .apply(
                "Upload File to GCS",
                ParDo.of(UploadFunction())
            )
            .setCoder(StringUtf8Coder.of())

        pipeline.run()
        logger.info("End Demo Feed Pipeline")
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(DemoFeedPipeline::class.java)
    }
}


fun main(args: Array<String>) {
    val properties = ClassLoader.getSystemClassLoader().getResourceAsStream("pipeline/" + System.getProperty("env") + "/demo.properties").use {
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
