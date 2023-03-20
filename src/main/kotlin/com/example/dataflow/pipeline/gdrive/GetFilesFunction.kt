package com.example.dataflow.pipeline.gdrive

import com.example.dataflow.config.GDriveConfig
import com.example.dataflow.config.ObjectMapperConfig
import com.example.dataflow.models.DataFile
import com.fasterxml.jackson.databind.type.CollectionType
import org.apache.beam.sdk.transforms.DoFn
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class GetFilesFunction: DoFn<String, String>() {

    @ProcessElement
    fun processElement(@Element dataFileStr: String, context: ProcessContext) {
        val javaType: CollectionType = objectMapper.typeFactory
            .constructCollectionType(MutableList::class.java, DataFile::class.java)
        val dataFiles: List<DataFile> = objectMapper.readValue(dataFileStr, javaType)
        val dataFilesWithContents = dataFiles.map {
            val fileContents = GDriveConfig().downloadFile(it.id)
            logger.info("File downloaded for '${it.name}'.")
            DataFile(
                id=it.id,
                name=it.name,
                contents=fileContents.toByteArray()
            )
        }
        context.output(objectMapper.writeValueAsString(dataFilesWithContents))

    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(GetFilesFunction::class.java)
        val objectMapper = ObjectMapperConfig.objectMapper()
    }

}