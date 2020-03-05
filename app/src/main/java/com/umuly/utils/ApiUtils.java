package com.umuly.utils;

import com.umuly.networkservice.ApiService;
import com.umuly.networkservice.RetrofitClient;


public class ApiUtils {
    private ApiUtils() {
    }

    private static final String BASE_URL = "https://umuly.com/api/";

    public static ApiService getAPIService() {
        return RetrofitClient.getClient(BASE_URL).create(ApiService.class);
    }

}
