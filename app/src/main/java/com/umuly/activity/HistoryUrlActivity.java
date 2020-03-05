package com.umuly.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.umuly.R;
import com.umuly.adapters.DomainAdapter;
import com.umuly.adapters.HistoryUrlAdapter;
import com.umuly.adapters.SortAdapter;
import com.umuly.adapters.StatusAdapter;
import com.umuly.constants.Constants;
import com.umuly.models.EditUrlObject;
import com.umuly.models.SpinnerObject;
import com.umuly.models.response.AllUrlListResponseObject;
import com.umuly.models.response.AllUrlResponseObject;
import com.umuly.models.response.DomainListResponseObject;
import com.umuly.models.response.DomainResponseObject;
import com.umuly.networkservice.ApiService;
import com.umuly.utils.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.umuly.constants.Constants.TOKEN_KEY;
import static com.umuly.constants.Constants.getToken;
import static com.umuly.constants.Constants.hideProgressBar;
import static com.umuly.constants.Constants.showProgressBar;


public class HistoryUrlActivity extends AppCompatActivity implements View.OnClickListener {
    ListView historyListView;
    ApiService apiService;
    ProgressBar historyProgressBar;
    Context context;
    SharedPreferences sharedPref;
    LinearLayout LLFilterButton;
    FloatingActionButton createButton;
    LinearLayout LLNoRecords;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        context = HistoryUrlActivity.this;
        sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        apiService = ApiUtils.getAPIService();

    }

    @Override
    protected void onResume() {
        super.onResume();
        bindView();
    }

    private void bindView() {
        historyListView = findViewById(R.id.history_url_listview);
        historyProgressBar = findViewById(R.id.history_progress_bar);
        LLFilterButton = findViewById(R.id.ll_history_filter_button);
        createButton = findViewById(R.id.history_floating_button);
        LLNoRecords = findViewById(R.id.ll_no_records);

        LLFilterButton.setOnClickListener(this);
        createButton.setOnClickListener(this);

        getUrlList(domainStr, codeStr, redirectUrlStr, descStr, shortUrlStr, tagsStr, titleStr, sortStr, statusStr);
    }

    private void getUrlList(String domain, String code, String redirectUrl, String desc, String shortUrl, String tags, String title, String sortColumn, String status) {
        showProgressBar(HistoryUrlActivity.this, historyProgressBar);
        apiService.getAllUrl(Constants.getToken(HistoryUrlActivity.this), "0", domain, code, redirectUrl, desc, shortUrl, tags, title, sortColumn, "1", status)
                .enqueue(new Callback<AllUrlResponseObject>() {
                    @Override
                    public void onResponse(Call<AllUrlResponseObject> call, Response<AllUrlResponseObject> response) {
                        hideProgressBar(HistoryUrlActivity.this, historyProgressBar);
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getUrlList().size() != 0) {
                                    LLNoRecords.setVisibility(View.GONE);
                                    historyListView.setVisibility(View.VISIBLE);
                                    setAdapter(response.body().getUrlList());
                                } else {
                                    LLNoRecords.setVisibility(View.VISIBLE);
                                    historyListView.setVisibility(View.GONE);
                                }

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AllUrlResponseObject> call, Throwable t) {
                        hideProgressBar(HistoryUrlActivity.this, historyProgressBar);
                        Toast.makeText(HistoryUrlActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void setAdapter(List<AllUrlListResponseObject> urlList) {
        HistoryUrlAdapter historyUrlAdapter = new HistoryUrlAdapter(HistoryUrlActivity.this, urlList);
        historyListView.setAdapter(historyUrlAdapter);


        historyListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteEditDialog(((AllUrlListResponseObject) historyListView.getItemAtPosition(position)));

                return false;
            }
        });
    }

    SharedPreferences.Editor editor;

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(HistoryUrlActivity.this)
                .setMessage(getResources().getString(R.string.exit_dialog_message))
                .setPositiveButton(getResources().getString(R.string.exit_dialog_positive), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor = sharedPref.edit();
                        editor.remove(TOKEN_KEY);
                        editor.apply();
                        finish();
                        startActivity(new Intent(HistoryUrlActivity.this, LoginActivity.class));
                    }
                }).setNegativeButton(getResources().getString(R.string.exit_dialog_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    List<DomainListResponseObject> domainList;
    Call<DomainResponseObject> domainsResponseObjectCall;

    private void getDomainList() {
        showProgressBar(HistoryUrlActivity.this, historyProgressBar);
        domainsResponseObjectCall = apiService.getDomainList(getToken(HistoryUrlActivity.this));
        domainsResponseObjectCall.enqueue(new Callback<DomainResponseObject>() {
            @Override
            public void onResponse(Call<DomainResponseObject> call, Response<DomainResponseObject> response) {
                hideProgressBar(HistoryUrlActivity.this, historyProgressBar);
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
                hideProgressBar(HistoryUrlActivity.this, historyProgressBar);
            }
        });

    }

    int domainSpinnerPosition = 0;

    private void setDomainAdapter(final List<DomainListResponseObject> list) {
        List<DomainListResponseObject> newList = new ArrayList<>();
        DomainListResponseObject domainListResponseObject = new DomainListResponseObject();
        domainListResponseObject.setDomainUrl("All");
        newList.add(domainListResponseObject);
        newList.addAll(list);
        DomainAdapter domainAdapter = new DomainAdapter(newList, context);
        domainSpinner.setAdapter(domainAdapter);
        domainSpinner.setSelection(domainSpinnerPosition);


        filterDomainSpinnerTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                domainSpinner.performClick();
            }
        });

        domainSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                domainSpinnerPosition = position;
                DomainListResponseObject domainListResponseObject = (DomainListResponseObject) domainSpinner.getItemAtPosition(position);
                filterDomainSpinnerTitle.setText("Domain:" + domainListResponseObject.getDomainUrl());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    SpinnerObject statusSpinnerObject;
    int statusSpinnerPosition = 0;

    private void setStatusAdapter() {
        List<SpinnerObject> statusList = new ArrayList<>();
        statusList.add(new SpinnerObject("All", ""));
        statusList.add(new SpinnerObject("Error", "-4"));
        statusList.add(new SpinnerObject("Passive", "-3"));
        statusList.add(new SpinnerObject("Waiting for Delete", "-2"));
        statusList.add(new SpinnerObject("Abuse", "-1"));
        statusList.add(new SpinnerObject("Active", "1"));
        StatusAdapter statusAdapter = new StatusAdapter(statusList, context);
        statusSpinner.setAdapter(statusAdapter);
        statusSpinner.setSelection(statusSpinnerPosition);


        filterStatusSpinnerTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusSpinner.performClick();
            }
        });

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statusSpinnerPosition = position;
                statusSpinnerObject = (SpinnerObject) statusSpinner.getItemAtPosition(position);
                filterStatusSpinnerTitle.setText("Status:" + statusSpinnerObject.getTitle());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    int sortSpinnerPosition = 0;

    private void setSortAdapter() {
        List<String> sortList = new ArrayList<>();
        sortList.add("Created On");
        sortList.add("Visit Count");
        sortList.add("Last Visit Date");
        sortList.add("Title");
        sortList.add("Description");
        sortList.add("Tags");
        sortList.add("Code");
        sortList.add("Redirect Url");
        sortList.add("Short Url");
        SortAdapter sortAdapter = new SortAdapter(sortList, context);
        sortSpinner.setAdapter(sortAdapter);
        sortSpinner.setSelection(sortSpinnerPosition);


        filterSortSpinnerTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortSpinner.performClick();
            }
        });

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortSpinnerPosition = position;
                String title = (String) sortSpinner.getItemAtPosition(position);
                filterSortSpinnerTitle.setText("Sort:" + title);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void showDeleteEditDialog(final AllUrlListResponseObject allUrlListResponseObject) {
        final Dialog dialog = new Dialog(HistoryUrlActivity.this, R.style.MyAlertDialogTheme);
        dialog.setContentView(R.layout.custom_edit_delete_dialog);
        dialog.setCancelable(false);

        final Button editButton = dialog.findViewById(R.id.edit_button);
        Button deleteButton = dialog.findViewById(R.id.delete_button);
        Button cancelButton = dialog.findViewById(R.id.cancel_button);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                EditUrlObject editUrlObject = new EditUrlObject();
                editUrlObject.setDesc(allUrlListResponseObject.getDescription());
                editUrlObject.setTags(allUrlListResponseObject.getTags());
                editUrlObject.setTitle(allUrlListResponseObject.getTitle());
                editUrlObject.setShortUrl(allUrlListResponseObject.getShortUrl());
                editUrlObject.setDomain(allUrlListResponseObject.getCode());
                editUrlObject.setDomainId(allUrlListResponseObject.getDomainID());
                editUrlObject.setScheme(allUrlListResponseObject.getScheme());
                editUrlObject.setRedirectUrl(allUrlListResponseObject.getRedirectUrl());
                startActivity(new Intent(HistoryUrlActivity.this, CreateLinkActivity.class).putExtra("editUrl", editUrlObject));

            }
        });


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                apiService.deleteUrl(getToken(HistoryUrlActivity.this), allUrlListResponseObject.getId()).enqueue(new Callback<AllUrlResponseObject>() {
                    @Override
                    public void onResponse(Call<AllUrlResponseObject> call, Response<AllUrlResponseObject> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getStatus() == 200) {
                                    getUrlList(domainStr, codeStr, redirectUrlStr, descStr, shortUrlStr, tagsStr, titleStr, sortStr, statusStr);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AllUrlResponseObject> call, Throwable t) {

                    }
                });
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }


    Spinner domainSpinner, statusSpinner, sortSpinner;
    TextView filterDomainSpinnerTitle, filterStatusSpinnerTitle, filterSortSpinnerTitle;
    Button positiveButton, negativeButton;
    EditText codeEdit, redirectUrlEdit, shortUrlEdit, descEdit, tagsEdit, titleEdit;
    String statusStr = "";
    String domainStr = "";
    String codeStr = "";
    String redirectUrlStr = "";
    String shortUrlStr = "";
    String descStr = "";
    String tagsStr = "";
    String titleStr = "";
    String sortStr = "CreatedOn";

    private void showFilterDialog() {
        final Dialog dialog = new Dialog(HistoryUrlActivity.this, R.style.MyAlertDialogTheme);
        dialog.setContentView(R.layout.custom_filter_dialog);
        dialog.setCancelable(false);

        domainSpinner = dialog.findViewById(R.id.filter_domain_spinner);
        filterDomainSpinnerTitle = dialog.findViewById(R.id.filter_domain_spinner_title);

        statusSpinner = dialog.findViewById(R.id.filter_status_spinner);
        filterStatusSpinnerTitle = dialog.findViewById(R.id.filter_status_spinner_title);

        sortSpinner = dialog.findViewById(R.id.filter_sort_spinner);
        filterSortSpinnerTitle = dialog.findViewById(R.id.filter_sort_spinner_title);

        positiveButton = dialog.findViewById(R.id.filter_positive_button);
        negativeButton = dialog.findViewById(R.id.filter_negative_button);

        codeEdit = dialog.findViewById(R.id.code_edit);
        redirectUrlEdit = dialog.findViewById(R.id.redirect_url_edit);
        shortUrlEdit = dialog.findViewById(R.id.short_url_edit);
        descEdit = dialog.findViewById(R.id.desc_edit);
        tagsEdit = dialog.findViewById(R.id.tags_edit);
        titleEdit = dialog.findViewById(R.id.title_edit);


        codeEdit.setText(codeStr);
        redirectUrlEdit.setText(redirectUrlStr);
        shortUrlEdit.setText(shortUrlStr);
        descEdit.setText(descStr);
        tagsEdit.setText(tagsStr);
        titleEdit.setText(titleStr);

        getDomainList();
        setStatusAdapter();
        setSortAdapter();

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeStr = codeEdit.getText().toString();
                redirectUrlStr = redirectUrlEdit.getText().toString();
                shortUrlStr = shortUrlEdit.getText().toString();
                descStr = descEdit.getText().toString();
                tagsStr = tagsEdit.getText().toString();
                titleStr = titleEdit.getText().toString();
                statusStr = statusSpinnerObject.getId();
                if (filterDomainSpinnerTitle.getText().toString().contains("All")) {
                    domainStr = "";
                } else {
                    domainStr = filterDomainSpinnerTitle.getText().toString().replace("Domain:", "");
                }
                sortStr = filterSortSpinnerTitle.getText().toString().replace("Sort:", "").replace(" ", "");
                getUrlList(domainStr, codeStr, redirectUrlStr, descStr, shortUrlStr, tagsStr, titleStr, sortStr, statusStr);
                dialog.dismiss();
            }
        });

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_history_filter_button:
                showFilterDialog();
                break;
            case R.id.history_floating_button:
                startActivity(new Intent(HistoryUrlActivity.this, CreateLinkActivity.class));
                break;
        }
    }
}
