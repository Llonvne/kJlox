package cn.llonvne.kJlox.interpreter

enum class InterpretResult {
    OK
}

fun interpret(): InterpretResult {
    return Interpreter().run()
}

private class Interpreter {

    fun run(): InterpretResult {
        while (true) {
            run_line(readLine() ?: return InterpretResult.OK)
        }
    }

    fun run_line(line: String) {

    }
}
