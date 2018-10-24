package com.panasonic.in.workbench;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.panasonic.in.comm.Communicator;
import com.panasonic.in.comm.auth.LoginRequestBody;
import com.panasonic.in.comm.auth.User;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1001;
    private static final String TAG = "LoginActivity";

    private EditText mLoginEditText;
    private EditText mPasswordEditText;
    private SignInButton mSignInButton;
    private Communicator mCommunicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCommunicator = Communicator.getInstance();

        mLoginEditText = findViewById(R.id.loginText);
        mPasswordEditText = findViewById(R.id.passwordText);

        Button loginButton = findViewById(R.id.button);
        loginButton.setOnClickListener(new loginClickListener());

        configureGoogleSignIn();
    }

    public void onStart() {
        super.onStart();
        // Check for existing Google Sign In account,
        // if the user is already signed in the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (null != account) {
            updateUserInformation(account);
            launchDashboard();
        }
    }

    private void configureGoogleSignIn() {
        // Set the dimensions of the sign-in button.
        mSignInButton = findViewById(R.id.sign_in_button);
        mSignInButton.setSize(SignInButton.SIZE_STANDARD);
        mSignInButton.setOnClickListener(new GoogleSignInButtonClickListener());
    }

    public class loginClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Editable loginE = mLoginEditText.getText();
            Editable passE = mPasswordEditText.getText();

            mCommunicator.getAuthenticationService()
                    .login(new LoginRequestBody("nnayan", "nirajnan"))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<User>() {
                        @Override
                        public void accept(User user) throws Exception {
                            Log.d(LoginActivity.class.getName(), "Login Successful");
                            launchDashboard();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.d(LoginActivity.class.getName(), "Login failed with error", throwable);
                        }
                    });
        }
    }

    public class GoogleSignInButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent signInIntent = MockTestApplication.getInstance().getGoogleSignInClient().getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

    private void updateUserInformation(GoogleSignInAccount account) {

    }

    private void launchDashboard() {
        Intent dashboardActivity = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(dashboardActivity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            updateUserInformation(account);
            launchDashboard();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }
}
