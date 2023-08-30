package com.yunns.calculator

interface Operation{
    val funcIndex: Int  // aynı türdeki fonksiyonları ayırt etmek için kullanılır
    val oncelikDegeri: Int
    val stringDegeri: String  // işlem yap fonksiyonunda kontrol edilmek üzere stack içine yazılan değer.
    val sembolDegeri: String  // ekranda gösterilmek için kullanılan değer.

}

class Numbers(value: String) : Operation {
    override val funcIndex = -1
    override val oncelikDegeri = -1
    override var stringDegeri = value
    override val sembolDegeri = value
}

class Carpma : Operation{
    override val funcIndex = 0
    override val oncelikDegeri = 2
    override val stringDegeri = "carpma"
    override val sembolDegeri = "x"
}

class Bolme : Operation{
    override val funcIndex = 0
    override val oncelikDegeri = 2
    override val stringDegeri = "bolme"
    override val sembolDegeri = 247.toChar().toString()
}

class Toplama : Operation{
    override val funcIndex = 1
    override val oncelikDegeri = 1
    override val stringDegeri = "toplama"
    override val sembolDegeri = "+"
}

class Cikarma : Operation{
    override val funcIndex = 1
    override val oncelikDegeri = 1
    override val stringDegeri = "cikarma"
    override val sembolDegeri = "-"
}

class Yuzde : Operation{
    override val funcIndex = 2
    override val oncelikDegeri = 3
    override val stringDegeri = "yuzde"
    override val sembolDegeri = "%"
}

class Mod : Operation{
    override val funcIndex = 1
    override val oncelikDegeri = 2
    override val stringDegeri = "mod"
    override val sembolDegeri = "mod"
}

class Virgul : Operation{
    override val funcIndex = 3
    override val oncelikDegeri = 4
    override val stringDegeri = "virgul"
    override val sembolDegeri = ","
}

class OpenParenthesis : Operation{
    override val funcIndex = 4
    override val oncelikDegeri = 5
    override val stringDegeri = "parantezAc"
    override val sembolDegeri = "("
}
class ClosedParenthesis : Operation{
    override val funcIndex = 5
    override val oncelikDegeri = 5
    override val stringDegeri = "parantezKapa"
    override val sembolDegeri = ")"
}

//scientific:
class Sinus : Operation{
    override val funcIndex = 6
    override val oncelikDegeri = 3
    override val stringDegeri = "Sinus"
    override val sembolDegeri = "sin"
}
class Cosine : Operation{
    override val funcIndex = 6
    override val oncelikDegeri = 3
    override val stringDegeri = "Cosine"
    override val sembolDegeri = "cos"
}
class Tangent : Operation{
    override val funcIndex = 6
    override val oncelikDegeri = 3
    override val stringDegeri = "Tangent"
    override val sembolDegeri = "tan"
}
class Cotangent : Operation{
    override val funcIndex = 6
    override val oncelikDegeri = 3
    override val stringDegeri = "Cotangent"
    override val sembolDegeri = "cot"
}
class ArcSinus : Operation{
    override val funcIndex = 6
    override val oncelikDegeri = 3
    override val stringDegeri = "ArcSinus"
    override val sembolDegeri = "arcsin"
}
class ArcCosine : Operation{
    override val funcIndex = 6
    override val oncelikDegeri = 3
    override val stringDegeri = "ArcCosine"
    override val sembolDegeri = "arccos"
}
class ArcTangent : Operation{
    override val funcIndex = 6
    override val oncelikDegeri = 3
    override val stringDegeri = "ArcTangent"
    override val sembolDegeri = "arctan"
}
class ArcCotangent : Operation{
    override val funcIndex = 6
    override val oncelikDegeri = 3
    override val stringDegeri = "ArcCotangent"
    override val sembolDegeri = "arccot"
}
class Ln : Operation{
    override val funcIndex = 6
    override val oncelikDegeri = 3
    override val stringDegeri = "ln"
    override val sembolDegeri = "ln"
}
class Log : Operation{
    override val funcIndex = 6
    override val oncelikDegeri = 3
    override val stringDegeri = "log"
    override val sembolDegeri = "log"
}

//others:
class Factorial : Operation{
    override val funcIndex = 7
    override val oncelikDegeri = 3
    override val stringDegeri = "factorial"
    override val sembolDegeri = "!"
}
class Power : Operation{
    override val funcIndex = 8
    override val oncelikDegeri = 3
    override val stringDegeri = "power"
    override val sembolDegeri = "^"
}
class SquareRoot : Operation{
    override val funcIndex = 8
    override val oncelikDegeri = 3
    override val stringDegeri = "squareRoot"
    override val sembolDegeri = (8730).toChar().toString()
}







