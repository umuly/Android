package com.umuly.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.umuly.R;
import com.umuly.activity.forgotpassword.ForgotPasswordActivity;
import com.umuly.activity.forgotpassword.ForgotPasswordCodeActivity;
import com.umuly.models.response.LoginResponseObject;
import com.umuly.networkservice.ApiService;
import com.umuly.utils.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.umuly.constants.Constants.SYSTEM_ALERT_WINDOW_PERMISSION;
import static com.umuly.constants.Constants.TOKEN_KEY;
import static com.umuly.constants.Constants.hideProgressBar;
import static com.umuly.constants.Constants.showProgressBar;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    Context context;
    EditText usernamaEdit, passwordEdit;
    String usernamaStr, passwordStr;
    Button loginButton, signUpButton, forgotPasswordButton;
    ProgressBar progressBar;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    GoogleApiClient googleApiClient;
    static final int code = 100;
    SignInButton btnGoogleSignIn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = LoginActivity.this;
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            askPermission();
        }*/
        sharedPref = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        bindView();
    }

    private void askPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);
        }

    }

    private void bindView() {
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);
        usernamaEdit = findViewById(R.id.username_edittext);
        passwordEdit = findViewById(R.id.password_edittext);
        loginButton = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.login_progress_bar);


        signUpButton = findViewById(R.id.sign_up_button);
        forgotPasswordButton = findViewById(R.id.forgot_password_button);

        //usernamaEdit.setText("tnc3rr@gmail.com");
        //passwordEdit.setText("11223344");

        loginButton.setOnClickListener(this);
        btnGoogleSignIn.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
        forgotPasswordButton.setOnClickListener(this);
        passwordEdit.setTransformationMethod(new PasswordTransformationMethod());

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();

    }

    private void googleSocialLogin() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, code);
    }


    ApiService apiService;
    Call<LoginResponseObject> loginCall;

    private void login(String username, String password) {
        apiService = ApiUtils.getAPIService();
        loginCall = apiService.loginMethod(username, password);
        loginCall.enqueue(new Callback<LoginResponseObject>() {
            @Override
            public void onResponse(Call<LoginResponseObject> call, Response<LoginResponseObject> response) {
                hideProgressBar(LoginActivity.this, progressBar);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        editor = sharedPref.edit();
                        editor.putString(TOKEN_KEY, response.body().getToken());
                        editor.apply();
                        startActivity(new Intent(context, HistoryUrlActivity.class));
                    }
                } else {
                    Toast.makeText(context, getResources().getString(R.string.login_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponseObject> call, Throwable t) {
                hideProgressBar(LoginActivity.this, progressBar);
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private Boolean checkRequiredArea(String username, String password) {
        return !TextUtils.isEmpty(username) || !TextUtils.isEmpty(password);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_button) {
            usernamaStr = usernamaEdit.getText().toString();
            passwordStr = passwordEdit.getText().toString();
            if (checkRequiredArea(usernamaStr, passwordStr)) {
                showProgressBar(LoginActivity.this, progressBar);
                login(usernamaStr, passwordStr);
            } else {
                Toast.makeText(context, getResources().getString(R.string.login_empty_value_error), Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.btnGoogleSignIn) {
            googleSocialLogin();
        } else if (v.getId() == R.id.sign_up_button) {
            startActivity(new Intent(LoginActivity.this, RegisterUserActivity.class));
        } else if (v.getId() == R.id.forgot_password_button) {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == code) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResultGoogleSignIn(result);
        }
    }

    String socialEmail, socialUserId;

    private void handleResultGoogleSignIn(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            socialEmail = account.getEmail();
            socialUserId = account.getId();
            usernamaEdit.setText(socialEmail);
            passwordEdit.setText(socialUserId);


        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
