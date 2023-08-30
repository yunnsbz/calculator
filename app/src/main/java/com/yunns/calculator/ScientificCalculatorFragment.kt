package com.yunns.calculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableRow
import androidx.core.view.get
import com.yunns.calculator.databinding.FragmentScientificCalculatorBinding



class ScientificCalculatorFragment : Fragment() {

    lateinit var tasarim: FragmentScientificCalculatorBinding


    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        tasarim = FragmentScientificCalculatorBinding.inflate(layoutInflater,container,false)
        NumbersAndOperators.removeAllElements()

        tasarim.buttonUs.text = "${(696).toChar()}${(735).toChar()}"
        tasarim.buttonBolme.text = 247.toChar().toString()
        tasarim.buttonPi.text = 960.toChar().toString()
        tasarim.buttonKok.text = "${(8730).toChar()}"
        //"${(178).toChar()}${(8730).toChar()}"
        /* char codes:
        708 ^
        8730 kök
        185 üs 1
        178 üs 2
        179 üs 3
        176 derece
         */

        var txt = ""
        var isNumberEnteringContinue = true
        var hasUnclosedParentheses: Boolean //parantez hiç yoksa yada tüm parantezler kapalı ise = false


        fun operationButtonClick(islem: Operation){
            val startControl = (islem is ParantezAc) || (islem.funcIndex == 6)
            if(txt.isNotEmpty() || startControl) {
                //zaten bir işleme basılmışsa onu değiştir:
                val operationAllreadyEntered = if(startControl) false
                else ( (txt.last() == 'x') || (txt.last() == 247.toChar()) || (txt.last() == '+') || (txt.last() == '-') || (txt.last() == ',') || (txt.last() == '('))&& (islem.sembolDegeri != "(")

                if (operationAllreadyEntered) {
                    txt = txt.removeRange(txt.length - 1 until txt.length)
                    txt += islem.sembolDegeri
                    tasarim.inputTextView2.text = txt

                    NumbersAndOperators.pop()
                    NumbersAndOperators.push(islem)


                } else {
                    txt += islem.sembolDegeri
                    tasarim.inputTextView2.text = txt

                    NumbersAndOperators.push(islem)

                }

                if(islem.funcIndex == 6) { //trigonometric func
                    operationButtonClick(ParantezAc())
                }

                isNumberEnteringContinue = false
            }
        }

        //keyboard:
        // on click listener for numbers:
        for (i in 0 until tasarim.tableLayout.childCount){

            if(tasarim.tableLayout[i] is TableRow){

                val row = tasarim.tableLayout[i] as TableRow

                //her satırdaki button'a ulaşma
                for (j in 0 until row.childCount){

                    val button = row[j] as Button

                    if(button.contentDescription == "num") {
                        button.setOnClickListener{
                            if(NumbersAndOperators.isEmpty() || NumbersAndOperators.lastElement() !is ParantezKapa) {
                                txt += button.text.toString()
                                tasarim.inputTextView2.text = txt

                                //sayıyı Stack'e ekleme:
                                var isThereOperator = true
                                var lastEnteredOperationIndex = 0
                                val hasOperatorAtEnd =
                                    if (txt.isNotEmpty()) { //eleman silindiği için tekrar kontrol etmek gerekli
                                        (txt.last() == 'x') || (txt.last() == 247.toChar()) || (txt.last() == '+') || (txt.last() == '-') || (txt.last() == '%') || (txt.last() == ',') || (txt.last() == '(') || (txt.last() == ')')
                                    } else false
                                for (k in txt.indices.reversed()) {
                                    if (hasOperatorAtEnd) continue
                                    val whereOperationEntered =
                                        (txt[k] == 'x') || (txt[k] == 247.toChar()) || (txt[k] == '+') || (txt[k] == '-') || (txt[k] == '%') || (txt[k] == ',') || (txt[k] == '(') || (txt[k] == ')')
                                    if (whereOperationEntered) {
                                        lastEnteredOperationIndex = k + 1
                                        isThereOperator = true
                                        break
                                    } else {
                                        isThereOperator = false
                                    }
                                }
                                if (!isThereOperator) {
                                    lastEnteredOperationIndex = 0
                                    if (NumbersAndOperators.isNotEmpty()) NumbersAndOperators.pop()// yeni gelecek sayıyı eklemek için
                                } else if (isNumberEnteringContinue) { // birçok yerde değişen durum (sayı silmede: true, işlem butonu basma: false)
                                    if (NumbersAndOperators.isNotEmpty()) NumbersAndOperators.pop()
                                }
                                val lastEnteredNumber =
                                    txt.substring(lastEnteredOperationIndex, txt.length)
                                NumbersAndOperators.push(Number(lastEnteredNumber))
                                isNumberEnteringContinue = true
                            }
                        }
                    }
                }
            }
        }

