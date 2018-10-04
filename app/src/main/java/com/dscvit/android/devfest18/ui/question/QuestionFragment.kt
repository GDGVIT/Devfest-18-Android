package com.dscvit.android.devfest18.ui.question

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dscvit.android.devfest18.R
import com.dscvit.android.devfest18.model.Question
import com.dscvit.android.devfest18.model.QuestionList
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
import kotlinx.android.synthetic.main.fragment_questions.*
import org.jetbrains.anko.toast

class QuestionFragment : Fragment() {

    private val RC_SIGN_IN = 9001

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val database = FirebaseDatabase.getInstance()
    private val questionRef = database.getReference("questions")

    private var firebaseUser: FirebaseUser? = null
    private var questionList: QuestionList? = QuestionList()

    private var questionAdapter: QuestionAdapter? = null

    companion object {
        fun newInstance() = QuestionFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_questions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showNotAuth()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        context?.let { mGoogleSignInClient = GoogleSignIn.getClient(it, gso) }

        mAuth.addAuthStateListener {
            it.currentUser?.let {

                firebaseUser = it
                showQuestions()

                context?.let {
                    with(rv_questions) {
                        layoutManager = LinearLayoutManager(it)
                        questionAdapter = QuestionAdapter(questionList!!.mainList, firebaseUser!!.uid)
                        adapter = questionAdapter!!
                    }
                }

                questionRef.addValueEventListener(object : ValueEventListener {

                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        questionList = dataSnapshot.getValue(QuestionList::class.java)
                        questionAdapter?.updateList(questionList?.mainList ?: arrayListOf())
                    }
                })
            } ?: run {
                showNotAuth()
            }
        }

        question_sign_in_button.setOnClickListener { signIn() }
    }

    private fun showNotAuth() {
        hideAll()
        layout_question_not_auth?.show()
    }

    private fun showQuestions() {
        hideAll()
        layout_question_list?.show()
    }

    private fun hideAll() {
        layout_question_list?.hide()
        layout_question_not_auth?.hide()
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

    }

    private fun filter() {

    }
}