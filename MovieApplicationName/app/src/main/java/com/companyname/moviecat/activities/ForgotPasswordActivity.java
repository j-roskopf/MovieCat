package com.companyname.moviecat.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.companyname.movieapplicationname.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPasswordActivity extends AppCompatActivity {

    @BindView(R.id.forgotPasswordEmail)
    EditText forgotPasswordEmail;

    @BindView(R.id.forgotPasswordProgress)
    ProgressBar forgotPasswordProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);

        FirebaseAnalytics.getInstance(this).setCurrentScreen(this, "ForgotPasswordActivity", null /* class override */);

    }

    /**
     * Validate input so the user can be registers
     */
    private boolean validateInput() {
        boolean toReturn = true;
        String message = "";

        if (TextUtils.isEmpty(forgotPasswordEmail.getText())) {
            toReturn = false;
            message = "Please enter an email";
        }

        if (!toReturn) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        return toReturn;
    }

    private void sendEmail(){
        forgotPasswordProgress.setVisibility(View.VISIBLE);

        String email = forgotPasswordEmail.getText().toString();

        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ForgotPasswordActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                        }

                        forgotPasswordProgress.setVisibility(View.GONE);
                    }
                });
    }

    @OnClick(R.id.forgotPasswordResetPassword)
    public void forgotPasswordResetPasswordClicked(){
        if(validateInput()){
            sendEmail();
        }
    }

    @OnClick(R.id.forgotPasswordBack)
    public void forgotPasswordBackClicked(){
        finish();
    }
}
