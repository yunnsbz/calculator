package com.yunns.calculator


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableRow
import android.widget.Toast
import androidx.core.view.get
import com.yunns.calculator.databinding.FragmentStandardCalculatorBinding
import java.lang.Exception
import kotlin.math.roundToInt


class StandardCalculatorFragment : Fragment() {

    private lateinit var tasarim : FragmentStandardCalculatorBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        tasarim = FragmentStandardCalculatorBinding.inflate(layoutInflater,container,false)
        sayiVeIslemler.removeAllElements()

        //special symbol for divide operator:
        tasarim.buttonBolme.text = 247.toChar().toString()

        var txt = ""
        var isNumberEnteringContinue = true
        var hasUnclosedParentheses: Boolean //parantez hiç yoksa yada tüm parantezler kapalı ise = false

        fun operationButtonClick(islem: Operation){
            if(txt.isNotEmpty()|| islem is ParantezAc) {
                //zaten bir işleme basılmışsa onu değiştir:
                val operationAllreadyEntered = if(islem is ParantezAc) false
                else ( (txt.last() == 'x') || (txt.last() == 247.toChar()) || (txt.last() == '+') || (txt.last() == '-') || (txt.last() == ',') || (txt.last() == '('))&& (islem.sembolDegeri != "(")

                if (operationAllreadyEntered) {
                    txt = txt.removeRange(txt.length - 1 until txt.length)
                    txt += islem.sembolDegeri
                    tasarim.inputTextView.text = txt

                    sayiVeIslemler.pop()
                    sayiVeIslemler.push(islem.stringDegeri)

                    for (i in sayiVeIslemler.indices) Log.e("stack $i:", sayiVeIslemler[i])
                    Log.e("stack end:", "-------------------------")
                } else {

                    txt += islem.sembolDegeri
                    tasarim.inputTextView.text = txt

                    sayiVeIslemler.push(islem.stringDegeri)
                    for (i in sayiVeIslemler.indices) Log.e("stack $i:", sayiVeIslemler[i])
                    Log.e("stack end:", "-------------------------")
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
                            if(sayiVeIslemler.isEmpty() || sayiVeIslemler.lastElement() != "parantezKapa") {
                                txt += button.text.toString()
                                tasarim.inputTextView.text = txt
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
                                    if (sayiVeIslemler.isNotEmpty()) sayiVeIslemler.pop()// yeni gelecek sayıyı eklemek için
                                } else if (isNumberEnteringContinue) { // birçok yerde değişen durum (sayı silmede: true, işlem butonu basma: false)
                                    if (sayiVeIslemler.isNotEmpty()) sayiVeIslemler.pop()
                                }
                                val lastEnteredNumber =
                                    txt.substring(lastEnteredOperationIndex, txt.length)
                                sayiVeIslemler.push(lastEnteredNumber)
                                isNumberEnteringContinue = true
                            }
                        }
                    }
                }
            }
        }

        //silme tuşu
        tasarim.buttonBackspace.setOnClickListener {

            if(txt.isNotEmpty()) {
                val removedChar = txt.last()
                txt = txt.removeRange(txt.length - 1 until txt.length)
                tasarim.inputTextView.text = txt

            //stack'ten silme işlemi:
                for (i in sayiVeIslemler.indices)  Log.e("silmeden önce stack $i:", sayiVeIslemler[i])
                Log.e("stack end:", "-------------------------")

                val lastIndexOfStackIsOperator: Boolean = try {
                    sayiVeIslemler.last().toLong() //sayı ise int dönüşür değilse catch bloğuna geçer
                    false
                } catch (e: Exception) { true }

                if(lastIndexOfStackIsOperator && sayiVeIslemler.isNotEmpty()){
                    sayiVeIslemler.pop()
                } else if(sayiVeIslemler.isNotEmpty()){
                    val tempNum = sayiVeIslemler.last().removeSuffix(removedChar.toString())
                    sayiVeIslemler.pop()
                    isNumberEnteringContinue = if(tempNum != "") {
                        sayiVeIslemler.push(tempNum)
                        true
                    } else false

                }
            }
            else{
                sayiVeIslemler.removeAllElements()
                tasarim.inputTextView.text = ""
                tasarim.outputTextView.text = ""
            }
        }

        //AC herşeyi temizleme butonu:
        tasarim.buttonAc.setOnClickListener {
            txt = ""
            tasarim.inputTextView.text = txt
            tasarim.outputTextView.text = ""
            sayiVeIslemler.removeAllElements()

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
        tasarim.buttonYuzde.setOnLongClickListener {// yüzde butonunun nasıl kullanıldığını göstersin
            Toast.makeText(this.requireContext(),"1. yöntem:  10 %  -> 10'u 100 e böl\n" +
                    "2. yöntem:  10 % 50  -> 10'un yüzde 50'sini hesapla",Toast.LENGTH_LONG)
                .show()
            true
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
            val lastIndexOfStackIsOperator: Boolean = try {
            sayiVeIslemler.last().toLong() //sayı ise int dönüşür değilse catch bloğuna geçer
            false
            } catch (e: Exception) { true }

            //her parantezAc için bir parantezKapa yok ise parantez kapa gelmesi gerekir yani isThereParentheses true olur
            var parenthesesCount = 0
            for(i in sayiVeIslemler.indices){
                if(sayiVeIslemler[i] == "parantezAc") parenthesesCount++
                if(sayiVeIslemler[i] == "parantezKapa") parenthesesCount--
            }
            hasUnclosedParentheses = (parenthesesCount != 0)


            if(sayiVeIslemler.isNotEmpty() && sayiVeIslemler[sayiVeIslemler.size-1] == "parantezKapa") {
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

    //eşittir butonu
        tasarim.buttonEsittir.setOnClickListener {
            for (i in sayiVeIslemler.indices)  Log.e("işlem öncesi stack $i:", sayiVeIslemler[i])
            Log.e("stack :", "-------------------------")

            var parenthesesCount = 0
            for(i in sayiVeIslemler.indices){
                if(sayiVeIslemler[i] == "parantezAc") parenthesesCount++
                if(sayiVeIslemler[i] == "parantezKapa") parenthesesCount--
            }
            hasUnclosedParentheses = (parenthesesCount != 0)

            if(txt.isNotEmpty() && !hasUnclosedParentheses) {
                var result = solveOperation(sayiVeIslemler)
                result = fixFloatingNum(result)

                val redundantFloatingPoint = (result.last() == '0') && (result[result.length-2] == '.')
                if(redundantFloatingPoint){
                    result = result.toDouble().roundToInt().toString()
                }

                tasarim.outputTextView.text = result
                txt = ""
            }
            else Log.e("eşittir tuşu problemi :", "açık parantez var mı: $hasUnclosedParentheses ")
        }

        return tasarim.root
    }
}

