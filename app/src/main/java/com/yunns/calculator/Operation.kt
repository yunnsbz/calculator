package com.yunns.calculator

interface Operation{
    val funcIndex: Int  // aynı türdeki fonksiyonları ayırt etmek için kullanılır
    val priorityValue: Int
    val symbolValue: String
}

class Numbers(value: String) : Operation {
    override val funcIndex = -1
    override val priorityValue = -1
    override val symbolValue = value
}

class Multiply : Operation{
    override val funcIndex = 0
    override val priorityValue = 2
    override val symbolValue = "x"
}

class Division : Operation{
    override val funcIndex = 0
    override val priorityValue = 2
    override val symbolValue = 247.toChar().toString()
}

class Addition : Operation{
    override val funcIndex = 1
    override val priorityValue = 1
    override val symbolValue = "+"
}

class Subtraction : Operation{
    override val funcIndex = 1
    override val priorityValue = 1
    override val symbolValue = "-"
}

class Percentage : Operation{
    override val funcIndex = 2
    override val priorityValue = 3
    override val symbolValue = "%"
}

class Mod : Operation{
    override val funcIndex = 6
    override val priorityValue = 2
    override val symbolValue = "mod"
}

class Comma : Operation{
    override val funcIndex = 3
    override val priorityValue = 10
    override val symbolValue = ","
}

class OpenParenthesis : Operation{
    override val funcIndex = 4
    override val priorityValue = 5
    override val symbolValue = "("
}
class ClosedParenthesis : Operation{
    override val funcIndex = 5
    override val priorityValue = 5
    override val symbolValue = ")"
}

//scientific:
class Sinus : Operation{
    override val funcIndex = 6
    override val priorityValue = 3
    override val symbolValue = "sin"
}
class Cosine : Operation{
    override val funcIndex = 6
    override val priorityValue = 3
    override val symbolValue = "cos"
}
class Tangent : Operation{
    override val funcIndex = 6
    override val priorityValue = 3
    override val symbolValue = "tan"
}
class Cotangent : Operation{
    override val funcIndex = 6
    override val priorityValue = 3
    override val symbolValue = "cot"
}
class ArcSinus : Operation{
    override val funcIndex = 6
    override val priorityValue = 3
    override val symbolValue = "arcsin"
}
class ArcCosine : Operation{
    override val funcIndex = 6
    override val priorityValue = 3
    override val symbolValue = "arccos"
}
class ArcTangent : Operation{
    override val funcIndex = 6
    override val priorityValue = 3
    override val symbolValue = "arctan"
}
class ArcCotangent : Operation{
    override val funcIndex = 6
    override val priorityValue = 3
    override val symbolValue = "arccot"
}
class Ln : Operation{
    override val funcIndex = 6
    override val priorityValue = 3
    override val symbolValue = "ln"
}
class Log : Operation{
    override val funcIndex = 6
    override val priorityValue = 3
    override val symbolValue = "log"
}

//others:
class Factorial : Operation{
    override val funcIndex = 7
    override val priorityValue = 3
    override val symbolValue = "!"
}
class Power : Operation{
    override val funcIndex = 8
    override val priorityValue = 4
    override val symbolValue = "^"
}
class SquareRoot : Operation{
    override val funcIndex = 8
    override val priorityValue = 4
    override val symbolValue = (8730).toChar().toString()
}


//val allSymbols : Array<String> = arrayOf("x",247.toChar().toString(),"+","-","%",",","(",")","^",(8730).toChar().toString(),"!","m","o","d")




