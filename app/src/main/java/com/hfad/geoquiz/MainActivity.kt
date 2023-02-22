package com.hfad.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.TextView
import android.widget.Toast


private const val TAG = " MainActivity"

class MainActivity : AppCompatActivity() {


    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )
    private var currentIndex = 0
    private var scoreIndex = 0

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var scoreTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        initView()
        selectAnswer()
        checkQuestion()
        updateQuestion()
    }

    private fun initView() {
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)
        scoreTextView = findViewById(R.id.score_text_view)
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer
        var messageResId = if (userAnswer == correctAnswer) {
            score()
            Toast.makeText(this, "Угадали! ваши очки - ${scoreIndex}", Toast.LENGTH_SHORT).show()
                .let {
                    currentIndex = (currentIndex + 1) % questionBank.size
                    updateQuestion()
                }
        } else {
            Toast.makeText(this, "Ошиблись! ваши очки - ${scoreIndex}", Toast.LENGTH_SHORT).show()
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }
    }

    private fun score() {
        scoreIndex = scoreIndex + 1
        scoreTextView.setText(scoreIndex.toString())
    }

    private fun checkQuestion() {
        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }
        prevButton.setOnClickListener {
            currentIndex = (currentIndex - 1) % questionBank.size
            updateQuestion()
        }
    }

    private fun selectAnswer() {
        trueButton.setOnClickListener {
            checkAnswer(true)
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
        }
    }
}
//комит