package com.retailvend.retrofit;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClient {


//    private static final String BASE_URL = "http://103.120.179.221:3000/api/v1/";

    //live url
    public static final String LIVE_URL = "http://18.139.78.111/index.php/";

    //test url
    public static final String TEST_URL="http://18.139.78.111/v2/index.php/";


//    public static final String BASE_URL1 = "http://35.222.101.173/index.php/";
//    public static final String BASE_IMAGE = "http://35.239.71.99:3003/api/v1/reports/imageurlslits";

//    public static final String TEST_URL="https://www.datasense.in/demo/hoisst/index.php/";

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
