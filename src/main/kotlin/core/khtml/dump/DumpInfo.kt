package core.khtml.dump

data class DumpInfo(
    val clazz: Class<*>,
    var dir: String = "",
    var screenshot: Boolean = true,
    var condition: DumpCondition = DumpCondition.FAIL
)