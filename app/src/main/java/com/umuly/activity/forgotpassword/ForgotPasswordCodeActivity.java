package com.umuly.activity.forgotpassword;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.umuly.R;
import com.umuly.models.request.ChangePasswordRequestObject;
import com.umuly.models.response.CreateShortUrlResponseObject;
import com.umuly.networkservice.ApiService;
import com.umuly.utils.ApiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.umuly.constants.Constants.hideProgressBar;
import static com.umuly.constants.Constants.showProgressBar;

public class ForgotPasswordCodeActivity extends AppCompatActivity {
    ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_code);
        apiService = ApiUtils.getAPIService();
        emailStr = getIntent().getStringExtra("email");
        bindView();
    }

    EditText codeEdit, passEdit;
    Button changePassButton;
    String codeStr, passStr, emailStr;
    ProgressBar progressBar;

    private void bindView() {
        codeEdit = findViewById(R.id.code_edittext);
        passEdit = findViewById(R.id.password_edittext);
        changePassButton = findViewById(R.id.change_pass_button);
        progressBar = findViewById(R.id.code_progress_bar);

        changePassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePass();
            }
        });
    }

    private void changePass() {
        codeStr = codeEdit.getText().toString();
        passStr = passEdit.getText().toString();
        if (!TextUtils.isEmpty(codeStr) || !TextUtils.isEmpty(passStr)) {
            showProgressBar(ForgotPasswordCodeActivity.this, progressBar);
            ChangePasswordRequestObject changePasswordRequestObject = new ChangePasswordRequestObject();
            changePasswordRequestObject.setCode(codeStr);
            changePasswordRequestObject.setEmail(emailStr);
            changePasswordRequestObject.setPassword(passStr);
            apiService.changePass(changePasswordRequestObject).enqueue(new Callback<CreateShortUrlResponseObject>() {
                @Override
                public void onResponse(Call<CreateShortUrlResponseObject> call, Response<CreateShortUrlResponseObject> response) {
                    hideProgressBar(ForgotPasswordCodeActivity.this, progressBar);
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            showDialog(response.body().getStatusText(), response.body().getStatus());
                        }
                    } else {
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            showDialog(jsonObject.getString("statusText"), 0);
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CreateShortUrlResponseObject> call, Throwable t) {
                    hideProgressBar(ForgotPasswordCodeActivity.this, progressBar);
                }
            });
        } else {
            showDialog(getResources().getString(R.string.error_message), 0);
        }

    }

    private void showDialog(String message, final Integer status) {
        new AlertDialog.Builder(ForgotPasswordCodeActivity.this)
                .setMessage(message)
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (status == 200) {
                            finish();
                        }
                    }
                }).show();
    }
}
