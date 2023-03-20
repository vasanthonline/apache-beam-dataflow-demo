package com.example.dataflow.config

import com.example.dataflow.pipeline.DemoFunction
import com.google.cloud.storage.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class GCSConfig(
    @Value("\${spring.cloud.gcp.project-id}") val projectId: String = "vasanthgopal-5dec6",
    @Value("\${spring.cloud.gcp.cloud-storage-bucket-name}") val bucketName: String = "dataflow-demo-storage-vasanthgopal-5dec6",
) {

    fun uploadFile(fileContents: ByteArray, fileName: String): Boolean {
        val storage: Storage = StorageOptions.newBuilder().setProjectId(projectId).build().service
        val blobId: BlobId = BlobId.of(bucketName, fileName)
        val blobInfo = BlobInfo.newBuilder(blobId).build()
        val blob: Blob = storage.create(blobInfo, fileContents)
        logger.info("File upload success for '$fileName': $blob")
        return true
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(GCSConfig::class.java)
    }
}