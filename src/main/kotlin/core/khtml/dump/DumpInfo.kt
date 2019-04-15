package core.khtml.dump

import java.lang.reflect.Method

data class DumpInfo(
    val method: Method,
    var dir: String = "",
    var screenshot: Boolean = true,
    var condition: DumpCondition = DumpCondition.FAIL
)