package com.example.dataflow.config

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.hadoop.util.testing.MockHttpTransportHelper.JSON_FACTORY
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream

@Configuration
class GDriveConfig() {

    @Value("\${gcp.service-account-key-path}")
    val credentialsFilePath: String = "pipeline/credentials/service-account-key.json"

    @Throws(IOException::class)
    private fun getCredentials(): HttpRequestInitializer? {
        // Load client secrets.
        val inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(credentialsFilePath)
        return HttpCredentialsAdapter(
            ServiceAccountCredentials.fromStream(inputStream)
                .createScoped(SCOPES)
        )
    }

    fun getFiles(): List<File> {
        // Build a new authorized API client service.
        val httpTransport: NetHttpTransport = GoogleNetHttpTransport.newTrustedTransport()
        val service: Drive = Drive.Builder(httpTransport, JSON_FACTORY, getCredentials())
            .setApplicationName("Apache Beam Dataflow Demo")
            .build()

        return service.files().list()
            .setPageSize(2)
            .setFields("nextPageToken, files(id, name)")
            .setQ("mimeType != 'application/vnd.google-apps.folder'")
            .execute()
            .files
    }

    fun downloadFile(fileId: String): ByteArrayOutputStream {
        val httpTransport: NetHttpTransport = GoogleNetHttpTransport.newTrustedTransport()
        val service: Drive = Drive.Builder(httpTransport, JSON_FACTORY, getCredentials())
            .setApplicationName("Apache Beam Dataflow Demo")
            .build()

        val outputStream: OutputStream = ByteArrayOutputStream()
        service.files().get(fileId).executeMediaAndDownloadTo(outputStream);
        return outputStream as ByteArrayOutputStream
    }

    companion object {
        const val CREDENTIALS_FILE_PATH = ""
        const val TOKENS_DIRECTORY_PATH = "tokens"
        val SCOPES = listOf(DriveScopes.DRIVE_METADATA_READONLY, DriveScopes.DRIVE_READONLY)
    }
}