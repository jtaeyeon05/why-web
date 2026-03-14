package io.github.jtaeyeon05.why_web.viewmodel

import io.github.jtaeyeon05.why_web.util.URLSearchParams
import io.github.jtaeyeon05.why_web.util.get


data class AppModel(
    val avatar: Int? = null,
    val name: Int? = null,
) {
    fun toQueryMap(): Map<String, String> {
        val queryMap = mutableMapOf<String, String>()
        avatar?.let { queryMap["avatar"] = avatar.toString() }
        name?.let { queryMap["name"] = name.toString() }
        return queryMap
    }

    companion object {
        fun fromQuery(params: URLSearchParams): AppModel {
            params["test1"]
            return AppModel(
                avatar = params["avatar"]?.toIntOrNull(),
                name = params["name"]?.toIntOrNull(),
            )
        }
    }
}
