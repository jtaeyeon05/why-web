package io.github.jtaeyeon05.why_web.util


fun buildQuery(
    queryMap: Map<String, String?> = mapOf(),
    queryList: List<String?> = listOf(),
): String {
    val parts = mutableListOf<String>()
    for ((key, value) in queryMap) {
        if (value != null) {
            parts.add("$key=${encodeURIComponent(value)}")
        }
    }
    for (query in queryList) {
        if (query != null) {
            parts.add(query)
        }
    }
    return if (parts.isEmpty()) "" else "?${parts.joinToString("&")}"
}

operator fun URLSearchParams.get(name: String): String? = getEncoded(name)?.let { decodeURIComponent(it) }
