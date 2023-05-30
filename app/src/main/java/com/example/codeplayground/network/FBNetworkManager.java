package com.example.codeplayground.network;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import retrofit2.Response;

public class FBNetworkManager {

    private final FBApiConfig mRetrofit;
    private final Context context;


    public FBNetworkManager(Context context, String baseUrl) {
        this.context = context;
        this.mRetrofit = FBRetrofit.getClient(baseUrl).create(FBApiConfig.class);
    }


    public void testApi(FormResponse.Callback<FBNetworkModel> callback) {
        TaskRunner.getInstance().executeAsync(new Callable<FBNetworkModel>() {
            @Override
            public FBNetworkModel call() throws Exception {
                try {

                    Map<String, String> params = new HashMap<>();
//                    params.put("form_id", property.getFormId() + "");
                    Response<FBNetworkModel> response;
//                    response = mRetrofit.requestPost("creatething", params).execute();
                    response = mRetrofit.requestPostDataForm("creatething", params).execute();
                    if (response.body() != null) {
                        return response.body();
                    } else {
                        return new FBNetworkModel("FAILURE", response.message());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return new FBNetworkModel("FAILURE", e.getMessage());
                }
            }
        }, new TaskRunner.Callback<FBNetworkModel>() {
            @Override
            public void onComplete(FBNetworkModel finalData) {
                callback.onSuccess(finalData);
            }
        });
    }

}