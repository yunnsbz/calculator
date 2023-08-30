package com.yunns.calculator

import android.util.Log
import java.lang.Exception
import java.util.Stack
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.tan


var NumbersAndOperators = Stack<Operation>()

//işlemlerde kullanılacak temp sayılar
var tempNumHolder1 : Double = 0.0
var tempNumHolder2 : Double = 0.0

const val PI = 3.1415926535
const val E  = 2.7182818284

var isStartingWithOperator = false
fun solveOperation(myStack :Stack<Operation>) : String{

    var degreePart = 0.0

    var canLoop : Boolean
    var i = 0
    do{
        when (myStack[i]) {

            is Carpma -> {
                val isMultiplicationPossible =
                    if (i + 2 > myStack.size - 1) true
                    else (myStack[i].oncelikDegeri > myStack[i + 1].oncelikDegeri) && (myStack[i].oncelikDegeri  > myStack[i + 2].oncelikDegeri)

                if (isMultiplicationPossible) {
                    myStack.removeAt(i)
                    tempNumHolder2 = myStack.removeAt(i).stringDegeri.toDouble()
                    val result = tempNumHolder1 * tempNumHolder2
                    myStack.add(i, Numbers(result.toString()))
                    myStack.removeAt(i - 1)  //kalan tempNumHolder1 hala stack içinde onu da kaldırmak gerek

                    i = -1
                }
            }
            is Bolme -> {
                val isDivisionPossible =
                    if (i + 2 > myStack.size - 1) true
                    else (myStack[i].oncelikDegeri > myStack[i + 1].oncelikDegeri) && (myStack[i].oncelikDegeri  > myStack[i + 2].oncelikDegeri)

                if (isDivisionPossible) {
                    myStack.removeAt(i)
                    tempNumHolder2 = myStack.removeAt(i).stringDegeri.toDouble()
                    val result = tempNumHolder1 / tempNumHolder2
                    myStack.add(i, Numbers(result.toString()))
                    myStack.removeAt(i - 1)  //işlemdeki ilk sayı, tempNumHolder1 hala stack içinde onu da kaldırmak gerek

                    i = -1
                }
            }
            is Cikarma -> {
                if(myStack[i + 1] !is OpenParenthesis) {
                    myStack.removeAt(i)
                    tempNumHolder2 = myStack.removeAt(i).stringDegeri.toDouble() * -1
                    myStack.add(i, Toplama())
                    myStack.add(
                        i + 1,
                        Numbers(tempNumHolder2.toString())
                    ) // tempNumHolder2'yi stack te olması gereken yere yerleştirme

                    i = -1
                }
            }
            is Toplama -> {
                val isAdditionPossible =
                    if (i + 2 > myStack.size - 1) true
                    else (myStack[i].oncelikDegeri > myStack[i + 1].oncelikDegeri) && (myStack[i].oncelikDegeri  > myStack[i + 2].oncelikDegeri)
                        //(myStack[i + 2] !is Carpma) && (myStack[i + 2] !is Bolme) && (myStack[i + 2] !is Yuzde) && (myStack[i + 2] !is Virgul) && (myStack[i + 1] !is ParantezAc)

                if (isAdditionPossible) {

                    myStack.removeAt(i)
                    tempNumHolder2 = myStack.removeAt(i).stringDegeri.toDouble()
                    val result = tempNumHolder1 + tempNumHolder2
                    myStack.add(i, Numbers(result.toString()))
                    myStack.removeAt(i-1)  //kalan tempNumHolder1 hala stack içinde onu da kaldırmak gerek

                    i = -1
                }
            }
            is Yuzde -> {
               /*
               1. yöntem:  10 %             -> 10'u 100 e böl
               2. yöntem:  10 % 50          -> 10'un yüzde 50'sini hesapla
               3. yöntem:  sayı işlem 10%   -> sayı'nın yüzde 10'u nu alıp sayı ile işlemi yap
                */
                if(myStack.size <= i+1 || myStack[i + 1] !is OpenParenthesis) {
                    val operatorMethod =
                        if (i + 1 > myStack.size - 1) 1 //1. yöntem yüzde işlemi yap
                        else if (
                            (myStack[i + 1] is Carpma) ||
                            (myStack[i + 1] is Bolme) ||
                            (myStack[i + 1] is Cikarma) ||
                            (myStack[i + 1] is Toplama)
                        ) 1     //true 1. yöntem false 2. yöntem
                        else 2
                    if (operatorMethod == 1) {//1. yöntem
                        myStack.removeAt(i)

                        val newNum = tempNumHolder1 / 100

                        myStack.add(i, Numbers(newNum.toString()))
                        myStack.removeAt(i - 1)// önceki sayıyı kaldır, yeni sayı eklendi

                        i = -1
                    } else {//2. yöntem
                        myStack.removeAt(i)
                        tempNumHolder2 = myStack.removeAt(i).stringDegeri.toDouble()
                        val newNum = tempNumHolder1 * tempNumHolder2 / 100

                        myStack.add(i, Numbers(newNum.toString()))
                        myStack.removeAt(i - 1)// önceki sayıyı kaldır, yeni sayı eklendi

                        i = -1
                    }
                }
            }

            is Virgul -> {
                if(myStack[i + 1] !is OpenParenthesis) {
                    myStack.removeAt(i)
                    var tempNumString = myStack.removeAt(i).stringDegeri
                    //
                    if(tempNumString.last() == '0' && tempNumString[tempNumString.length-2] == '.'){
                        tempNumString = tempNumString.removeRange(tempNumString.length-2,tempNumString.length)
                    }
                    val newNum =
                        tempNumHolder1 + (tempNumString.toDouble() / (10.0).pow(tempNumString.length.toDouble()))
                    myStack.add(i, Numbers(newNum.toString()))
                    myStack.removeAt(i - 1)
                    Log.e("virgüllü sayı", "v: $newNum")

                    i = -1
                }
            }

            is OpenParenthesis -> {
                for (j in i .. myStack.lastIndex){
                    if(myStack[j] is ClosedParenthesis){
                        i = j-1
                        break
                    }
                }
            }

            is ClosedParenthesis -> {
                val tempStack = Stack<Operation>()

                //ilk denk gelinen "parantez aç"ı bul
                for (j in i-1 downTo 0){
                    if(myStack[j] is OpenParenthesis){
                        //ilk denk gelinen "parantez aç"ı kaldır
                        myStack.removeAt(j)

                        // parantez içindeki işlemler geçici stack'e alınır. sayiVeIslemler'den elemanlar silinip yerine işlem sonucu eklenicek
                        for (k in j until i - 1) {
                            tempStack.push(myStack[j])//index sabit çünkü sildikçe öndeki elemanlar bir sıra geriye gelir
                            myStack.removeAt(j)
                        }
                        myStack.removeAt(j)//parantez kapa'yı kaldır

                        var result = solveOperation(tempStack)
                        result = fixFloatingNum(result)
                        myStack.add(j, Numbers(result))

                        i = -1
                        break
                    }
                }
            }

            is Sinus -> { //sin((12-2)-23*(1/2))
                trigonometricFuncPrefix(myStack,i)

                val radian = Math.toRadians(tempNumHolder2)
                var result = sin(radian)
                result = fixFloatingNum(result.toString()).toDouble()

                myStack.add(i, Numbers(result.toString()))


                i = -1
            }
            is Cosine -> {
                trigonometricFuncPrefix(myStack,i)

                val radian = Math.toRadians(tempNumHolder2)
                var result = cos(radian)
                result = fixFloatingNum(result.toString()).toDouble()

                myStack.add(i, Numbers(result.toString()))

                i = -1
            }
            is Tangent -> {
                trigonometricFuncPrefix(myStack,i)

                val radian = Math.toRadians(tempNumHolder2)
                var result = tan(radian)
                result = fixFloatingNum(result.toString()).toDouble()

                myStack.add(i, Numbers(result.toString()))

                i = -1
            }
            is Cotangent -> {
                trigonometricFuncPrefix(myStack,i)

                val radian = Math.toRadians(tempNumHolder2)
                var result = 1 / tan(radian)
                result = fixFloatingNum(result.toString()).toDouble()

                myStack.add(i, Numbers(result.toString()))

                i = -1
            }
            is ArcSinus -> {
                trigonometricFuncPrefix(myStack,i)

                val result = asin(tempNumHolder2)* (180 / PI)

                myStack.add(i, Numbers(result.toString()))
                degreePart += result
                i = -1
            }
            is ArcCosine -> {
                trigonometricFuncPrefix(myStack,i)

                val result = acos(tempNumHolder2)* (180 / PI)

                myStack.add(i, Numbers(result.toString()))
                degreePart += result

                i = -1
            }
            is ArcTangent -> {
                trigonometricFuncPrefix(myStack,i)

                val result = atan(tempNumHolder2)* (180 / PI)

                myStack.add(i, Numbers(result.toString()))
                degreePart += result

                i = -1
            }
            is ArcCotangent -> {
                trigonometricFuncPrefix(myStack,i)

                val result = atan(1.0 / tempNumHolder2)* (180 / PI)

                myStack.add(i, Numbers(result.toString()))
                degreePart += result

                i = -1
            }
            is Ln -> {
                trigonometricFuncPrefix(myStack,i)

                val result = ln(tempNumHolder2)

                myStack.add(i, Numbers(result.toString()))

                i = -1
            }
            is com.yunns.calculator.Log -> {
                trigonometricFuncPrefix(myStack,i)

                val result = log10(tempNumHolder2)

                myStack.add(i, Numbers(result.toString()))

                i = -1
            }

            is Factorial -> {}
            is Power -> {}
            is SquareRoot -> {}


            else -> {//if it's a number
                try {
                    tempNumHolder1 = myStack[i].stringDegeri.toDouble()  //stack'ten çıkarmamalısın
                } catch (_: Exception) {
                    Log.e("(stack okuma) sayi hatası:", "hata: ${myStack[i]}")
                }
            }
        }

        i++
        canLoop = myStack.size -1 >= i

        val canLoopAgain = (myStack.size -1 >= 1) && (!canLoop)
        if(canLoopAgain){
            i = 0
            canLoop = true
        }

    }while (canLoop)

    for (j in myStack.indices)  Log.e("result öncesi stack $j:", myStack[j].stringDegeri)
    Log.e("stack end :", "-------------------------")

    /*
    val result = if(myStack.isNotEmpty() && degreePart != 0.0){//10 +arcsin(1/2) = 10+30degree
        myStack.last().stringDegeri + "+$degreePart${176.toChar()}"
    }
    else if(myStack.isNotEmpty()){
        myStack.last().stringDegeri
    }
    else degreePart.toString() + "${176.toChar()}"
*/

    val result = myStack.last().stringDegeri
    myStack.removeAllElements()

    return result
}



