package com.hfad.geoquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders


private const val TAG = " MainActivity"
private const val KEY_INDEX = "index"
private const val KEY_SCORE = "score"

class MainActivity : AppCompatActivity() {

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this)[QuizViewModel::class.java]
    }

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var scoreTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            quizViewModel.currentIndex = 0
            quizViewModel.scoreIndex = 0
        } else {
            quizViewModel.currentIndex =
                savedInstanceState.getInt(KEY_INDEX, quizViewModel.currentIndex)
            quizViewModel.scoreIndex =
                savedInstanceState.getInt(KEY_SCORE, quizViewModel.scoreIndex)
        }

        initView()
        selectAnswer()
        checkQuestion()//след пред
        updateQuestion()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_INDEX, quizViewModel.currentIndex)
        outState.putInt(KEY_SCORE, quizViewModel.scoreIndex)
    }

    private fun initView() {
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        cheatButton = findViewById(R.id.cheat_button)
        questionTextView = findViewById(R.id.question_text_view)
        scoreTextView = findViewById(R.id.score_text_view)
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    //ответил правильно или нет + начисление очков,переход к след вопросу с отрисовкой очков на экране
    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        if (userAnswer == correctAnswer) {
            quizViewModel.score()
            scoreTextView.text =
                quizViewModel.scoreIndex.toString()//FIXME очки не сохраняются после отрисовки после поворота экрана
            Toast.makeText(
                this,
                "Угадали! ваши очки - ${quizViewModel.scoreIndex}",
                Toast.LENGTH_SHORT
            ).show()
            quizViewModel.moveToNext()
            updateQuestion()
        } else {
            Toast.makeText(
                this,
                "Ошиблись! ваши очки - ${quizViewModel.scoreIndex}",
                Toast.LENGTH_SHORT
            ).show()
            quizViewModel.moveToNext()
            updateQuestion()
        }
    }

    //след или предыдущий вопрос
    private fun checkQuestion() {
        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }
        prevButton.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()
        }
    }

    //выбор ответа пользователя
    private fun selectAnswer() {
        trueButton.setOnClickListener {
            checkAnswer(true)
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
        }
        cheatButton.setOnClickListener {
            // Начало CheatActivity
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            startActivity(intent)
        }
    }
}