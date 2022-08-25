package cn.llonvne.kJlox.scanner

class Source(val sourceText: String) {
    var start = 0
    var current = 0
    var line = 1

    /**
     * ## 该函数用于界定当前处理到元素(char) 是否到源代码文件尾部
     * @return 一个 Boolean 值，标识文件是否到达尾部
     */
    fun isAtEnd(): Boolean = current >= sourceText.length

    /**
     * ## 返回当前未处理的字符，并进位到下一个字符
     * @return Char 字符
     */
    fun advance(): Char {
        ++current
        return sourceText[current - 1]
    }

    /**
     * @param expected 期待匹配到下一个字符
     * @return 存在下一个字符且匹配成功返回 true 且跳过下一个字符 ，到达尾部或者匹配失败返回 False
     */
    fun match(expected: Char): Boolean {
        if (isAtEnd()) return false

        if (sourceText[current] == expected) {
            ++current
            return true
        }
        return false
    }

    fun peek(): Char {
        return if (isAtEnd()) '\u0000' else sourceText[current]
    }

    fun peekNext(): Char {
        return if (current + 1 >= sourceText.length) '\u0000' else sourceText[current + 1]
    }
}