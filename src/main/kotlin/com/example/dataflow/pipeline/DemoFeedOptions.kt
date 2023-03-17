package com.example.dataflow.pipeline

import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions
import org.apache.beam.sdk.options.Description
import org.apache.beam.sdk.options.StreamingOptions
import org.apache.beam.sdk.options.Validation

interface DemoFeedOptions : StreamingOptions, DataflowPipelineOptions {
    @get:Validation.Required
    @get:Description("Path of the file to write to")
    var output: String

    var windowSize: Long

    var workerThread: Int

    var tempDirectory: String
}