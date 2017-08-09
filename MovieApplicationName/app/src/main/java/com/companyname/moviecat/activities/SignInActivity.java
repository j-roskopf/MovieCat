package com.companyname.moviecat.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.companyname.movieapplicationname.R;
import com.companyname.moviecat.dagger.components.AppComponent;
import com.companyname.moviecat.dagger.components.DaggerAppComponent;
import com.companyname.moviecat.dagger.modules.AppModule;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SignInActivity extends BaseActivity {

    @BindView(R.id.signInUsername)
    EditText signInUsername;

    @BindView(R.id.signInPassword)
    EditText signInPassword;

    @BindView(R.id.signInProgressBar)
    ProgressBar signInProgressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppComponent component = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        component.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        checkIfUserIsLoggedIn();

        FirebaseAnalytics.getInstance(this).setCurrentScreen(this, "SignInActivity", null /* class override */);

    }

    /**
     * Validate input so the user can be registers
     */
    private boolean validateInput() {
        boolean toReturn = true;
        String message = "";

        if (TextUtils.isEmpty(signInUsername.getText())) {
            toReturn = false;
            message = "Please enter a username";
        } else if (TextUtils.isEmpty(signInPassword.getText())) {
            toReturn = false;
            message = "Please enter a password";
        }

        if (!toReturn) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        return toReturn;
    }

    private void signInUser(){
        signInProgressBar.setVisibility(View.VISIBLE);

        String username = signInUsername.getText().toString();
        String password = signInPassword.getText().toString();

        //authenticate user
        FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        signInProgressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            // there was an error
                            Toast.makeText(SignInActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                        } else {
                            startMainActivity();
                        }
                    }
                });
    }

    /**
     * If the user is already logged in, take them to the main page
     */
    private void checkIfUserIsLoggedIn(){
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startMainActivity(); 
        }
    }

    @OnClick(R.id.signInRegisterButton)
    public void signInRegisterButtonClicked(){
        startActivityWithAnimation(this, RegisterActivity.class);
    }

    @OnClick(R.id.signInSignInButton)
    public void signInSignInButtonClicked(){
        if(validateInput()){
            signInUser();
        }
    }

    @OnClick(R.id.signInForgotPassword)
    public void signInForgotPasswordClicked(){
        startActivityWithAnimation(this, ForgotPasswordActivity.class);
    }
}
