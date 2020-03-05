package com.umuly.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.umuly.R;
import com.umuly.models.request.CreateUserRequestObject;
import com.umuly.models.response.CreateShortUrlResponseObject;
import com.umuly.networkservice.ApiService;
import com.umuly.utils.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.umuly.constants.Constants.hideProgressBar;
import static com.umuly.constants.Constants.showProgressBar;

public class RegisterUserActivity extends AppCompatActivity {

    EditText nameSurnameEdit, emailEdit, passwordEdit;
    String nameSurnameStr, emailStr, passwordStr;
    Button registerButton;
    ProgressBar progressBar;
    ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        apiService = ApiUtils.getAPIService();
        bindView();
    }


    private void bindView() {
        nameSurnameEdit = findViewById(R.id.name_surname_edittext);
        emailEdit = findViewById(R.id.email_edittext);
        passwordEdit = findViewById(R.id.password_edittext);
        registerButton = findViewById(R.id.register_button);
        progressBar = findViewById(R.id.register_progress_bar);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

    }

    private void createUser() {
        nameSurnameStr = nameSurnameEdit.getText().toString();
        emailStr = emailEdit.getText().toString();
        passwordStr = passwordEdit.getText().toString();

        if (!TextUtils.isEmpty(nameSurnameStr) && !TextUtils.isEmpty(emailStr) && !TextUtils.isEmpty(passwordStr)) {
            showProgressBar(RegisterUserActivity.this, progressBar);
            CreateUserRequestObject createUserRequestObject = new CreateUserRequestObject();
            createUserRequestObject.setName(nameSurnameStr);
            createUserRequestObject.setEmail(emailStr);
            createUserRequestObject.setPassword(passwordStr);
            apiService.createUser(createUserRequestObject).enqueue(new Callback<CreateShortUrlResponseObject>() {
                @Override
                public void onResponse(Call<CreateShortUrlResponseObject> call, Response<CreateShortUrlResponseObject> response) {
                    hideProgressBar(RegisterUserActivity.this, progressBar);
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                showDialog(getResources().getString(R.string.register_success_message), response.body().getStatus());
                            } else {
                                showDialog(response.body().getStatusText(), response.body().getStatus());
                            }

                        }
                    }
                }

                @Override
                public void onFailure(Call<CreateShortUrlResponseObject> call, Throwable t) {
                    hideProgressBar(RegisterUserActivity.this, progressBar);
                    Toast.makeText(RegisterUserActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void showDialog(String message, final Integer status) {
        new AlertDialog.Builder(RegisterUserActivity.this)
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
