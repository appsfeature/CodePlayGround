// IRemoteService.aidl
package com.droidhelios.playstoretesting;

import com.droidhelios.playstoretesting.RemoteCallback;

// Declare any non-default types here with import statements

interface IRemoteService {
    /** Request the process ID of this service, to do evil things with it. */
        int getPid();

        void setPid(in int pid);

        String getStatus();

        void openRequest(RemoteCallback callback);
}