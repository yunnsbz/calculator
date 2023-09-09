package com.yunns.calculator

val allSymbols : Array<String> = arrayOf("x",247.toChar().toString(),"+","-","%",",","(",")","^",(8730).toChar().toString(),"!","m","o","d")

interface Operation{
    val funcIndex: Int  // aynı türdeki fonksiyonları ayırt etmek için kullanılır
    val priorityValue: Int
    val stringValue: String
    val symbolValue: String
}

class Numbers(value: String) : Operation {
    override val funcIndex = -1
    override val priorityValue = -1
    override var stringValue = value
    override val symbolValue = value
}

class Carpma : Operation{
    override val funcIndex = 0
    override val priorityValue = 2
    override val stringValue = "carpma"
    override val symbolValue = "x"
}

class Bolme : Operation{
    override val funcIndex = 0
    override val priorityValue = 2
    override val stringValue = "bolme"
    override val symbolValue = 247.toChar().toString()
}

class Toplama : Operation{
    override val funcIndex = 1
    override val priorityValue = 1
    override val stringValue = "toplama"
    override val symbolValue = "+"
}

class Cikarma : Operation{
    override val funcIndex = 1
    override val priorityValue = 1
    override val stringValue = "cikarma"
    override val symbolValue = "-"
}

class Yuzde : Operation{
    override val funcIndex = 2
    override val priorityValue = 3
    override val stringValue = "yuzde"
    override val symbolValue = "%"
}

class Mod : Operation{
    override val funcIndex = 6
    override val priorityValue = 2
    override val stringValue = "mod"
    override val symbolValue = "mod"
}

class Virgul : Operation{
    override val funcIndex = 3
    override val priorityValue = 10
    override val stringValue = "virgul"
    override val symbolValue = ","
}

class OpenParenthesis : Operation{
    override val funcIndex = 4
    override val priorityValue = 5
    override val stringValue = "parantezAc"
    override val symbolValue = "("
}
class ClosedParenthesis : Operation{
    override val funcIndex = 5
    override val priorityValue = 5
    override val stringValue = "parantezKapa"
    override val symbolValue = ")"
}

//scientific:
class Sinus : Operation{
    override val funcIndex = 6
    override val priorityValue = 3
    override val stringValue = "Sinus"
    override val symbolValue = "sin"
}
class Cosine : Operation{
    override val funcIndex = 6
    override val priorityValue = 3
    override val stringValue = "Cosine"
    override val symbolValue = "cos"
}
class Tangent : Operation{
    override val funcIndex = 6
    override val priorityValue = 3
    override val stringValue = "Tangent"
    override val symbolValue = "tan"
}
class Cotangent : Operation{
    override val funcIndex = 6
    override val priorityValue = 3
    override val stringValue = "Cotangent"
    override val symbolValue = "cot"
}
class ArcSinus : Operation{
    override val funcIndex = 6
    override val priorityValue = 3
    override val stringValue = "ArcSinus"
    override val symbolValue = "arcsin"
}
class ArcCosine : Operation{
    override val funcIndex = 6
    override val priorityValue = 3
    override val stringValue = "ArcCosine"
    override val symbolValue = "arccos"
}
class ArcTangent : Operation{
    override val funcIndex = 6
    override val priorityValue = 3
    override val stringValue = "ArcTangent"
    override val symbolValue = "arctan"
}
class ArcCotangent : Operation{
    override val funcIndex = 6
    override val priorityValue = 3
    override val stringValue = "ArcCotangent"
    override val symbolValue = "arccot"
}
class Ln : Operation{
    override val funcIndex = 6
    override val priorityValue = 3
    override val stringValue = "ln"
    override val symbolValue = "ln"
}
class Log : Operation{
    override val funcIndex = 6
    override val priorityValue = 3
    override val stringValue = "log"
    override val symbolValue = "log"
}

//others:
class Factorial : Operation{
    override val funcIndex = 7
    override val priorityValue = 3
    override val stringValue = "factorial"
    override val symbolValue = "!"
}
class Power : Operation{
    override val funcIndex = 8
    override val priorityValue = 4
    override val stringValue = "power"
    override val symbolValue = "^"
}
class SquareRoot : Operation{
    override val funcIndex = 8
    override val priorityValue = 4
    override val stringValue = "squareRoot"
    override val symbolValue = (8730).toChar().toString()
}







