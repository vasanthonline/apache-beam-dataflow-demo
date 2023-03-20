package com.example.dataflow.pipeline.gcs

import com.example.dataflow.config.GCSConfig
import com.example.dataflow.config.ObjectMapperConfig
import com.example.dataflow.models.DataFile
import com.example.dataflow.pipeline.gdrive.GetFilesFunction
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.CollectionType
import org.apache.beam.sdk.transforms.DoFn
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class UploadFunction: DoFn<String, String>() {

    @ProcessElement
    fun processElement(@Element dataFileStr: String, context: ProcessContext) {
        val javaType: CollectionType = GetFilesFunction.objectMapper.typeFactory
            .constructCollectionType(MutableList::class.java, DataFile::class.java)
        val dataFiles: List<DataFile> = GetFilesFunction.objectMapper.readValue(dataFileStr, javaType)
        dataFiles.forEach { dataFile ->
            dataFile.contents?.let { GCSConfig().uploadFile(it, dataFile.name) }
            logger.info("File '${dataFile.name}' uploaded successfully.")
        }
        context.output("Success")
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(UploadFunction::class.java)
        val objectMapper = ObjectMapperConfig.objectMapper()
    }

}