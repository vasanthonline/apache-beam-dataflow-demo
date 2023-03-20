package com.example.dataflow.pipeline.gdrive

import com.example.dataflow.config.GDriveConfig
import com.example.dataflow.config.ObjectMapperConfig
import com.example.dataflow.models.DataFile
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.beam.sdk.transforms.DoFn
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ListFilesFunction: DoFn<String, String>() {
    @ProcessElement
    fun processElement(@Element path: String, context: ProcessContext) {
        logger.info("ListFilesFunction read path :: $path")

        val files = GDriveConfig().getFiles()
        logger.info("Files found $files")

        val dataFiles = files.mapNotNull {
            logger.info("File: ${it.name}, ${it.id}")
            DataFile(id=it.id, name=it.name)
        }
        context.output(objectMapper.writeValueAsString(dataFiles))
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(ListFilesFunction::class.java)
        val objectMapper = ObjectMapperConfig.objectMapper()
    }

}