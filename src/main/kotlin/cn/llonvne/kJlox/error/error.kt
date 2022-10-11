package cn.llonvne.kJlox.error

import cn.llonvne.kJlox.main.KJlox
import cn.llonvne.kJlox.token.Token
import cn.llonvne.kJlox.token.TokenType

fun reportError(line: Int, where: String, message: String) {
    KJlox.printkJloxPrefix()

    println("[Line: $line] Error $where : $message !")

    KJlox.hasError = true
}

fun error(token: Token, message: String) {
    if (token.type === TokenType.EOF) {
        reportError(token.line, " at end", message)
    } else {
        reportError(token.line, " at '" + token.lexeme + "'", message)
    }
}