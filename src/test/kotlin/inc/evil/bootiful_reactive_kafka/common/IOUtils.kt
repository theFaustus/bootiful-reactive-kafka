package inc.evil.bootiful_reactive_kafka.common

import org.apache.commons.io.IOUtils

class IOUtils {
    companion object {
        fun readResource(path: String): String? = IOUtils.toString(this::class.java.getResourceAsStream(path), "UTF-8")
    }
}