        tasarim.buttonE.setOnClickListener {
            if(NumbersAndOperators.isEmpty() || NumbersAndOperators.lastElement() !is ParantezKapa) {
                txt += "e" // E symbol
                tasarim.inputTextView2.text = txt

                //sayıyı Stack'e ekleme:
                NumbersAndOperators.push(Number(E.toString()))
                isNumberEnteringContinue = false
            }
        }

        tasarim.buttonPi.setOnClickListener {
            if(NumbersAndOperators.isEmpty() || NumbersAndOperators.lastElement() !is ParantezKapa) {
                txt += 960.toChar().toString() // pi symbol
                tasarim.inputTextView2.text = txt

                //sayıyı Stack'e ekleme:
                NumbersAndOperators.push(Number(PI.toString()))
                isNumberEnteringContinue = false
            }
        }



        tasarim.buttonBackspace.setOnClickListener {

            if(txt.isNotEmpty()) {
                val removedChar = txt.last()
                txt = txt.removeRange(txt.length - 1 until txt.length)
                tasarim.inputTextView2.text = txt

                //stack'ten silme işlemi:
                for (i in NumbersAndOperators.indices)  Log.e("silmeden önce stack $i:", NumbersAndOperators[i].stringDegeri)
                Log.e("stack end:", "-------------------------")

                val lastIndexOfStackIsOperator: Boolean = (NumbersAndOperators.last() !is Number)

                if(lastIndexOfStackIsOperator && NumbersAndOperators.isNotEmpty()){
                    NumbersAndOperators.pop()
                } else if(NumbersAndOperators.isNotEmpty()){
                    val tempNum = NumbersAndOperators.last().stringDegeri.removeSuffix(removedChar.toString())
                    NumbersAndOperators.pop()
                    isNumberEnteringContinue = if(tempNum != "") {
                        NumbersAndOperators.push(Number(tempNum))
                        true
                    } else false

                }
            }
            else{
                NumbersAndOperators.removeAllElements()
                tasarim.inputTextView2.text = ""
                tasarim.outputTextView2.text = ""
            }
        }

        tasarim.buttonAc.setOnClickListener {
            txt = ""
            tasarim.inputTextView2.text = txt
            tasarim.outputTextView2.text = ""
            NumbersAndOperators.removeAllElements()

            isStartingWithOperator = false
            hasUnclosedParentheses = false
        }


        //operation buttons:
        tasarim.buttonCarpma.setOnClickListener {
            operationButtonClick(Carpma())
        }
        tasarim.buttonBolme.setOnClickListener {
            operationButtonClick(Bolme())
        }
        tasarim.buttonToplama.setOnClickListener {
            operationButtonClick(Toplama())
        }
        tasarim.buttonCikarma.setOnClickListener {
            operationButtonClick(Cikarma())
        }
        tasarim.buttonYuzde.setOnClickListener {
            //yüzde diğer işlemlerden farklı olarak sayıdan sonra kullanılıyor (işlem yüzde ile bitebilir.)
            operationButtonClick(Yuzde())
        }

        tasarim.buttonVirgul.setOnClickListener {
            operationButtonClick(Virgul())
        }

