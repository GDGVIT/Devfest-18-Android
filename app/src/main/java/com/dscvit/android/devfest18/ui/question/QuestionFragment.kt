package com.dscvit.android.devfest18.ui.question

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
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
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_questions.*
import kotlinx.android.synthetic.main.fragment_quiz.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.childrenSequence
import org.jetbrains.anko.toast
import java.util.*
import kotlin.collections.ArrayList

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
                        if (firebaseUser!!.email!! != question.userEmail) {
                            if (firebaseUser!!.uid in question.upVotedList) {
                                question.upvotes = question.upvotes - 1
                                question.upVotedList.remove(firebaseUser!!.uid)
                                questionRef.child("mainList").child(question.id).setValue(question)
                                context.toast("Downvoted")
                            } else {
                                question.upvotes = question.upvotes + 1
                                question.upVotedList.add(firebaseUser!!.uid)
                                questionRef.child("mainList").child(question.id).setValue(question)
                                context.toast("Upvoted")
                            }
                        } else {
                            context.toast("Can't upvote")
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

        hideAll()

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
                    filterSelectionIndex = indices
                    updateList()
                }
                .positiveButton(text = "Choose")
                .negativeButton(text = "Clear") {
                    filterSelectionIndex = null
                    updateList()
                }
                .show()
    }

    private fun input() {

//        MaterialDialog(requireContext())
//        .title(text = "New Question")
//        .input { dialog, text ->
//            var tempQuestion = Question(
//                    id = questionRef.child("mainList").push().key!!,
//                    upvotes = 0.toLong(),
//                    question = text.toString(),
//                    tag = "Harsh Dattani",
//                    upVotedList = arrayListOf(""),
//                    date = Date(),
//                    userName = firebaseUser?.displayName ?: "",
//                    userEmail = firebaseUser!!.email ?: ""
//            )
//            questionRef.child("mainList").child(tempQuestion.id).setValue(tempQuestion)
//            questionRef.child("backupList").child(tempQuestion.id).setValue(tempQuestion)
//
//        }
//        .positiveButton(text = "Add")
//        .show()

        MaterialDialog(requireContext())
                .title(text = "Enter a question")
                .customView(R.layout.layout_input_dialog, scrollable = true)
                .positiveButton(text = "Add") {
                    it.getCustomView()?.let { customView ->
                        val chipGroup = customView.findViewById<ChipGroup>(R.id.input_chip_group)
                        val input = customView.findViewById<EditText>(R.id.input_questionn)
//                        chipGroup.checkedChipId
//                        input.text
//                        context?.toast("${chipGroup.checkedChipId} - ${input.text}")
                        val inputText = input.text.toString()
                        if (inputText.isNullOrBlank() || inputText.isNullOrEmpty()) {
                            context?.toast("Question can't be empty")
                        } else {
                            var tempQuestion = Question(
                                    id = questionRef.child("mainList").push().key!!,
                                    upvotes = 0.toLong(),
                                    question = input.text.toString(),
                                    tag = when (chipGroup.checkedChipId) {
                                        R.id.chip1 -> requireContext().getString(R.string.speaker_0)
                                        R.id.chip2 -> requireContext().getString(R.string.speaker_1)
                                        R.id.chip3 -> requireContext().getString(R.string.speaker_2)
                                        R.id.chip4 -> requireContext().getString(R.string.speaker_3)
                                        R.id.chip5 -> requireContext().getString(R.string.speaker_4)
                                        R.id.chip6 -> requireContext().getString(R.string.speaker_5)
                                        R.id.chip7 -> requireContext().getString(R.string.speaker_6)
                                        R.id.chip8 -> requireContext().getString(R.string.speaker_7)
                                        R.id.chip9 -> requireContext().getString(R.string.speaker_8)
                                        else -> ""
                                    },
                                    upVotedList = arrayListOf(""),
                                    date = Date(),
                                    userName = firebaseUser?.displayName ?: "",
                                    userEmail = firebaseUser!!.email ?: ""
                            )
                            questionRef.child("mainList").child(tempQuestion.id).setValue(tempQuestion)
                            questionRef.child("backupList").child(tempQuestion.id).setValue(tempQuestion)
                        }

                    }
                }
                .negativeButton(text = "Cancel")
                .show()
    }

    private fun updateList() {

        var tempQuestions = questions

        filterSelectionIndex?.let { filterIndices ->

//            val tempTagList = arrayListOf<String>()
//            filterIndices.forEach {
//                tempTagList.add(speakersList!!.get(it))
//            }

            tempQuestions = ArrayList(questions!!.filter {
                filterIndices.contains(speakersList!!.indexOf(it!!.tag))
//                speakersList!!.indexOf(it!!.tag) == 0
            })

//            tempQuestions?.forEach {
//                it?.let {
//                    if (it.tag !in tempTagList) {
//                        tempQuestions.remove(it)
//                    }
//                }
//            }
        }

        if (tempQuestions?.size == 0) {
            text_empty_questions_placeholder?.show()
            rv_questions?.hide()
        } else {
            text_empty_questions_placeholder?.hide()
            rv_questions?.show()
        }

        when(sortSelectionIndex) {
            0 -> tempQuestions?.sortByDescending { it?.upvotes }
            1 -> tempQuestions?.sortByDescending { it?.date }
        }

        questionAdapter?.updateList(tempQuestions)
    }
}