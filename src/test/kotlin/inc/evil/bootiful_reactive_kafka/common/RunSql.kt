package inc.evil.bootiful_reactive_kafka.common

@Target(AnnotationTarget.FUNCTION)
annotation class RunSql(val scripts: Array<String>)
