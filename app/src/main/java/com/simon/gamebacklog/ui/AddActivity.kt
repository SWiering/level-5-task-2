package com.simon.gamebacklog.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.simon.gamebacklog.R
import com.simon.gamebacklog.model.Game

import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.content_add.*
import kotlinx.android.synthetic.main.item_game.*
import java.lang.Exception
import java.text.SimpleDateFormat

const val EXTRA_GAME = "EXTRA_GAME"

class AddActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        setSupportActionBar(toolbar)

        initListeners()
    }

    private fun initListeners() {
        fabSave.setOnClickListener{ submit() }
    }

    private fun submit() {
        if(validateEmptyFields()){

            try{
                val dateString = "${etDateDay.text.toString()}-${etDateMonth.text.toString()}-${etDateYear.text.toString()}"
                val dateFormat = SimpleDateFormat("dd-MM-yyyy")
                dateFormat.isLenient = false
                val formDate = dateFormat.parse(dateString)

                val game = Game(etGameTitle.text.toString(), etPlatform.text.toString(), formDate)

                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_GAME, game)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
            catch (e: Exception){
                Toast.makeText(this, "Please fill in a valid date", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun validateEmptyFields(): Boolean {
        // TODO: Also validate range of year and months etc.
        return validateField(etGameTitle, "title")
                && validateField(etPlatform, "platform")
                && validateField(etDateDay, "date")
                && validateField(etDateMonth, "date")
                && validateField(etDateYear, "date")
    }

    private fun validateField(theInput: TextInputEditText?, fieldName: String): Boolean {
        if(theInput?.text.toString().isBlank()){
            Toast.makeText(this, "Please fill in a $fieldName", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
