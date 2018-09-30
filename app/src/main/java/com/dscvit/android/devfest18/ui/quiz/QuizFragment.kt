package com.dscvit.android.devfest18.ui.quiz

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet_navigation.*
import kotlinx.android.synthetic.main.fragment_quiz.*
import org.jetbrains.anko.toast

class QuizFragment : Fragment() {

    private val QUIZ_TIMEOUT: Long = 10000
    private val QUIZ_INTERVAL: Long = 2000

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val quizRef = database.getReference("quiz")
    private var firebaseUser: FirebaseUser? = null
    private var userRef = database.getReference("users")

    private val quizListener = object : ValueEventListener {

        override fun onCancelled(databaseError: DatabaseError) {

        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val quiz = dataSnapshot.getValue(Quiz::class.java)
            quiz?.let {
                when(it.quizEnabled.toInt()) {
                    0 -> hideQuiz()
                    1 -> startQuiz(it)
                    2 -> completeQuiz()
                }
            }
        }
    }

    private val authListener = FirebaseAuth.AuthStateListener { authListener ->
        authListener.currentUser?.let {
            firebaseUser = it
            userRef.child(firebaseUser!!.uid).addValueEventListener(userListener)
        } ?: run {
            showNotAuth()
        }
    }

    private val userListener = object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.hasChild("score")) {
                //FIXME: This is causing error because if score is updated during quiz this ends the quiz
                val dbScore: Long = dataSnapshot.child("score").getValue(Long::class.java)!!
                score = dbScore.toInt()
                completeQuiz()
            } else {
                quizRef.addValueEventListener(quizListener)
            }
        }
    }

    private var sharedPreferences: SharedPreferences? = null

    private var quiz = Quiz()
    private var clickedOptionPosition = -1
    private var questionIndex = 0
    private var isQuizCompleted = false
    private var score = 0

    private lateinit var textViewList: List<TextView>

    companion object {
        fun newInstance() = QuizFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_quiz, container, false)

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

        isQuizCompleted = sharedPreferences?.getBoolean(Constants.PREF_QUIZ_COMPLETED, false) ?: false
        score = sharedPreferences?.getInt(Constants.PREF_QUIZ_SCORE, 0) ?: 0

        if (isQuizCompleted) {
            completeQuiz()
        } else {
//            hideQuiz()
            mAuth.addAuthStateListener(authListener)
        }
    }

    private fun showNotAuth() {
        context?.toast("Not authorised")
        hideAll()
        layout_quiz_not_auth?.show()
    }

    private fun startQuiz(quiz: Quiz) {
        userRef.child(firebaseUser!!.uid).removeEventListener(userListener)
        updateScore()
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
            completeQuiz()
        }
    }

    private fun stopTimer() {
        showAnswer()
        Handler().postDelayed({
            if (getCurrentAnsIndex() != clickedOptionPosition) {
                completeQuiz()
            } else {
                questionIndex += 1
                resetOptions()
                startTimer()
            }
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
        if (currAnsIndex == clickedOptionPosition) {
            score++
            updateScore()
        }
    }

    private fun removeClickListeners() {
        for (tv in textViewList) {
            tv?.setOnClickListener(null)
        }
    }

    private fun setClickListeners() {
//        text_quiz_option_1?.setOnClickListener {
//            resetOptions()
//            clickedOptionPosition = 0
//            text_quiz_option_1.setQuizOptionSelected()
//        }
//        text_quiz_option_2?.setOnClickListener {
//            resetOptions()
//            clickedOptionPosition = 1
//            text_quiz_option_2.setQuizOptionSelected()
//        }
//        text_quiz_option_3?.setOnClickListener {
//            resetOptions()
//            clickedOptionPosition = 2
//            text_quiz_option_3.setQuizOptionSelected()
//        }
//        text_quiz_option_4?.setOnClickListener {
//            resetOptions()
//            clickedOptionPosition = 3
//            text_quiz_option_4.setQuizOptionSelected()
//        }

        for (i in 0 until textViewList.size) {
            textViewList[i].setOnClickListener {
                resetOptions()
                clickedOptionPosition = i
                textViewList[i].setQuizOptionSelected()
            }
        }
    }

    private fun resetOptions() {
        for (tv in textViewList) {
            tv?.resetQuizOptionSelected()
        }
        clickedOptionPosition = -1
    }

    private fun showQuiz() {
        hideAll()
        layout_quiz?.show()
        resetOptions()
    }

    private fun hideQuiz() {
        hideAll()
        layout_quiz_placeholder?.show()
    }

    private fun completeQuiz() {
        quizRef.removeEventListener(quizListener)
        mAuth.removeAuthStateListener(authListener)
        hideAll()
        sharedPreferences?.edit()?.putBoolean(Constants.PREF_QUIZ_COMPLETED, true)?.apply()
        quiz.quizList = listOf()
        layout_quiz_completed?.show()
        text_quiz_score_final?.text = "You scored $score"
    }

    private fun hideAll() {
        layout_quiz_placeholder?.hide()
        layout_quiz?.hide()
        layout_quiz_completed?.hide()
        layout_quiz_not_auth?.hide()
    }

    private fun updateScore() {
        //TODO: implement this to update view also
        text_quiz_score_live?.text = "SCORE $score"
        sharedPreferences?.edit()?.putInt(Constants.PREF_QUIZ_SCORE, score)?.apply()
        userRef.child(firebaseUser?.uid ?: "").child("score").setValue(score)
    }

    private fun updateQuestion(quizQuestion: QuizQuestion) {
        text_quiz_question?.text = quizQuestion.question
        for (i in 0 until textViewList.size) {
            textViewList[i]?.text = quizQuestion.optionList[i]
        }
    }
}