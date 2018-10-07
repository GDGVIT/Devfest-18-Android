package com.dscvit.android.devfest18.ui.quiz

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_quiz.*
import org.jetbrains.anko.toast
import android.net.ConnectivityManager
import kotlinx.android.synthetic.main.fragment_questions.*


class QuizFragment : Fragment() {

    private val RC_SIGN_IN = 9001

    private val QUIZ_TIMEOUT: Long = 10000
    private val QUIZ_INTERVAL: Long = 2000

    //TODO: remove "test"
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val database = FirebaseDatabase.getInstance()
    private val quizRef = database.getReference("testQuiz")
    private var firebaseUser: FirebaseUser? = null
    private var userRef = database.getReference("testQuizUsers")

    private var sharedPreferences: SharedPreferences? = null

    private var quiz = Quiz()
    private var clickedOptionPosition = -1
    private var questionIndex = 0
    private var score = 0
    private var mCountDownTimer: CountDownTimer? = null

    private var isQuizRunning = false
    private var isQuizCompleted = false

    private lateinit var textViewList: List<TextView>

    private val quizListener = object : ValueEventListener {

        override fun onCancelled(databaseError: DatabaseError) {

        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val quiz = dataSnapshot.getValue(Quiz::class.java)
            quiz?.let {
                when(it.quizEnabled.toInt()) {
                    0 -> hideQuiz()
                    1 -> {
                        if (isQuizCompleted) {
                            completeQuiz()
                        } else {
                            startQuiz(it)
                        }
                    }
                    2 -> completeQuiz()
                }
            }
        }
    }

    private val authListener = FirebaseAuth.AuthStateListener { authListener ->
        authListener.currentUser?.let {
            firebaseUser = it
            initalise()
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
//                isCompletedQuiz = true
            } else {
                userRef.child(firebaseUser!!.uid).child("score").setValue(0)
                userRef.child(firebaseUser!!.uid).child("userName").setValue(firebaseUser!!.displayName)
                userRef.child(firebaseUser!!.uid).child("email").setValue(firebaseUser!!.email)
                score = 0
            }
            quizRef.addValueEventListener(quizListener)
        }
    }

    companion object {
        fun newInstance() = QuizFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_quiz, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layout_quiz_loader?.show()

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

//        //TODO: toggle '&& false'
//        if (isQuizCompleted) {
//            completeQuiz()
//        } else {
////            hideQuiz()
//            mAuth.addAuthStateListener(authListener)
//
//            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                    .requestIdToken(getString(R.string.default_web_client_id))
//                    .requestEmail()
//                    .build()
//
//            context?.let { mGoogleSignInClient = GoogleSignIn.getClient(it, gso) }
//
//            quiz_sign_in_button.setOnClickListener { signIn() }
//        }

        mAuth.addAuthStateListener(authListener)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        context?.let { mGoogleSignInClient = GoogleSignIn.getClient(it, gso) }

        quiz_sign_in_button.setOnClickListener { signIn() }

        button_quiz_retry?.setOnClickListener { initalise() }
    }

    private fun initalise() {
        if (isOnline()) {
            userRef.child(firebaseUser!!.uid).addValueEventListener(userListener)
        } else {
            showNoInternet()
        }
    }

    private fun showNotAuth() {
//        context?.toast("Not authorised")
        hideAll()
        layout_quiz_not_auth?.show()
    }

    private fun startQuiz(quiz: Quiz) {
        if (!isQuizRunning) {
            isQuizRunning = true
//                userRef.child(firebaseUser!!.uid).removeEventListener(userListener)
            score = 0
            updateScore()
            this.quiz = quiz
            showQuiz()
            startTimer()
        }
    }

    private fun startTimer() {
        clickedOptionPosition = -1
        if (questionIndex < quiz.quizList.size) {

            updateQuestion(quiz.quizList[questionIndex])
            setClickListeners()

//            Handler().postDelayed({
//                progersssbar_quiz_timer.progress = progersssbar_quiz_timer.max
//                removeClickListeners()
//                stopTimer()
//            }, QUIZ_TIMEOUT)

            progersssbar_quiz_timer?.progress = 0

            mCountDownTimer = object : CountDownTimer(QUIZ_TIMEOUT + 1000, 50) {

                override fun onTick(millisUntilFinished: Long) {
                    progersssbar_quiz_timer?.incrementProgressBy(1)
                }

                override fun onFinish() {
                    progersssbar_quiz_timer?.progress = progersssbar_quiz_timer.max
                    removeClickListeners()
                    stopTimer()
                }
            }

            mCountDownTimer?.start()
        } else {
//            context?.toast("Quiz completed")
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
//        textViewList[currAnsIndex]?.setCorrectIndication()
        if (clickedOptionPosition != -1 && currAnsIndex != clickedOptionPosition) {
            textViewList[clickedOptionPosition]?.setWrongIndication()
        } else if (currAnsIndex == clickedOptionPosition) {
            textViewList[clickedOptionPosition]?.setCorrectIndication()
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
        for (i in 0 until textViewList.size) {
            textViewList[i]?.setOnClickListener {
                resetOptions()
                clickedOptionPosition = i
                textViewList[i]?.setQuizOptionSelected()
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

    private fun showNoInternet() {
        hideAll()
        layout_quiz_nointernet?.show()
    }

    private fun hideQuiz() {
        hideAll()
        layout_quiz_placeholder?.show()
        score = 0
        updateScore()
        sharedPreferences?.edit()?.putBoolean(Constants.PREF_QUIZ_COMPLETED, false)?.apply()
        isQuizCompleted = false
    }

    private fun completeQuiz() {
        isQuizRunning = false
//        quizRef.removeEventListener(quizListener)
//        mAuth.removeAuthStateListener(authListener)
        hideAll()
        sharedPreferences?.edit()?.putBoolean(Constants.PREF_QUIZ_COMPLETED, true)?.apply()
        isQuizCompleted = true
//        quiz.quizList = listOf()
        layout_quiz_completed?.show()
        if (score < 0) {
            text_quiz_score_final?.hide()
        } else {
            text_quiz_score_final?.show()
            text_quiz_score_final?.text = "You scored  $score"
        }
    }

    private fun hideAll() {
        layout_quiz_placeholder?.hide()
        layout_quiz?.hide()
        layout_quiz_completed?.hide()
        layout_quiz_not_auth?.hide()
        layout_quiz_nointernet?.hide()
        layout_quiz_loader?.hide()
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

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                context?.toast("Google sign in failed")
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                firebaseUser = mAuth.currentUser
            } else {
                context?.toast("Login failed")
            }
        }
    }

    private fun isOnline(): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val netInfo = cm?.activeNetworkInfo
        return netInfo?.isConnected ?: false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (isQuizRunning) {
            completeQuiz()
            mCountDownTimer?.cancel()
            context?.toast("Quiz completed")
        }
        removeListeners()
    }

    private fun removeListeners() {
        userRef.child(firebaseUser!!.uid).removeEventListener(userListener)
        quizRef.removeEventListener(quizListener)
        mAuth.removeAuthStateListener(authListener)
    }
}