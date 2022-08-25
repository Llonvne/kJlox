package cn.llonvne.kJlox.error

import cn.llonvne.kJlox.main.KJlox

fun reportError(line: Int, where: String, message: String) {
    KJlox.printkJloxPrefix()

    println("[Line: $line] Error $where : $message !")

    KJlox.hasError = true
}