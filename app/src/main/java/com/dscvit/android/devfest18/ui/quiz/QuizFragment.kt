package com.dscvit.android.devfest18.ui.quiz

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dscvit.android.devfest18.R
import com.dscvit.android.devfest18.model.Quiz
import com.dscvit.android.devfest18.model.QuizQuestion
import com.dscvit.android.devfest18.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_quiz.*
import org.jetbrains.anko.toast

class QuizFragment : Fragment() {

    private val QUIZ_TIMEOUT: Long = 10000
    private val QUIZ_INTERVAL: Long = 2000

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val quizRef = database.getReference("quiz")

    private val listener = object : ValueEventListener {

        override fun onCancelled(databaseError: DatabaseError) {

        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val quiz = dataSnapshot.getValue(Quiz::class.java)
            if (quiz != null && quiz.quizEnabled) {
                startQuiz(quiz)
            } else {
                hideQuiz()
            }
        }
    }

    var sharedPreferences: SharedPreferences? = null

    private var quiz = Quiz()
    private var clickedOptionPosition = -1
    private var questionIndex = 0
    private var isQuizCompleted = false

    private lateinit var textViewList: List<TextView>

    companion object {
        fun newInstance() = QuizFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = activity?.getSharedPreferences(Constants.PREF_KEY, Context.MODE_PRIVATE)

        textViewList = listOf(
                text_quiz_option_1,
                text_quiz_option_2,
                text_quiz_option_3,
                text_quiz_option_4
        )

//        hideQuiz()
        //TODO: Check user auth
        mAuth.addAuthStateListener {
            it.currentUser?.let {
                context?.toast(it.displayName.toString())
            } ?: run {

            }
        }

        isQuizCompleted = sharedPreferences?.getBoolean(Constants.PREF_QUIZ_COMPLETEED, false) ?: false

        if (isQuizCompleted) {
            //TODO show completed layout
            completeQuiz()
        } else {
            hideQuiz()
            quizRef.addValueEventListener(listener)
        }
    }

    private fun startQuiz(quiz: Quiz) {
        this.quiz = quiz
        showQuiz()
        startTimer()
    }

    private fun startTimer() {
        clickedOptionPosition = -1
        if (questionIndex < quiz.quizList.size) {
            updateQuestion(quiz.quizList[questionIndex])
            setClickListeners()
            Handler().postDelayed({
                removeClickListeners()
                stopTimer()
            }, QUIZ_TIMEOUT)
        } else {
            context?.toast("Quiz completed")
            //TODO: Show quiz completed page
            completeQuiz()
        }
    }

    private fun stopTimer() {
        showAnswer()
        Handler().postDelayed({
            questionIndex += 1
            resetOptions()
            startTimer()
        }, QUIZ_INTERVAL)
    }

    private fun getCurrentAnsIndex(): Int {
        val currentAnswer = quiz.quizList[questionIndex].answer
        return quiz.quizList[questionIndex].optionList.indexOf(currentAnswer)
    }

    private fun showAnswer() {
        val currAnsIndex = getCurrentAnsIndex()
        textViewList[currAnsIndex]?.setCorrectIndication()
        if (clickedOptionPosition != -1 && currAnsIndex != clickedOptionPosition) {
            textViewList[clickedOptionPosition]?.setWrongIndication()
        }
    }

    private fun removeClickListeners() {
        for (tv in textViewList) {
            tv?.setOnClickListener(null)
        }
    }

    private fun setClickListeners() {
        text_quiz_option_1?.setOnClickListener {
            resetOptions()
            clickedOptionPosition = 0
            text_quiz_option_1.setQuizOptionSelected()
        }
        text_quiz_option_2?.setOnClickListener {
            resetOptions()
            clickedOptionPosition = 1
            text_quiz_option_2.setQuizOptionSelected()
        }
        text_quiz_option_3?.setOnClickListener {
            resetOptions()
            clickedOptionPosition = 2
            text_quiz_option_3.setQuizOptionSelected()
        }
        text_quiz_option_4?.setOnClickListener {
            resetOptions()
            clickedOptionPosition = 3
            text_quiz_option_4.setQuizOptionSelected()
        }
    }

    private fun resetOptions() {
        for (tv in textViewList) {
            tv?.resetQuizOptionSelected()
        }
        clickedOptionPosition = -1
    }

    private fun showQuiz() {
        layout_quiz_placeholder?.hide()
        layout_quiz_completed?.hide()
        layout_quiz?.show()
        resetOptions()
    }

    private fun hideQuiz() {
        layout_quiz_placeholder?.show()
        layout_quiz?.hide()
        layout_quiz_completed?.hide()
    }

    private fun completeQuiz() {
        quizRef.removeEventListener(listener)
        layout_quiz_placeholder?.hide()
        layout_quiz?.hide()
        layout_quiz_completed?.show()
        sharedPreferences?.edit()?.putBoolean(Constants.PREF_QUIZ_COMPLETEED, true)?.apply()
    }

    private fun updateQuestion(quizQuestion: QuizQuestion) {
        text_quiz_question?.text = quizQuestion.question
        for (i in 0 until textViewList.size) {
            textViewList[i]?.text = quizQuestion.optionList[i]
        }
    }
}