package io.github.jtaeyeon05.why_web.util


fun buildQuery(
    mapQuery: Map<String, String?> = mapOf(),
    listQuery: List<String?> = listOf(),
): String {
    val parts = mutableListOf<String>()
    for ((key, value) in mapQuery) {
        if (value != null) {
            parts.add("$key=${encodeURIComponent(value)}")
        }
    }
    for (query in listQuery) {
        if (query != null) {
            parts.add(query)
        }
    }
    return if (parts.isEmpty()) "" else "?${parts.joinToString("&")}"
}
