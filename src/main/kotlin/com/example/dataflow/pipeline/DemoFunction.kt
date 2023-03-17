package pipeline.dev

import org.apache.beam.sdk.transforms.DoFn
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DemoFunction: DoFn<String, String>() {

    @ProcessElement
    fun processElement(@Element recordName: String, context: ProcessContext) {
        logger.info("DemoFunction processElement :: $recordName")
        context.output("$recordName - processed")
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(DemoFunction::class.java)
    }
}