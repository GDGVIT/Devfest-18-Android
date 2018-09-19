package com.dscvit.android.devfest18.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.dscvit.android.devfest18.R
import com.dscvit.android.devfest18.ui.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.android.synthetic.main.fragment_bottom_sheet_navigation.*
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import com.google.android.gms.tasks.Task
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import android.content.Intent
import android.util.Log
import org.jetbrains.anko.imageResource
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.AuthCredential
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.toast


class NavigationBottomSheetFragment : RoundedBottomSheetDialogFragment() {

    private val RC_SIGN_IN = 9001

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    var navClickListener: NavClickListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_navigation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        context?.let { mGoogleSignInClient = GoogleSignIn.getClient(it, gso) }

//        mAuth = FirebaseAuth.getInstance()

        updateAccountUI(mAuth.currentUser)

        sign_in_button.setOnClickListener { signIn() }
        sign_out_button.setOnClickListener { signOut() }

        setupNavListViews()
        updateNavList(MainActivity.selectedFragmentIndex)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
//                Log.w(TAG, "Google sign in failed", e);
                updateAccountUI(null)
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
//        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
//                Log.d(TAG, "signInWithCredential:success")
                val user = mAuth.currentUser
                updateAccountUI(user)
            } else {
//                Log.w(TAG, "signInWithCredential:failure", task.exception)
                context?.toast("Login failed")
                updateAccountUI(null)
            }
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signOut() {
        mAuth.signOut()
        mGoogleSignInClient.signOut().addOnCompleteListener {
            updateAccountUI(null)
        }
    }

    private fun updateAccountUI(user: FirebaseUser?) {
        user?.let { user ->

            layout_nav_before_auth.visibility = View.GONE
            layout_nav_after_auth.visibility = View.VISIBLE

            Picasso.get().load(user.photoUrl).into(image_account)
            text_account_username.text = user.displayName
            text_account_email.text = user.email
        } ?: run {

            layout_nav_before_auth.visibility = View.VISIBLE
            layout_nav_after_auth.visibility = View.GONE
            image_account.imageResource = R.drawable.ic_account_circle_white_24dp
        }
    }

    private fun setupNavListViews() {
        layout_nav_main.setOnClickListener {
            navClickListener?.onNavItemClicked(0)
        }
        layout_nav_agenda.setOnClickListener {
            navClickListener?.onNavItemClicked(1)
        }
        layout_nav_scratch.setOnClickListener {
            navClickListener?.onNavItemClicked(2)
        }
        layout_nav_sponsors.setOnClickListener {
            navClickListener?.onNavItemClicked(3)
        }
        layout_nav_about.setOnClickListener {
            navClickListener?.onNavItemClicked(4)
        }
        resetBackgrounds()
    }

    private fun resetBackgrounds() {
        text_nav_main.setBackgroundResource(0)
        text_nav_agenda.setBackgroundResource(0)
        text_nav_scratch.setBackgroundResource(0)
        text_nav_sponsors.setBackgroundResource(0)
        text_nav_about.setBackgroundResource(0)
    }

    private fun updateNavList(index: Int) {
        val bgResourceId = R.drawable.bg_nav_sheet_selection
        when(index) {
            0 -> text_nav_main.backgroundResource = bgResourceId
            1 -> text_nav_agenda.backgroundResource = bgResourceId
            2 -> text_nav_scratch.backgroundResource = bgResourceId
            3 -> text_nav_sponsors.backgroundResource = bgResourceId
            4 -> text_nav_about.backgroundResource = bgResourceId
        }
    }
}

interface NavClickListener {
    fun onNavItemClicked(index: Int)
}