        tasarim.buttonParantez.setOnClickListener {
            /*
            1. durum: parantez hiç yoksa her durumda "parantez aç" gelir
                sayı işlem parantez ... parantez -> sayı parantez içi ile (işlem önceliğine göre) işlemi gerçekleştirir
                sayı parantez ... parantez -> sayı parantez içi ile çarpım durumundadır
            2. durum: parantez var parantez içindeki son elemanın sayı ya da işlem olmasına göre parantezin açılıp kapanacağı belirlenir:
                ... "parantez aç" sayı işlem "parantez aç" ...
                ... "parantez aç" sayı "parantez kapa" ...
            özel durumlar:
                "(("  ->  parantez açık varsa tekrar parantez eklenirse yine parantez aç gelir
                "))"  ->  parantez kapalı varsa ve tekrar parantez tuşuna basılırsa parantez kapa gelir
             */
            val lastIndexOfStackIsOperator: Boolean = (NumbersAndOperators.last() !is Number)

            //her parantezAc için bir parantezKapa yok ise parantez kapa gelmesi gerekir yani isThereParentheses true olur
            var parenthesesCount = 0
            for(i in NumbersAndOperators.indices){
                if(NumbersAndOperators[i] is ParantezAc) parenthesesCount++
                if(NumbersAndOperators[i] is ParantezKapa) parenthesesCount--
            }
            hasUnclosedParentheses = (parenthesesCount != 0)


            if(NumbersAndOperators.isNotEmpty() && NumbersAndOperators[NumbersAndOperators.size-1] is ParantezKapa) {
                operationButtonClick(ParantezKapa()) //özel durum 2. seçenek "))"
            }
            else {
                if (hasUnclosedParentheses && lastIndexOfStackIsOperator) { //2. durum 1. seçenek (parantez aç)
                    operationButtonClick(ParantezAc())
                    isStartingWithOperator = true
                } else if (hasUnclosedParentheses) { // 2. durum 2. seçenek (parantez kapa)
                    operationButtonClick(ParantezKapa())
                }

                if (!hasUnclosedParentheses && lastIndexOfStackIsOperator) { // 1. durum 1. seçenek (parantez aç)
                    operationButtonClick(ParantezAc())
                    hasUnclosedParentheses = true
                    isStartingWithOperator = true
                } else if (!hasUnclosedParentheses) { // 1. durum 2. seçenek (parantez aç)
                    operationButtonClick(ParantezAc())
                    hasUnclosedParentheses = true
                }
            }
        }

        //equal button
        tasarim.buttonEsittir.setOnClickListener {
            for (i in NumbersAndOperators.indices)  Log.e("işlem öncesi stack $i:", NumbersAndOperators[i].stringDegeri)
            Log.e("stack :", "-------------------------")

            var parenthesesCount = 0
            for(i in NumbersAndOperators.indices){
                if(NumbersAndOperators[i] is ParantezAc) parenthesesCount++
                if(NumbersAndOperators[i] is ParantezKapa) parenthesesCount--
            }
            hasUnclosedParentheses = (parenthesesCount != 0)

            if(txt.isNotEmpty() && !hasUnclosedParentheses) {
                var result = solveOperation(NumbersAndOperators)
                result = fixFloatingNum(result)

                tasarim.outputTextView2.text = result
                txt = ""
            }
            else Log.e("eşittir tuşu problemi :", "açık parantez var mı: $hasUnclosedParentheses ")
        }

        // scientific buttons:
        var isShiftClicked = false
        tasarim.buttonShift.setOnClickListener {
            if(!isShiftClicked) {
                tasarim.buttonSin.text = "arcsin"
                tasarim.buttonSin.textSize = 20.0f
                tasarim.buttonCos.text = "arccos"
                tasarim.buttonCos.textSize = 20.0f
                tasarim.buttonCot.text = "arccot"
                tasarim.buttonCot.textSize = 20.0f
                tasarim.buttonTan.text = "arctan"
                tasarim.buttonTan.textSize = 20.0f
                isShiftClicked = true
            }
            else{
                tasarim.buttonSin.text = "sin"
                tasarim.buttonSin.textSize = 24.0f
                tasarim.buttonCos.text = "cos"
                tasarim.buttonCos.textSize = 24.0f
                tasarim.buttonCot.text = "cot"
                tasarim.buttonCot.textSize = 24.0f
                tasarim.buttonTan.text = "tan"
                tasarim.buttonTan.textSize = 24.0f
                isShiftClicked = false
            }
        }

        tasarim.buttonSin.setOnClickListener {
            if(!isShiftClicked) operationButtonClick( Sinus() )
            else operationButtonClick( ArcSinus() )
        }
        tasarim.buttonCos.setOnClickListener {
            if(!isShiftClicked) operationButtonClick( Cosine() )
            else operationButtonClick( ArcCosine() )
        }
        tasarim.buttonCot.setOnClickListener {
            if(!isShiftClicked) operationButtonClick( Cotangent() )
            else operationButtonClick( ArcCotangent() )
        }
        tasarim.buttonTan.setOnClickListener {
            if(!isShiftClicked) operationButtonClick( Tangent() )
            else operationButtonClick( ArcTangent() )
        }

        tasarim.buttonLog.setOnClickListener {
            operationButtonClick( Log() )
        }
        tasarim.buttonLn.setOnClickListener {
            operationButtonClick( Ln() )
        }
        tasarim.buttonKok.setOnClickListener {
            operationButtonClick( SquareRoot() )
        }
        tasarim.buttonUs.setOnClickListener {
            operationButtonClick( Power() )
        }
        tasarim.buttonMod.setOnClickListener {
            operationButtonClick( Mod() )
        }
        tasarim.buttonFactorial.setOnClickListener {
            operationButtonClick( Factorial() )
        }



        return tasarim.root
    }
}