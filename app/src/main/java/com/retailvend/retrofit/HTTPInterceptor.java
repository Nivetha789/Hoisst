package com.retailvend.retrofit;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class HTTPInterceptor {
    public static HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    public static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static OkHttpClient.Builder logInterceptor() {
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.connectTimeout(30, TimeUnit.SECONDS);
        httpClient.readTimeout(30, TimeUnit.SECONDS);
        httpClient.addInterceptor(logging);
        return httpClient;
    }

    public static OkHttpClient.Builder networkInterceptor() {
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addNetworkInterceptor(logging);
        return httpClient;
    }
}