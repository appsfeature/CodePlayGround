// RemoteCallback.aidl
package com.droidhelios.playstoretesting;


interface RemoteCallback {

    void onSuccess(String result);
    void onFailure(int errorCode);
}