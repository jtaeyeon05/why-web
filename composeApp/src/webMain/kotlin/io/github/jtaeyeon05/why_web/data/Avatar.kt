package io.github.jtaeyeon05.why_web.data


data class Avatar(
    val avatar: String? = null,
    val name: String
) {
    companion object {
        val Avatars = listOf(
            "😀", "😳", "🫩", "🤯",
            "🥶", "😶‍🌫️", "🤡", "🤠",
            "🤓", "😈", "👹", "🥵",
            "👩‍🚀", "🧑‍🔧", "🕵️", "🧑‍🏭",
        ) // 16개
        val Names = listOf(
            "박 null", "금성 위 생쥐",
            "가나다라마", "X Æ A-12",
            "사람 이름", "남궁펑션",
            "갸라도스", "제갈 rm -rf /",
            "홍' OR 1 = 1 --", "user",
            "정햄버거세트", "김 %s",
        ) // 12개

        val God = Avatar(
            avatar = "🧞",
            name = "??"
        )
        val System = Avatar(
            avatar = "💻",
            name = "시스템"
        )

        fun user(
            avatar: Int? = null,
            name: Int? = null,
        ) = Avatar(
            avatar = Avatars[avatar?.coerceIn(0 ..< Avatars.size) ?: 0],
            name = Names[name?.coerceIn(0 ..< Names.size) ?: 0],
        )
    }
}
