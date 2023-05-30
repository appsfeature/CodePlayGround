package com.example.codeplayground.network;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Amit on 3/28/2018.
 */

public class FBRetrofit {

    private static final String defaultBaseUrl = "http://appsfeature.com/";

    public static Retrofit getClient(String baseUrl) {
        return getClient("https://qbzhxhwy3l.execute-api.ap-south-1.amazonaws.com/prod/","", null);
    }

    @Nullable
    public static Retrofit getClient(String baseUrl, String securityCode, String securityCodeEnc) {
        if(baseUrl == null){
            baseUrl = defaultBaseUrl;
        }
        Retrofit retrofit = null;
        try {
            if(!baseUrl.endsWith("/")){
                baseUrl += "/";
            }
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            OkHttpClient.Builder client = getHttpClient(securityCode, securityCodeEnc, true);
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client.build())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retrofit;
    }

    private static OkHttpClient.Builder getHttpClient(final String securityCode, final String securityCodeEnc, boolean isDebug) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        Request request;

                        request = original.newBuilder()
//                                .header("Authorization", securityCode)
                                .header("x-api-key", "sRE3WnbhIU7hVvDlT4cGE3Hb8p1jZ7uk4C4YDiLi")
                                .header("ThingName", "bonushub199")
                                .method(original.method(), original.body())
                                .build();

                        return chain.proceed(request);
                    }
                });
        builder.readTimeout(60, TimeUnit.SECONDS);
        builder.connectTimeout(60, TimeUnit.SECONDS);

        if (isDebug) {
            builder.addInterceptor(loggingInterceptor);
        }
        return builder;
    }

    private static final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

}
