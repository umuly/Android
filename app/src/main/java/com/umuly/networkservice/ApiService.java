package com.umuly.networkservice;

import com.umuly.models.request.ChangePasswordRequestObject;
import com.umuly.models.request.ChartRequestObject;
import com.umuly.models.request.CreateShortUrlRequestObject;
import com.umuly.models.request.CreateUserRequestObject;
import com.umuly.models.request.ResetPassRequestObject;
import com.umuly.models.request.UrlVisitRequestObject;
import com.umuly.models.response.AllUrlResponseObject;
import com.umuly.models.response.ChartResponseObject;
import com.umuly.models.response.CreateShortUrlResponseObject;
import com.umuly.models.response.DomainResponseObject;
import com.umuly.models.response.LoginResponseObject;
import com.umuly.models.response.ResetPassResponseObject;
import com.umuly.models.response.VisitUrlResponseObject;


import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("Token")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Call<LoginResponseObject> loginMethod(@Query("Email") String email, @Query("Password") String password);

    @GET("domains")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Call<DomainResponseObject> getDomainList(@Header("Authorization") String token);

    @POST("url")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Call<CreateShortUrlResponseObject> createShortUrl(@Header("Authorization") String token, @Body CreateShortUrlRequestObject createShortUrlRequestObject);


    @GET("url")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Call<AllUrlResponseObject> getAllUrl(@Header("Authorization") String token, @Query("Skip") String skip, @Query(value = "Domain", encoded = true) String domain,
                                         @Query("Code") String code, @Query("RedirectUrl") String redirectUrl,
                                         @Query("Description") String description, @Query("ShortUrl") String shortUrl,
                                         @Query("Tags") String tags, @Query("Title") String title,
                                         @Query("Sort.Column") String sortColumn, @Query("Sort.Type") String sortType,
                                         @Query("Status") String status);


    @DELETE("url/{urlId}")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Call<AllUrlResponseObject> deleteUrl(@Header("Authorization") String token, @Path("urlId") String urlId);


    @POST("user")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Call<CreateShortUrlResponseObject> createUser(@Body CreateUserRequestObject createUserRequestObject);

    @POST("user/reset-password")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Call<ResetPassResponseObject> resetPass(@Body ResetPassRequestObject resetPassRequestObject);

    @POST("user/change-password")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Call<CreateShortUrlResponseObject> changePass(@Body ChangePasswordRequestObject changePasswordRequestObject);

    @POST("urlvisit/getChart")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Call<ChartResponseObject> getChartData(@Header("Authorization") String token, @Body HashMap<String, Object> anon);


    @POST("urlvisit")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Call<VisitUrlResponseObject> getVisitList(@Header("Authorization") String token, @Body UrlVisitRequestObject urlVisitRequestObject);


}
