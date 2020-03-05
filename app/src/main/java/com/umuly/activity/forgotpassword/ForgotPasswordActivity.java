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
import com.umuly.models.request.ResetPassRequestObject;
import com.umuly.models.response.ResetPassResponseObject;
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

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText emailEdit;
    String emailStr;
    Button sendCodeButton;
    ProgressBar progressBar;
    ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        apiService = ApiUtils.getAPIService();
        bindView();
    }

    private void bindView() {
        emailEdit = findViewById(R.id.email_edittext);
        sendCodeButton = findViewById(R.id.send_code_button);
        progressBar = findViewById(R.id.register_progress_bar);


        sendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode();
            }
        });

    }

    private void sendCode() {
        emailStr = emailEdit.getText().toString();
        if (!TextUtils.isEmpty(emailStr)) {
            showProgressBar(ForgotPasswordActivity.this, progressBar);
            ResetPassRequestObject resetPassRequestObject = new ResetPassRequestObject();
            resetPassRequestObject.setEmail(emailStr);
            apiService.resetPass(resetPassRequestObject).enqueue(new Callback<ResetPassResponseObject>() {
                @Override
                public void onResponse(Call<ResetPassResponseObject> call, Response<ResetPassResponseObject> response) {
                    hideProgressBar(ForgotPasswordActivity.this, progressBar);
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
                public void onFailure(Call<ResetPassResponseObject> call, Throwable t) {
                    hideProgressBar(ForgotPasswordActivity.this, progressBar);
                }
            });
        } else {
            showDialog(getResources().getString(R.string.reset_pass_error), 0);
        }

    }

    private void showDialog(String message, final Integer status) {
        new AlertDialog.Builder(ForgotPasswordActivity.this)
                .setMessage(message)
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (status == 200) {
                            finish();
                            startActivity(new Intent(ForgotPasswordActivity.this, ForgotPasswordCodeActivity.class).putExtra("email", emailStr));
                        }
                    }
                }).show();
    }
}
