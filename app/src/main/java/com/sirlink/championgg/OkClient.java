package com.sirlink.championgg;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * Created by compudep on 21/11/2017.
 */

public class OkClient {

    private static OkHttpClient client;

    public static OkHttpClient getHttp(){
        if( client == null){
           client = new OkHttpClient();
        }
        return client;
    }


}
