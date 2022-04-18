package com.retailvend.retrofit;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClient {



    //live url
    public static final String LIVE_URL = "http://18.139.78.111/index.php/";

    //test url
    public static final String TEST_URL="http://18.139.78.111/beta/index.php/";


    private static RetrofitClient mInstance;
    private Retrofit retrofit;


    private RetrofitClient() {
//
//        OkHttpClient okHttpClient = new OkHttpClient();
//        OkHttpClient okHttpClientV1 = okHttpClient
//                .newBuilder()
//                .followRedirects(false)
//                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(TEST_URL)
                .client(HTTPInterceptor.logInterceptor().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static synchronized RetrofitClient getInstance() {

        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;

    }

    public Api getApi() {
        return retrofit.create(Api.class);
    }


}
