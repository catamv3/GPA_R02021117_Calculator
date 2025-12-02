package com.example.gpa_r02021117_calculator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var classGradeOne: EditText
    private lateinit var classGradeTwo: EditText
    private lateinit var classGradeThree: EditText
    private lateinit var classGradeFour: EditText
    private lateinit var classGradeFive: EditText

    private lateinit var computeButton: Button

    private lateinit var displayGPA: TextView

    private var currentBackgroundColor: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //initializing class grade TextFields
        classGradeOne = findViewById(R.id.course1)
        classGradeTwo = findViewById(R.id.course2)
        classGradeThree = findViewById(R.id.course3)
        classGradeFour = findViewById(R.id.course4)
        classGradeFive = findViewById(R.id.course5)

        //initializing button
        computeButton = findViewById(R.id.btnCompute)

        //Initializing displaying grade
        displayGPA = findViewById(R.id.tvResult)

        //restore saved state if it exists
        if (savedInstanceState != null) {
            classGradeOne.setText(savedInstanceState.getString("grade1", ""))
            classGradeTwo.setText(savedInstanceState.getString("grade2", ""))
            classGradeThree.setText(savedInstanceState.getString("grade3", ""))
            classGradeFour.setText(savedInstanceState.getString("grade4", ""))
            classGradeFive.setText(savedInstanceState.getString("grade5", ""))
            displayGPA.text = savedInstanceState.getString("gpaResult", getString(R.string.gpa_result))
            computeButton.setText(savedInstanceState.getString("buttonText", getString(R.string.compute_gpa)))

            currentBackgroundColor = savedInstanceState.getInt("backgroundColor", resources.getColor(R.color.white))
            findViewById<View>(R.id.main).setBackgroundColor(currentBackgroundColor)
        } else {
            currentBackgroundColor = resources.getColor(R.color.white)
        }

        //setup TextWatchers on all grade fields
        setupTextWatchers()

        //Set up compute button click listener
        computeButton.setOnClickListener { view ->
            onHandleComputeButton(view)
        }
    }

    /**
     * Saving the instance state to preserve user inputs, displayed GPA,
     * button text, and background color across configuration changes.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        //save all grade inputs
        outState.putString("grade1", classGradeOne.text.toString())
        outState.putString("grade2", classGradeTwo.text.toString())
        outState.putString("grade3", classGradeThree.text.toString())
        outState.putString("grade4", classGradeFour.text.toString())
        outState.putString("grade5", classGradeFive.text.toString())

        //save displayed GPA result
        outState.putString("gpaResult", displayGPA.text.toString())

        //save button text
        outState.putString("buttonText", computeButton.text.toString())

        //save background color
        outState.putInt("backgroundColor", currentBackgroundColor)
    }

    /**
     * onHandle function that can compute or clear the form
     */
    fun onHandleComputeButton(view: View) {

        //if the button text says "Clear Form", the whole form will be cleared
        if(computeButton.text.equals("Clear Form")){
            return clearForm()
        }

        //Validating user's inputs to make sure the EditFields are not empty
        if (!validateGrade(classGradeOne, classGradeTwo, classGradeThree, classGradeFour, classGradeFive)) {
            return
        }

        //getting user's grades, if there is an empty TextField, it will be highlighted red
        val grade1 = classGradeOne.text.toString().toDouble()
        val grade2 = classGradeTwo.text.toString().toDouble()
        val grade3 = classGradeThree.text.toString().toDouble()
        val grade4 = classGradeFour.text.toString().toDouble()
        val grade5 = classGradeFive.text.toString().toDouble()

        //calculating the user's gpa
        val gpa = (grade1 + grade2 + grade3 + grade4 + grade5) / 5.0

        //changing background depending on the user's GPA
        if(gpa < 60) {
            //changes the background to red if gpa is < 60
            currentBackgroundColor = resources.getColor(R.color.red_bg)
            findViewById<View>(R.id.main).setBackgroundColor(currentBackgroundColor)
        }
        else if ((gpa >= 61) and (gpa <= 79)) {
            //changes the background to yellow if gpa is >= 61 and gpa <= 79
            currentBackgroundColor = resources.getColor(R.color.yellow_bg)
            findViewById<View>(R.id.main).setBackgroundColor(currentBackgroundColor)
        }
        else if ((gpa >= 80) and (gpa <= 100)){
            //changes the background to green if gpa >= 80 and gpa <= 100
            currentBackgroundColor = resources.getColor(R.color.green_bg)
            findViewById<View>(R.id.main).setBackgroundColor(currentBackgroundColor)
        }

        //gets the displayGPA R.string and concatenates with the gpa
        val totalGPA = String.format("GPA: %.2f", gpa)

        //displays the user's final gpa and compute button gets changed to "Clear Form"
        displayGPA.text = totalGPA
        computeButton.setText(R.string.clear_form)

        //clears any TextFields that were highlighted red
        clearRedTextFields()
    }

    /**
     * Function that clears the whole form
     */
    fun clearForm() {
        //clearing all TextFields
        classGradeOne.text.clear()
        classGradeTwo.text.clear()
        classGradeThree.text.clear()
        classGradeFour.text.clear()
        classGradeFive.text.clear()
        displayGPA.text = getString(R.string.gpa_result)

        //resetting EditText borders to normal
        classGradeOne.setBackgroundResource(android.R.drawable.edit_text)
        classGradeTwo.setBackgroundResource(android.R.drawable.edit_text)
        classGradeThree.setBackgroundResource(android.R.drawable.edit_text)
        classGradeFour.setBackgroundResource(android.R.drawable.edit_text)
        classGradeFive.setBackgroundResource(android.R.drawable.edit_text)

        //changing the button text to "Compute GPA"
        computeButton.setText(R.string.compute_gpa)

        //changing the background back to white
        currentBackgroundColor = resources.getColor(R.color.white)
        findViewById<View>(R.id.main).setBackgroundColor(currentBackgroundColor)
    }

    /**
     * clears any red highlightings in the textfields if there are any present
     */
    fun clearRedTextFields() {
        classGradeOne.setBackgroundResource(android.R.drawable.edit_text)
        classGradeTwo.setBackgroundResource(android.R.drawable.edit_text)
        classGradeThree.setBackgroundResource(android.R.drawable.edit_text)
        classGradeFour.setBackgroundResource(android.R.drawable.edit_text)
        classGradeFive.setBackgroundResource(android.R.drawable.edit_text)
    }

    /**
     * Sets up TextWatchers on all grade input fields to enable re-calculation
     * after a GPA has already been computed
     */
    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // If the button says "Clear Form", change it back to "Compute GPA"
                // so the user can calculate again without clearing
                if (computeButton.text.equals("Clear Form")) {
                    computeButton.setText(R.string.compute_gpa)
                }
            }
        }

        // Add the same TextWatcher to all grade fields
        classGradeOne.addTextChangedListener(textWatcher)
        classGradeTwo.addTextChangedListener(textWatcher)
        classGradeThree.addTextChangedListener(textWatcher)
        classGradeFour.addTextChangedListener(textWatcher)
        classGradeFive.addTextChangedListener(textWatcher)
    }

    /**
     * Validates that all provided EditText fields are not empty.
     * If any field is empty, it sets an error message on that field.
     *
     * @param fields Vararg parameter of EditText fields to validate.
     * @return True if all fields are filled, false if any field is empty.
     */
    fun validateGrade(vararg fields: EditText): Boolean {
        for(field in fields) {
            if(field.text.toString().isBlank()) {
                //highlights the empty field red and sets an error message
                field.setBackgroundColor(resources.getColor(android.R.color.holo_red_light))
                field.error = "${field.hint ?: "This field"} is required"
                return false
            }
            else {
                //removes any red highlighting if the user has filled in the field
                field.setBackgroundResource(android.R.drawable.edit_text)
            }
        }
        return true
    }
}