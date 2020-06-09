package com.phystech.calculatorapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception


private const val OPERATION ="Operation"
private const val OPERAND1 = "OPERAND1"

class MainActivity : AppCompatActivity() {

    private var operand1 : Double? = null
    private var operand2 : Double = 0.0
    private var pendingOperation = ""


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val listener = View.OnClickListener{v ->
            val b = (v as Button)
            if (b.text == "neg"){
                if (newNumber.text.isEmpty())
                    newNumber.append("-")
//                if (newNumber.text.equals("0.0") && newNumber.text.equals("NaN")){
//                    newNumber.text.clear()
//                    newNumber.text.append("-")
//                }
                else{
                    newNumber.setText("-${newNumber.text}")
                }
            }else{
            newNumber.append(b.text)}
        }

        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)
        buttonNeg.setOnClickListener(listener)


        val operandListener =  View.OnClickListener{v ->
            val op = (v as Button).text.toString()
            val value = newNumber.text.toString()

            if (newNumber.text.isNotEmpty()){
                performOperation(op, value)
            }
            pendingOperation = op
            operation.text = pendingOperation
        }

        buttonMinus.setOnClickListener(operandListener)
        buttonDivide.setOnClickListener(operandListener)
        buttonSum.setOnClickListener(operandListener)
        buttonMultiply.setOnClickListener(operandListener)
        buttonEquals.setOnClickListener(operandListener)

    }
    private fun performOperation(op: String, value: String){
        try{
            if(operand1 == null){
                operand1 = value.toDouble()
            }else{
                operand2 = value.toDouble()
                if(pendingOperation == "="){
                    pendingOperation = op
                }
                when(pendingOperation){
                    "=" -> operand1 = operand2
                    "+" -> operand1 = operand1!! + operand2
                    "-" -> operand1 = operand1!! - operand2
                    "*" -> operand1 = operand1!! * operand2
                    "/" -> if(operand2 == 0.0){
                        operand1 = Double.NaN
                    }else{
                        operand1 = operand1!! / operand2
                    }
                    // Another form of writing if statement
//                    "/" -> operand1 = if(operand2 == 0.0){
//                        Double.NaN
//                    }else{
//                        operand1!! / operand2
                }
            }
            result.setText(operand1.toString())
            newNumber.text.clear()
        }catch(e : Exception){
            result.setText(Double.NaN.toString())
            newNumber.text.clear()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(OPERATION, operation.text.toString())
        if (operand1 != null){
        outState.putDouble(OPERAND1, operand1!!)}
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if(operand1 != null){
        operand1 = savedInstanceState.getDouble(OPERAND1)}

        operation.text =  savedInstanceState.getString(OPERATION)
        pendingOperation = operation.text.toString()


    }

}
