package com.example.gpa_r02021117_calculator

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {

    // Theme management constants
    private companion object {
        const val PREFS_NAME = "theme_prefs"
        const val KEY_THEME = "selected_theme"
        const val THEME_LIGHT = "light"
        const val THEME_DARK = "dark"
    }

    private lateinit var course1: EditText
    private lateinit var course2: EditText
    private lateinit var course3: EditText
    private lateinit var course4: EditText
    private lateinit var course5: EditText
    private lateinit var btnCompute: Button
    private lateinit var btnToggleTheme: Button
    private lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        // Apply theme before setContentView
        applyTheme()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        course1 = findViewById(R.id.course1)
        course2 = findViewById(R.id.course2)
        course3 = findViewById(R.id.course3)
        course4 = findViewById(R.id.course4)
        course5 = findViewById(R.id.course5)
        btnCompute = findViewById(R.id.btnCompute)
        btnToggleTheme = findViewById(R.id.btnToggleTheme)
        tvResult = findViewById(R.id.tvResult)

        // Set up compute button
        btnCompute.setOnClickListener {
            computeGPA()
        }

        // Set up theme toggle button
        btnToggleTheme.setOnClickListener {
            toggleTheme()
            // Recreate activity to apply new theme
            recreate()
        }
    }

    /**
     * Apply the saved theme from SharedPreferences
     */
    private fun applyTheme() {

    }

    /**
     * Toggle between light and dark theme
     */
    private fun toggleTheme() {
        val prefs = getThemePreferences()
        val currentTheme = prefs.getString(KEY_THEME, THEME_LIGHT)

        val newTheme = if (currentTheme == THEME_LIGHT) THEME_DARK else THEME_LIGHT

        prefs.edit().putString(KEY_THEME, newTheme).apply()

        when (newTheme) {
            THEME_DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    /**
     * Check if dark theme is currently active
     */
    private fun isDarkTheme(): Boolean {
        val prefs = getThemePreferences()
        return prefs.getString(KEY_THEME, THEME_LIGHT) == THEME_DARK
    }

    /**
     * Get SharedPreferences instance for theme
     */
    private fun getThemePreferences(): SharedPreferences {
        return getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // ==================== GPA CALCULATION FUNCTIONS ====================

    private fun computeGPA() {
        val courses = listOf(course1, course2, course3, course4, course5)
        var totalGrade = 0.0
        var validCount = 0
        var hasError = false

        // Reset backgrounds
        courses.forEach { it.setBackgroundColor(Color.TRANSPARENT) }

        // Validate and calculate
        for (course in courses) {
            val gradeText = course.text.toString().trim()

            if (gradeText.isEmpty()) {
                course.setBackgroundColor(Color.parseColor("#FFCDD2"))
                hasError = true
                continue
            }

            try {
                val grade = gradeText.toDouble()

                if (grade < 0 || grade > 100) {
                    course.setBackgroundColor(Color.parseColor("#FFCDD2"))
                    hasError = true
                } else {
                    totalGrade += grade
                    validCount++
                }
            } catch (e: NumberFormatException) {
                course.setBackgroundColor(Color.parseColor("#FFCDD2"))
                hasError = true
            }
        }

        if (hasError) {
            Toast.makeText(this, "Please enter valid grades (0-100)", Toast.LENGTH_SHORT).show()
            return
        }

        val gpa = totalGrade / validCount
        tvResult.text = String.format("GPA: %.2f", gpa)

        // Update result color based on GPA
        when {
            gpa < 60 -> {
                tvResult.setBackgroundColor(Color.RED)
                tvResult.setTextColor(Color.WHITE)
            }
            gpa in 60.0..79.0 -> {
                tvResult.setBackgroundColor(Color.YELLOW)
                tvResult.setTextColor(Color.BLACK)
            }
            else -> {
                tvResult.setBackgroundColor(Color.GREEN)
                tvResult.setTextColor(Color.WHITE)
            }
        }
    }
}