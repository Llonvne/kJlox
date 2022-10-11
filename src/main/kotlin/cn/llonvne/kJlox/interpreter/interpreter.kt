package cn.llonvne.kJlox.interpreter


enum class InterpretResult {
    OK
}

fun interpreter(): InterpretResult {
    return Interpreter().run()
}

private class Interpreter {

    fun run(): InterpretResult {
        while (true) {
//            runLine(readLine() ?: return InterpretResult.OK)
        }
    }
}
