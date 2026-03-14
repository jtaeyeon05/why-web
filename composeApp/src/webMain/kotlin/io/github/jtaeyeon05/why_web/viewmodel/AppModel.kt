package io.github.jtaeyeon05.why_web.viewmodel

import io.github.jtaeyeon05.why_web.util.URLSearchParams
import io.github.jtaeyeon05.why_web.util.get


data class AppModel(
    val test1: Int? = null,
    val test2: String? = null,
) {
    fun toQueryMap(): Map<String, String> {
        val queryMap = mutableMapOf<String, String>()
        test1?.let { queryMap["test1"] = test1.toString() }
        test2?.let { queryMap["test2"] = test2 }
        return queryMap
    }

    companion object {
        fun fromQuery(params: URLSearchParams): AppModel {
            params["test1"]
            return AppModel(
                test1 = params["test1"]?.toIntOrNull(),
                test2 = params["test2"],
            )
        }
    }
}
