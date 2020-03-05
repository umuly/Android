package com.umuly.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.umuly.R;
import com.umuly.adapters.DomainAdapter;
import com.umuly.adapters.LongUrlSpinnerAdapter;
import com.umuly.models.EditUrlObject;
import com.umuly.models.request.CreateShortUrlRequestObject;
import com.umuly.models.response.CreateShortUrlResponseObject;
import com.umuly.models.response.DomainListResponseObject;
import com.umuly.models.response.DomainResponseObject;
import com.umuly.networkservice.ApiService;
import com.umuly.utils.ApiUtils;
import com.umuly.utils.CopyListener;
import com.umuly.utils.CustomEdittext;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.umuly.constants.Constants.getToken;
import static com.umuly.constants.Constants.hideProgressBar;
import static com.umuly.constants.Constants.showProgressBar;

public class CreateLinkActivity extends AppCompatActivity implements View.OnClickListener {
    Context context;
    TextView toolbarTitle;
    Typeface helvaticaFont;
    CustomEdittext longUrlEdittext;
    EditText shortUrlEdittext, titleEdittext, descEdittext, tagsEdittext;
    Spinner longUrlSpinner, shortUrlSpinner;
    TextView longUrlTitle, shortUrlTitle;
    RelativeLayout RLLongUrl, RLShortUrl;
    ApiService apiService;
    LinearLayout LLOptionalArea;
    TextView showHideOptinalArea;
    TextView shortLinkShow;
    String shortLinkDomainList = "";
    String shortLinkDomainText = "";
    String longUrlStr = "";
    String shortLinkDomainStr = "";
    String titleStr = "";
    String descStr = "";
    String tagsStr = "";
    String domainId = "";
    ProgressBar progressBar;
    Button saveButton;
    SharedPreferences sharedPref;
    EditUrlObject editUrlObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = CreateLinkActivity.this;
        sharedPref = context.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        editUrlObject = (EditUrlObject) getIntent().getSerializableExtra("editUrl");
        helvaticaFont = Typeface.createFromAsset(getAssets(), "font/HelveticaMedium.ttf");
        apiService = ApiUtils.getAPIService();
        bindView();

    }


    private void bindView() {
        toolbarTitle = findViewById(R.id.main_toolbar_text);
        toolbarTitle.setTypeface(helvaticaFont);

        longUrlEdittext = findViewById(R.id.long_url_edittext);
        shortUrlEdittext = findViewById(R.id.short_url_edittext);
        titleEdittext = findViewById(R.id.title_edittext);
        descEdittext = findViewById(R.id.desc_edittext);
        tagsEdittext = findViewById(R.id.tags_edittext);

        longUrlSpinner = findViewById(R.id.long_url_spinner);
        shortUrlSpinner = findViewById(R.id.short_url_spinner);

        RLLongUrl = findViewById(R.id.rl_long_url);
        RLShortUrl = findViewById(R.id.rl_short_url);

        longUrlTitle = findViewById(R.id.long_url_title);
        shortUrlTitle = findViewById(R.id.short_url_title);

        LLOptionalArea = findViewById(R.id.ll_optional_area);

        shortLinkShow = findViewById(R.id.show_short_link);

        progressBar = findViewById(R.id.main_progress_bar);

        saveButton = findViewById(R.id.save_button);


        showHideOptinalArea = findViewById(R.id.show_hide_optional_area);
        showHideOptinalArea.setPaintFlags(showHideOptinalArea.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        RLLongUrl.setOnClickListener(this);
        RLShortUrl.setOnClickListener(this);
        showHideOptinalArea.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        setLongUrlSpinnerData();
        getDomainList();

        longUrlEdittext.addListener(new CopyListener() {
            @Override
            public void onUpdate() {
                String s = longUrlEdittext.getText().toString();
                if (s.contains("http://")) {
                    s = s.replace("http://", "");
                }
                if (s.contains("https://")) {
                    s = s.replace("http://", "");
                }
                longUrlEdittext.setText(s);
            }
        });


        shortUrlEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                shortLinkDomainText = s.toString();
                shortLinkShow.setText(shortLinkDomainList + "/" + shortLinkDomainText);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    List<DomainListResponseObject> domainList;
    Call<DomainResponseObject> domainsResponseObjectCall;

    private void getDomainList() {
        showProgressBar(CreateLinkActivity.this, progressBar);
        domainsResponseObjectCall = apiService.getDomainList(getToken(CreateLinkActivity.this));
        domainsResponseObjectCall.enqueue(new Callback<DomainResponseObject>() {
            @Override
            public void onResponse(Call<DomainResponseObject> call, Response<DomainResponseObject> response) {
                hideProgressBar(CreateLinkActivity.this, progressBar);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus() == 200) {
                            domainList = response.body().getDomainList();
                            setDomainAdapter(domainList);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<DomainResponseObject> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                hideProgressBar(CreateLinkActivity.this, progressBar);
            }
        });

    }

    private void setDomainAdapter(final List<DomainListResponseObject> list) {
        DomainAdapter domainAdapter = new DomainAdapter(list, context);
        shortUrlSpinner.setAdapter(domainAdapter);


        if (editUrlObject != null) {
            if (!"".equalsIgnoreCase(editUrlObject.getDesc()) || !"".equalsIgnoreCase(editUrlObject.getTitle()) || !"".equalsIgnoreCase(editUrlObject.getTags())) {
                isShow = true;
                LLOptionalArea.setVisibility(View.VISIBLE);
                showHideOptinalArea.setText(getResources().getString(R.string.hide_area_title));
            } else {
                isShow = false;
                LLOptionalArea.setVisibility(View.GONE);
                showHideOptinalArea.setText(getResources().getString(R.string.show_area_title));
            }

            longUrlEdittext.setText(editUrlObject.getRedirectUrl());
            shortUrlEdittext.setText(editUrlObject.getDomain());
            titleEdittext.setText(editUrlObject.getTitle());
            descEdittext.setText(editUrlObject.getDesc());
            tagsEdittext.setText(editUrlObject.getTags());


            if ("http://".equalsIgnoreCase(editUrlObject.getScheme())) {
                longUrlSpinner.setSelection(0);
            } else if ("https://".equalsIgnoreCase(editUrlObject.getScheme())) {
                longUrlSpinner.setSelection(1);
            } else {
                longUrlSpinner.setSelection(2);
            }


            for (int i = 0; i < list.size(); i++) {
                if (editUrlObject.getDomainId().equalsIgnoreCase(list.get(i).getId())) {
                    shortUrlSpinner.setSelection(i);
                }
            }


        }

        shortUrlSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                domainListResponseObject = list.get(position);
                shortLinkDomainList = list.get(position).getDomainUrl();
                shortUrlData(shortLinkDomainList);
                shortLinkShow.setText(shortLinkDomainList + "/" + shortLinkDomainText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void setLongUrlSpinnerData() {
        final List<String> list = new ArrayList<>();
        list.add("http://");
        list.add("https://");
        list.add("Other Scheme");
        LongUrlSpinnerAdapter longUrlSpinnerAdapter = new LongUrlSpinnerAdapter(list, context);
        longUrlSpinner.setAdapter(longUrlSpinnerAdapter);


        longUrlSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 2) {
                    longUrlData("");
                } else {
                    longUrlData(list.get(position));
                }
                longUrlTitle.setText(list.get(position));


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void longUrlData(final String data) {
        longUrlEdittext.setText(data);
        longUrlEdittext.setSelection(data.length());
    }

    private void shortUrlData(final String data) {
        shortUrlTitle.setText(data);

    }

    Boolean isShow = false;
    DomainListResponseObject domainListResponseObject;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_long_url:
                longUrlSpinner.performClick();
                break;
            case R.id.rl_short_url:
                shortUrlSpinner.performClick();
                break;
            case R.id.show_hide_optional_area:
                if (!isShow) {
                    isShow = true;
                    LLOptionalArea.setVisibility(View.VISIBLE);
                    showHideOptinalArea.setText(getResources().getString(R.string.hide_area_title));
                } else {
                    isShow = false;
                    LLOptionalArea.setVisibility(View.GONE);
                    showHideOptinalArea.setText(getResources().getString(R.string.show_area_title));
                }
                break;
            case R.id.save_button:
                longUrlStr = longUrlEdittext.getText().toString();
                shortLinkDomainStr = shortUrlEdittext.getText().toString();
                titleStr = titleEdittext.getText().toString();
                descStr = descEdittext.getText().toString();
                tagsStr = tagsEdittext.getText().toString();
                domainId = domainListResponseObject.getId();
                if (URLUtil.isValidUrl(longUrlStr) && !"".equalsIgnoreCase(longUrlStr) && !"http://".equalsIgnoreCase(longUrlStr) && !"https://".equalsIgnoreCase(longUrlStr)) {
                    saveUrl();
                } else {
                    Toast.makeText(CreateLinkActivity.this, getResources().getString(R.string.url_error_message), Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    Call<CreateShortUrlResponseObject> callCreateShortUrl;
    CreateShortUrlRequestObject createShortUrlRequestObject;

    private void saveUrl() {
        showProgressBar(CreateLinkActivity.this, progressBar);
        createShortUrlRequestObject = new CreateShortUrlRequestObject();
        createShortUrlRequestObject.setDescription(descStr);
        createShortUrlRequestObject.setCode(shortLinkDomainStr);
        createShortUrlRequestObject.setDomainId(domainId);
        createShortUrlRequestObject.setRedirectUrl(longUrlStr);
        createShortUrlRequestObject.setTags(tagsStr);
        createShortUrlRequestObject.setTitle(titleStr);
        callCreateShortUrl = apiService.createShortUrl(getToken(CreateLinkActivity.this), createShortUrlRequestObject);
        callCreateShortUrl.enqueue(new Callback<CreateShortUrlResponseObject>() {
            @Override
            public void onResponse(Call<CreateShortUrlResponseObject> call, Response<CreateShortUrlResponseObject> response) {
                hideProgressBar(CreateLinkActivity.this, progressBar);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus() == 200) {
                            Toast.makeText(CreateLinkActivity.this, response.body().getStatusText(), Toast.LENGTH_SHORT).show();
                            if (response.body().getShortUrl() != null) {
                                if (!"".equalsIgnoreCase(response.body().getShortUrl().getShortUrl())) {
                                    showCustomAlertDialog(response.body().getShortUrl().getShortUrl());
                                }
                            }
                        } else {
                            if (response.body().getStatusText() != null) {
                                if (!"".equalsIgnoreCase(response.body().getStatusText())) {
                                    Toast.makeText(CreateLinkActivity.this, response.body().getStatusText(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(CreateLinkActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(CreateLinkActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(CreateLinkActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CreateLinkActivity.this, getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<CreateShortUrlResponseObject> call, Throwable t) {
                Toast.makeText(CreateLinkActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                hideProgressBar(CreateLinkActivity.this, progressBar);
            }
        });
    }

    public void showCustomAlertDialog(String shortLinkStr) {
        final Dialog dialog = new Dialog(CreateLinkActivity.this, R.style.MyAlertDialogTheme);
        dialog.setContentView(R.layout.custom_alert_dialog);
        dialog.setCancelable(false);

        final TextView shortLink = dialog.findViewById(R.id.dialog_short_link);
        Button copyButton = dialog.findViewById(R.id.copy_link_button);
        Button closeButton = dialog.findViewById(R.id.close_button);

        shortLink.setText(shortLinkStr);

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("copyText", shortLink.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(CreateLinkActivity.this, getResources().getString(R.string.link_copy_button), Toast.LENGTH_LONG).show();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
