package com.example.dataflow.pipeline.gdrive

import com.example.dataflow.config.GDriveConfig
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList
import com.google.cloud.hadoop.util.testing.MockHttpTransportHelper.JSON_FACTORY
import org.apache.beam.sdk.transforms.DoFn
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ListFilesFunction: DoFn<String, String>() {
    @ProcessElement
    fun processElement(@Element path: String, context: ProcessContext) {
        logger.info("ListFilesFunction read path :: $path")

        val files = GDriveConfig().getFiles()
        logger.info("Files found $files")

        val fileIds = files.mapNotNull {
            logger.info("File: ${it.name}, ${it.id}")
            it.id
        }
        context.output(fileIds.joinToString(","))
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(ListFilesFunction::class.java)
    }

}