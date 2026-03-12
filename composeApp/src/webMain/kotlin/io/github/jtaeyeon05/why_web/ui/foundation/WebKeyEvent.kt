package io.github.jtaeyeon05.why_web.ui.foundation


data class WebKeyEvent(
    val key: WebKey,
    val status: WebKeyType
) {
    constructor(
        keyCode: String,
        status: WebKeyType
    ) : this(
        key = WebKey.from(keyCode),
        status = status
    )

    val isConfirmPressed get() = isAnyPressed(WebKey.ENTER, WebKey.NUMPAD_ENTER, WebKey.SPACE)
    val isUpPressed get() = isAnyPressed(WebKey.ARROW_UP, WebKey.KEY_W, WebKey.NUMPAD_8)
    val isDownPressed get() = isAnyPressed(WebKey.ARROW_DOWN, WebKey.KEY_S, WebKey.NUMPAD_2)
    val isLeftPressed get() = isAnyPressed(WebKey.ARROW_LEFT, WebKey.KEY_A, WebKey.NUMPAD_4)
    val isRightPressed get() = isAnyPressed(WebKey.ARROW_RIGHT, WebKey.KEY_D, WebKey.NUMPAD_6)

    fun isPressed(key: WebKey) = status == WebKeyType.KEY_DOWN && this.key == key
    fun isAnyPressed(vararg keys: WebKey) = status == WebKeyType.KEY_DOWN && this.key in keys
    fun isReleased(key: WebKey) = status == WebKeyType.KEY_UP && this.key == key
    fun isAnyReleased(vararg keys: WebKey) = status == WebKeyType.KEY_UP && this.key in keys
}

enum class WebKey(val value: String) {
    F1("F1"), F2("F2"), F3("F3"), F4("F4"), F5("F5"), F6("F6"), F7("F7"), F8("F8"), F9("F9"), F10("F10"), F11("F11"), F12("F12"),
    DIGIT_0("Digit0"), DIGIT_1("Digit1"), DIGIT_2("Digit2"), DIGIT_3("Digit3"), DIGIT_4("Digit4"), DIGIT_5("Digit5"), DIGIT_6("Digit6"), DIGIT_7("Digit7"), DIGIT_8("Digit8"), DIGIT_9("Digit9"),
    NUMPAD_0("Numpad0"), NUMPAD_1("Numpad1"), NUMPAD_2("Numpad2"), NUMPAD_3("Numpad3"), NUMPAD_4("Numpad4"), NUMPAD_5("Numpad5"), NUMPAD_6("Numpad6"), NUMPAD_7("Numpad7"), NUMPAD_8("Numpad8"), NUMPAD_9("Numpad9"),
    KEY_A("KeyA"), KEY_B("KeyB"), KEY_C("KeyC"), KEY_D("KeyD"), KEY_E("KeyE"), KEY_F("KeyF"), KEY_G("KeyG"), KEY_H("KeyH"), KEY_I("KeyI"), KEY_J("KeyJ"), KEY_K("KeyK"), KEY_L("KeyL"), KEY_M("KeyM"), KEY_N("KeyN"), KEY_O("KeyO"), KEY_P("KeyP"), KEY_Q("KeyQ"), KEY_R("KeyR"), KEY_S("KeyS"), KEY_T("KeyT"), KEY_U("KeyU"), KEY_V("KeyV"), KEY_W("KeyW"), KEY_X("KeyX"), KEY_Y("KeyY"), KEY_Z("KeyZ"),

    ARROW_UP("ArrowUp"), ARROW_DOWN("ArrowDown"), ARROW_LEFT("ArrowLeft"), ARROW_RIGHT("ArrowRight"),
    BACKSPACE("Backspace"), TAB("Tab"), ENTER("Enter"), ESCAPE("Escape"), SPACE("Space"),
    DELETE("Delete"), INSERT("Insert"), HOME("Home"), END("End"), PAGE_UP("PageUp"), PAGE_DOWN("PageDown"), NUMPAD_ENTER("NumpadEnter"),
    SHIFT_LEFT("ShiftLeft"), SHIFT_RIGHT("ShiftRight"), CONTROL_LEFT("ControlLeft"), CONTROL_RIGHT("ControlRight"), ALT_LEFT("AltLeft"), ALT_RIGHT("AltRight"), META_LEFT("MetaLeft"), META_RIGHT("MetaRight"),

    UNSPECIFIED("Unspecified");

    companion object {
        private val map = entries.associateBy(WebKey::value)
        fun from(value: String): WebKey = map[value] ?: UNSPECIFIED
    }
}

enum class WebKeyType(val value: String) {
    KEY_DOWN("keydown"),
    KEY_UP("keyup"),
    UNSPECIFIED("unspecified")
}
