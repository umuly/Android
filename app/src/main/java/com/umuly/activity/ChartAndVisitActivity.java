package com.umuly.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.umuly.R;
import com.umuly.adapters.StatusAdapter;
import com.umuly.models.SpinnerObject;
import com.umuly.models.request.UrlVisitRequestObject;
import com.umuly.models.response.ChartListResponseObject;
import com.umuly.models.response.ChartResponseObject;
import com.umuly.models.response.VisitUrlResponseObject;
import com.umuly.networkservice.ApiService;
import com.umuly.utils.ApiUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.umuly.constants.Constants.dateFormat;
import static com.umuly.constants.Constants.getToken;
import static com.umuly.constants.Constants.hideProgressBar;
import static com.umuly.constants.Constants.showProgressBar;


public class ChartAndVisitActivity extends AppCompatActivity {
    ApiService apiService;
    SharedPreferences sharedPref;
    Context context;
    LineChartView lineChartView;
    ProgressBar progressBar;
    String shortUrlName = "";
    String shortUrlId = "";
    TextView title1, title2, title3, title4, title5, title6, title7, title8, title9, title10;
    TextView date1, date2, date3, date4, date5, date6, date7, date8, date9, date10;
    ImageView filterButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_and_visit);
        shortUrlName = getIntent().getStringExtra("shortLinkName");
        shortUrlId = getIntent().getStringExtra("shortLinkId");
        context = ChartAndVisitActivity.this;
        apiService = ApiUtils.getAPIService();
        sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        startDateStr = getResources().getString(R.string.filter_start_date);
        endDateStr = getResources().getString(R.string.filter_end_date);
        selectSpinnerObject = new SpinnerObject("Day", "1");
        bindView();
    }

    private void bindView() {
        lineChartView = findViewById(R.id.chart_view);
        progressBar = findViewById(R.id.chart_progress_bar);
        filterButton = findViewById(R.id.chart_filter_icon);

        title1 = findViewById(R.id.visit_title_1);
        title2 = findViewById(R.id.visit_title_2);
        title3 = findViewById(R.id.visit_title_3);
        title4 = findViewById(R.id.visit_title_4);
        title5 = findViewById(R.id.visit_title_5);
        title6 = findViewById(R.id.visit_title_6);
        title7 = findViewById(R.id.visit_title_7);
        title8 = findViewById(R.id.visit_title_8);
        title9 = findViewById(R.id.visit_title_9);
        title10 = findViewById(R.id.visit_title_10);


        date1 = findViewById(R.id.visit_date_1);
        date2 = findViewById(R.id.visit_date_2);
        date3 = findViewById(R.id.visit_date_3);
        date4 = findViewById(R.id.visit_date_4);
        date5 = findViewById(R.id.visit_date_5);
        date6 = findViewById(R.id.visit_date_6);
        date7 = findViewById(R.id.visit_date_7);
        date8 = findViewById(R.id.visit_date_8);
        date9 = findViewById(R.id.visit_date_9);
        date10 = findViewById(R.id.visit_date_10);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });

        getVisitList();
        getChartData();
    }

    int year = 0;
    int month = 0;
    int dayOfMonth = 0;
    DatePickerDialog datePickerDialog;
    Calendar calendar;
    String dayStr = "";
    String monthStr = "";
    String requestStartDateStr = "";
    String requestEndDateStr = "";

    String startDateStr = "";
    String endDateStr = "";

    private void showFilterDialog() {
        final Dialog dialog = new Dialog(ChartAndVisitActivity.this, R.style.MyAlertDialogTheme);
        dialog.setContentView(R.layout.custom_chart_filter_dialog);
        dialog.setCancelable(false);
        final TextView startDate = dialog.findViewById(R.id.filter_start_date);
        final TextView endDate = dialog.findViewById(R.id.filter_end_date);
        TextView typeTitle = dialog.findViewById(R.id.filter_chart_type_title);
        Spinner typeSpinner = dialog.findViewById(R.id.filter_chart_type_spinner);
        Button positiveButton = dialog.findViewById(R.id.filter_chart_positive_button);
        Button negativeButton = dialog.findViewById(R.id.filter_chart_negative_button);
        setStatusAdapter(typeSpinner, typeTitle);

        startDate.setText(startDateStr);
        endDate.setText(endDateStr);

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestStartDateStr = startDate.getText().toString();
                requestEndDateStr = endDate.getText().toString();
                getChartData();
                dialog.dismiss();
            }
        });


        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(ChartAndVisitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                if (day < 10) {
                                    dayStr = "0" + day;
                                } else {
                                    dayStr = day + "";
                                }
                                month = month + 1;
                                if (month < 10) {
                                    monthStr = "0" + month;
                                } else {
                                    monthStr = month + "";
                                }
                                startDateStr = year + "-" + monthStr + "-" + dayStr;
                                startDate.setText(startDateStr);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(ChartAndVisitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                if (day < 10) {
                                    dayStr = "0" + day;
                                } else {
                                    dayStr = day + "";
                                }
                                month = month + 1;
                                if (month < 10) {
                                    monthStr = "0" + month;
                                } else {
                                    monthStr = month + "";
                                }
                                endDateStr = year + "-" + monthStr + "-" + dayStr;
                                endDate.setText(endDateStr);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
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

    int spinnerSelectPosition = 0;
    SpinnerObject selectSpinnerObject;

    private void setStatusAdapter(final Spinner spinner, final TextView title) {
        List<SpinnerObject> statusList = new ArrayList<>();
        statusList.add(new SpinnerObject("Day", "1"));
        statusList.add(new SpinnerObject("Month", "2"));
        statusList.add(new SpinnerObject("Year", "3"));

        StatusAdapter statusAdapter = new StatusAdapter(statusList, context);
        spinner.setAdapter(statusAdapter);
        spinner.setSelection(spinnerSelectPosition);


        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.performClick();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSelectPosition = position;
                selectSpinnerObject = (SpinnerObject) spinner.getItemAtPosition(position);
                title.setText(selectSpinnerObject.getTitle());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void getChartData() {
        HashMap<String, Object> chartRequestObject = new HashMap<>();
        chartRequestObject.put("Url", shortUrlName);
        if (!"".equalsIgnoreCase(requestStartDateStr) && !"Start Date".equalsIgnoreCase(requestStartDateStr)) {
            chartRequestObject.put("StartDateTime", requestStartDateStr);
        }
        if (!"".equalsIgnoreCase(requestEndDateStr) && !"End Date".equalsIgnoreCase(requestEndDateStr)) {
            chartRequestObject.put("EndDateTime", requestEndDateStr);
        }
        chartRequestObject.put("VisitType", Integer.parseInt(selectSpinnerObject.getId()));
        showProgressBar(ChartAndVisitActivity.this, progressBar);
        apiService.getChartData(getToken(context), chartRequestObject).enqueue(new Callback<ChartResponseObject>() {
            @Override
            public void onResponse(Call<ChartResponseObject> call, Response<ChartResponseObject> response) {
                hideProgressBar(ChartAndVisitActivity.this, progressBar);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getChartList() != null) {
                            if (response.body().getChartList().size() != 0) {
                                bindChartData(response.body().getChartList());
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ChartResponseObject> call, Throwable t) {
                hideProgressBar(ChartAndVisitActivity.this, progressBar);
            }
        });
    }

    private void getVisitList() {
        UrlVisitRequestObject urlVisitRequestObject = new UrlVisitRequestObject();
        urlVisitRequestObject.setUrlId(shortUrlId);
        apiService.getVisitList(getToken(ChartAndVisitActivity.this), urlVisitRequestObject).enqueue(new Callback<VisitUrlResponseObject>() {
            @Override
            public void onResponse(Call<VisitUrlResponseObject> call, Response<VisitUrlResponseObject> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getUrlVisitList() != null) {
                            if (response.body().getUrlVisitList().size() != 0) {
                                bindVisitData(response.body());
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<VisitUrlResponseObject> call, Throwable t) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void bindVisitData(VisitUrlResponseObject response) {
        title1.setText("1." + getResources().getString(R.string.visit_count_desc) +
                (response.getUrlVisitList().get(0).getLanguageCode() != null ? response.getUrlVisitList().get(0).getLanguageCode() : ""));
        date1.setText(dateFormat(response.getUrlVisitList().get(0).getDate()));

        if (response.getUrlVisitList().size() > 1) {
            title2.setText("2." + getResources().getString(R.string.visit_count_desc) +
                    (response.getUrlVisitList().get(1).getLanguageCode() != null ? response.getUrlVisitList().get(1).getLanguageCode() : ""));
            date2.setText(dateFormat(response.getUrlVisitList().get(1).getDate()));
        }

        if (response.getUrlVisitList().size() > 2) {
            title3.setText("3." + getResources().getString(R.string.visit_count_desc) +
                    (response.getUrlVisitList().get(2).getLanguageCode() != null ? response.getUrlVisitList().get(2).getLanguageCode() : ""));
            date3.setText(dateFormat(response.getUrlVisitList().get(2).getDate()));
        }

        if (response.getUrlVisitList().size() > 3) {
            title4.setText("4." + getResources().getString(R.string.visit_count_desc) +
                    (response.getUrlVisitList().get(3).getLanguageCode() != null ? response.getUrlVisitList().get(3).getLanguageCode() : ""));
            date4.setText(dateFormat(response.getUrlVisitList().get(3).getDate()));
        }

        if (response.getUrlVisitList().size() > 4) {
            title5.setText("5." + getResources().getString(R.string.visit_count_desc) +
                    (response.getUrlVisitList().get(4).getLanguageCode() != null ? response.getUrlVisitList().get(4).getLanguageCode() : ""));
            date5.setText(dateFormat(response.getUrlVisitList().get(4).getDate()));
        }
        if (response.getUrlVisitList().size() > 5) {
            title6.setText("6." + getResources().getString(R.string.visit_count_desc) +
                    (response.getUrlVisitList().get(5).getLanguageCode() != null ? response.getUrlVisitList().get(5).getLanguageCode() : ""));
            date6.setText(dateFormat(response.getUrlVisitList().get(5).getDate()));
        }
        if (response.getUrlVisitList().size() > 6) {
            title7.setText("7." + getResources().getString(R.string.visit_count_desc) +
                    (response.getUrlVisitList().get(6).getLanguageCode() != null ? response.getUrlVisitList().get(6).getLanguageCode() : ""));
            date7.setText(dateFormat(response.getUrlVisitList().get(6).getDate()));
        }
        if (response.getUrlVisitList().size() > 7) {
            title8.setText("8." + getResources().getString(R.string.visit_count_desc) +
                    (response.getUrlVisitList().get(7).getLanguageCode() != null ? response.getUrlVisitList().get(7).getLanguageCode() : ""));
            date8.setText(dateFormat(response.getUrlVisitList().get(7).getDate()));
        }
        if (response.getUrlVisitList().size() > 8) {
            title9.setText("9." + getResources().getString(R.string.visit_count_desc) +
                    (response.getUrlVisitList().get(8).getLanguageCode() != null ? response.getUrlVisitList().get(8).getLanguageCode() : ""));
            date9.setText(dateFormat(response.getUrlVisitList().get(8).getDate()));
        }
        if (response.getUrlVisitList().size() > 9) {
            title10.setText("10." + getResources().getString(R.string.visit_count_desc) +
                    (response.getUrlVisitList().get(9).getLanguageCode() != null ? response.getUrlVisitList().get(9).getLanguageCode() : ""));
            date10.setText(dateFormat(response.getUrlVisitList().get(9).getDate()));
        }
    }


    String chartDay, chartMonth, chartYear;

    private void bindChartData(List<ChartListResponseObject> chartList) {
        List<PointValue> yAxisValues = new ArrayList<>();
        List<AxisValue> axisValues = new ArrayList<>();
        int counterX = 0;
        for (int i = 0; i < chartList.size(); i++) {
            if (chartList.get(i).getDateObject().getYear() == null) {
                chartYear = "";
            } else {
                chartYear = "" + chartList.get(i).getDateObject().getYear();
            }

            if (chartList.get(i).getDateObject().getMonth() == null) {
                chartMonth = "";
            } else {
                chartMonth = "/" + chartList.get(i).getDateObject().getMonth();
            }

            if (chartList.get(i).getDateObject().getDay() == null) {
                chartDay = "";
            } else {
                chartDay = "/" + chartList.get(i).getDateObject().getDay();
            }
            axisValues.add(counterX, new AxisValue(counterX).setLabel(chartYear + chartMonth + chartDay));
            yAxisValues.add(new PointValue(counterX, chartList.get(i).getCount()));
            counterX++;


        }
        Line line = new Line(yAxisValues).setColor(getResources().getColor(R.color.blue));
        line.setHasLabels(true);
        List<Line> lines = new ArrayList<>();
        lines.add(line);


        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis axis = new Axis();
        axis.setValues(axisValues);
        axis.setTextSize(16);
        axis.setHasTiltedLabels(false);
        axis.setTextColor(getResources().getColor(R.color.gray));
        data.setAxisXBottom(axis);

        SimpleAxisValueFormatter formatter = new SimpleAxisValueFormatter();
        formatter.setDecimalDigitsNumber(0);
        Axis yAxis = new Axis();
        yAxis.setTextColor(getResources().getColor(R.color.gray));
        yAxis.setTextSize(16);
        yAxis.setFormatter(formatter);
        data.setAxisYLeft(yAxis);


        lineChartView.setViewportCalculationEnabled(true);
        lineChartView.setLineChartData(data);
        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
        int countMax = 0;
        for (ChartListResponseObject chartListResponseObject : chartList) {
            if (countMax < chartListResponseObject.getCount()) {
                countMax = chartListResponseObject.getCount();
            }
        }
        viewport.top = countMax + 1;
        viewport.bottom = 0;
        if (chartList.size() > 10) {
            viewport.right = 10;
        } else {
            viewport.right = chartList.size();
        }
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);


    }
}