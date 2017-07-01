package com.companyname.moviecat.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.companyname.movieapplicationname.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    /**
     * UI
     */
    @BindView(R.id.registerConfirmPasswordEditText)
    EditText registerConfirmPasswordEditText;

    @BindView(R.id.registerPasswordEditText)
    EditText registerPasswordEditText;

    @BindView(R.id.registerUsernameEditText)
    EditText registerUsernameEditText;

    @BindView(R.id.registerProgressBar)
    ProgressBar registerProgressBar;

    /**
     * NON UI
     */

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        initVars();
    }

    /**
     * Initialize required variables for this screen
     */
    private void initVars() {
        context = this;
    }

    /**
     * Validate input so the user can be registers
     */
    private boolean validateInput() {
        boolean toReturn = true;
        String message = "";

        if (TextUtils.isEmpty(registerUsernameEditText.getText())) {
            toReturn = false;
            message = "Please enter a username";
        } else if (TextUtils.isEmpty(registerPasswordEditText.getText())) {
            toReturn = false;
            message = "Please enter a password";
        } else if (!registerConfirmPasswordEditText.getText().toString().equals(
                registerPasswordEditText.getText().toString())
                ) {
            toReturn = false;
            message = "Passwords do not match";
        }

        if (!toReturn) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        return toReturn;
    }

    private void registerUser() {
        registerProgressBar.setVisibility(View.VISIBLE);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        String username = registerUsernameEditText.getText().toString();
        String password = registerPasswordEditText.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        registerProgressBar.setVisibility(View.GONE);
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(context, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            startMainActivity();
                        }
                    }
                });

    }

    @OnClick(R.id.registerRegisterButton)
    public void registerRegisterButtonClicked() {
        if ( validateInput() && googlePlayServicesUpToDate()){
            registerUser();
        }
    }

    @OnClick(R.id.registerGoToLogin)
    public void registerGoToLoginClicked(){
        startActivityWithAnimation(this, SignInActivity.class);
    }

    @OnClick(R.id.registerForgotPassword)
    public void registerForgotPasswordClicked(){
        startActivityWithAnimation(this, ForgotPasswordActivity.class);
    }
}
