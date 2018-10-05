package com.dscvit.android.devfest18.ui.question

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.dscvit.android.devfest18.R
import com.dscvit.android.devfest18.model.Question
import com.dscvit.android.devfest18.ui.adapter.QuestionAdapter
import com.dscvit.android.devfest18.utils.hide
import com.dscvit.android.devfest18.utils.show
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_questions.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.toast
import java.util.*

class QuestionFragment : Fragment() {

    private val RC_SIGN_IN = 9001

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val database = FirebaseDatabase.getInstance()
    private val questionRef = database.getReference("questions")

    private var firebaseUser: FirebaseUser? = null
//    private var questionList: QuestionList? = QuestionList()
    private var questions: ArrayList<Question?>? = null
    private var speakersList: List<String>? = null

    private var questionAdapter: QuestionAdapter? = null

    var sortSelectionIndex = 0
    var filterSelectionIndex: IntArray? = null

    private val questionListener = object : ValueEventListener {

        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val isQuestionsEnabled = dataSnapshot.child("questionsEnabled").getValue(Boolean::class.java)
            isQuestionsEnabled?.let {
                if (isQuestionsEnabled) {
//                    questionList = it
                    var list = ArrayList<Question?>()
                    dataSnapshot.child("mainList").children.forEach {
                        list.add(it.getValue(Question()::class.java))
                    }
                    questions = list

                    showQuestions()
                    updateList()
                } else {
                    showPlaceHolder()
                }
            }
        }
    }

    private val authListener = FirebaseAuth.AuthStateListener {
        it.currentUser?.let {

            firebaseUser = it

            context?.let {
                with(rv_questions) {
                    layoutManager = LinearLayoutManager(it)
                    questionAdapter = QuestionAdapter(questions, firebaseUser!!.uid) { question ->
                        if (firebaseUser!!.email!! == question.userEmail) {

                        } else {
                            if (firebaseUser!!.uid in question.upVotedList) {
                                question.upvotes = question.upvotes - 1
                                question.upVotedList.remove(firebaseUser!!.uid)
                                questionRef.child("mainList").child(question.id).setValue(question)
                                context.toast("Downvoted")
                            } else {
                                question.upvotes = question.upvotes + 1
                                question.upVotedList.add(firebaseUser!!.uid)
                                questionRef.child("mainList").child(question.id).setValue(question)
                            }
                        }
                    }
                    adapter = questionAdapter!!
                }
            }

            questionRef.addValueEventListener(questionListener)
        } ?: run {
            showNotAuth()
        }
    }

    companion object {
        fun newInstance() = QuestionFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_questions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        questionRef.setValue(questionList)

        launch(UI) {
            speakersList = resources.getStringArray(R.array.speakers_list).asList()
        }

        showNotAuth()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        context?.let { mGoogleSignInClient = GoogleSignIn.getClient(it, gso) }

        mAuth.addAuthStateListener(authListener)

        question_sign_in_button.setOnClickListener { signIn() }

        button_question_sort.setOnClickListener { sort() }
        button_question_filter.setOnClickListener { filter() }

        activity?.fab_main_add?.setOnClickListener { input() }
    }

    private fun showNotAuth() {
        hideAll()
        layout_question_not_auth?.show()
    }

    private fun showQuestions() {
        hideAll()
        layout_question_list?.show()
        activity?.fab_main_add?.show()
    }

    private fun showPlaceHolder() {
        hideAll()
        layout_question_placeholder?.show()
    }

    private fun hideAll() {
        layout_question_list?.hide()
        layout_question_not_auth?.hide()
        activity?.fab_main_add?.hide()
        layout_question_placeholder?.hide()
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

    private fun sort() {
        MaterialDialog(requireContext())
                .title(text = "Sort Options")
                .listItemsSingleChoice(items = listOf("Most up-votes first", "Recent questions first"), initialSelection = sortSelectionIndex) { dialog, index, text ->
                    sortSelectionIndex = index
                    updateList()
                }
                .show()
    }

    private fun filter() {
        MaterialDialog(requireContext())
                .title(text = "Filter Options")
                .listItemsMultiChoice(items = speakersList, initialSelection = filterSelectionIndex ?: intArrayOf()) { dialog, indices, items ->
                    context?.toast("$indices")
                    filterSelectionIndex = indices
                }
                .positiveButton(text = "Choose")
                .negativeButton(text = "Clear") {
                    filterSelectionIndex = null
                }
                .show()
    }

    private fun input() {

        MaterialDialog(requireContext())
        .title(text = "New Question")
        .input { dialog, text ->
            var tempQuestion = Question(
                    id = questionRef.child("mainList").push().key!!,
                    upvotes = 0.toLong(),
                    question = text.toString(),
                    tag = "Harsh Dattani",
                    upVotedList = arrayListOf(""),
                    date = Date(),
                    userName = firebaseUser?.displayName ?: "",
                    userEmail = firebaseUser!!.email ?: ""
            )
//            questionList?.let {
////                it.backupList.add(tempQuestion)
////                it.mainList.add(tempQuestion)
////            }
////            questionRef.setValue(questionList)
            questionRef.child("mainList").child(tempQuestion.id).setValue(tempQuestion)
            questionRef.child("backupList").child(tempQuestion.id).setValue(tempQuestion)

//            FirebaseDatabase.getInstance().getReference("questionList").child("mainList").push().setValue(tempQuestion)
        }
        .positiveButton(text = "Add")
        .show()
    }

    private fun updateList() {

        when(sortSelectionIndex) {
            0 -> questions?.sortByDescending { it?.upvotes }
            1 -> questions?.sortByDescending { it?.date }
        }

        filterSelectionIndex?.let {  }

        questionAdapter?.updateList(questions)
    }
}