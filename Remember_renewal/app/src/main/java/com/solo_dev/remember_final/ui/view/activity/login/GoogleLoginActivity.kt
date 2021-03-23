package com.solo_dev.remember_final.ui.view.activity.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.solo_dev.remember_final.R
import com.solo_dev.remember_final.databinding.ActivityGoogleLoginBinding
import com.solo_dev.remember_final.ui.view.activity.main.MainActivity

class GoogleLoginActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var googleLoginBinding: ActivityGoogleLoginBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        googleLoginBinding = ActivityGoogleLoginBinding.inflate(layoutInflater)
        setContentView(googleLoginBinding!!.root)

        mAuth = FirebaseAuth.getInstance()
        if (mAuth!!.currentUser != null) {
            finish()
            val intent = Intent(application, MainActivity::class.java)
            startActivity(intent)
        }
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        googleLoginBinding!!.signInButton.setOnClickListener { v: View? -> signIn() }
    }

    // [START signin]
    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Log.e("Error, ", e.message)
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(acct!!.idToken, null)
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = mAuth!!.currentUser
                        Snackbar.make(window.decorView.rootView, "로그인 되었습니다, 환영합니다! ${user!!.displayName}님!", Snackbar.LENGTH_LONG).show()
                        updateUI(user)
                    } else {
                        Snackbar.make(window.decorView.rootView, "로그인에 실패했습니다. 기존 가입 계정은 문의를 넣어주시길 바랍니다.", Snackbar.LENGTH_LONG).show()
                        updateUI(null)
                    }
                }
    }

    override fun onBackPressed() {
        return
    }

    private fun updateUI(user: FirebaseUser?) { //update ui code here
        if (user != null) {
            finish()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}