package com.example.changeme.kitchenapp.Remote;

import com.example.changeme.kitchenapp.Model.DataMessage;
import com.example.changeme.kitchenapp.Model.MyResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by SHEGZ on 1/4/2018.
 */
public interface APIService {

    @Headers (
        {

            "Content-Type:application/json",
            "Authorization:key = AAAAVU-UPCw:APA91bE7XgiZyIzJWgNAEqn0VXmIhoWBxfNWdm_0Ke-IbkFqHV6qFhLRMZ0lbkx5uOs2MUZHLwdGnhWtDbkU6mUUpoJeFW3k9sutS0IB_W0tDTT8YjfoZnUcDYUVphvWKqcZw7-JyvHS"
        }

    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body DataMessage body);

}