fun trigonometricFuncPrefix(myStack: Stack<Operation>, position: Int){
    var myPosition = position
    myStack.removeAt(myPosition)

    // parantez içinde birden fazla eleman var mı:
    val index = myPosition
    var parenthesesCount = 0
    for(j in myPosition .. myStack.lastIndex){ // sin() içindeki parantez sayısını hesaplar
        if(myStack[j] is OpenParenthesis) parenthesesCount++
        if(myStack[j] is ClosedParenthesis) parenthesesCount--
        myPosition = j-1
        if(parenthesesCount == 0) break //ilk sıfırlandığında çıksın
    }
    myStack.removeAt(index) // ilk "parantezAc"ı kaldırır
    myPosition--
    tempNumHolder2 = if (index != myPosition) {
        val tempStack = Stack<Operation>()
        for (k in index..myPosition) {
            tempStack.push(myStack[index])//index sabit çünkü sildikçe öndeki elemanlar bir sıra geriye gelir
            myStack.removeAt(index)
        }
        solveOperation(tempStack).toDouble()
    } else {
        myStack.removeAt(myPosition).stringDegeri.toDouble()
    }
    myStack.removeAt(index) // "parantezKapa"yı kaldırır

}


fun fixFloatingNum(num:String) : String {

    var newNum = num
    var virguldenSonra = ""
    var virguldenOnce = ""
    for (i in num.indices.reversed()) {
        if (num[i] == '.') {
            virguldenSonra = num.substring(i + 1, num.length)
            virguldenOnce = num.substring(0, i)
            break
        }
        if (num[i] == 'E') {
            virguldenSonra = "" //uzun sayılar için güvenlik
            break
        }
    }

    // örnek durum: "234.0000000000000000002"  ->
    for (i in num.indices.reversed()) {
        if (virguldenSonra.length >= 3) {
            val hasRedundantFloatingPoint =
                (num[i] == '0') && (num[i - 1] == '0') && (num[i - 2] == '0') && (num[i - 3] == '.')
            if (hasRedundantFloatingPoint) {
                return num.toDouble().roundToInt().toString()
            }
        }
    }

    //1233.0 --> 1233
    val hasRedundantFloatingPoint = (num.last() == '0') && (num[num.length - 2] == '.')
    if (hasRedundantFloatingPoint) {
        return num.toDouble().roundToInt().toString()
    }

    if (virguldenSonra.length >= 3) {
        for (i in virguldenSonra.indices.reversed()) {
            if (i - 2 < 0) break
            if ((virguldenSonra[i] == '9') && (virguldenSonra[i - 1] == '9') && (virguldenSonra[i - 2] != '9')) {
                virguldenSonra = virguldenSonra.substring(0, i - 1)
                var tempChar = virguldenSonra.substring(i - 2, i - 1).toInt()
                virguldenSonra = virguldenSonra.substring(0, i - 2)
                tempChar++
                virguldenSonra += tempChar.toString()
                println("$virguldenSonra  :$tempChar")
                break
            } else if ((virguldenSonra[i] == '0') && (virguldenSonra[i - 1] == '0') && (virguldenSonra[i - 2] != '0')) {
                virguldenSonra = virguldenSonra.substring(0, i - 1)
                break
            }
        }

        newNum = "$virguldenOnce.$virguldenSonra"
    }
    return newNum

}
