package com.example.changeme.kitchenapp.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.changeme.kitchenapp.Model.Request;
import com.example.changeme.kitchenapp.Model.User;
import com.example.changeme.kitchenapp.Remote.APIService;
import com.example.changeme.kitchenapp.Remote.RetrofitClient;

/**
 * Created by SHEGZ on 12/30/2017.
 */
public class Common {
public static final String DELETE = "Delete";
    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Password";
    public static final String INTENT_FOOD_ID = "foodId";
    private static  final  String BASE_URL = "https://fcm.googleapis.com/";
public static String topic_name = "News";
    public  static Request currentRequest;
    public static User currentuser;

    public static APIService getFCMService()
    {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);

    }

    public static String convertCodeToStatus(String status) {
        if(status.equals("0"))
            return "Placed";
        else if(status.equals("1"))
            return "on my way";
        else
            return "Delivered";

    }

    public static boolean isConnectedToInternet(Context context){
        ConnectivityManager connectivityManager =  (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager!= null){
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if(info!=null){
                for(int i= 0;i<info.length; i++){
                    if(info[i].getState() == NetworkInfo.State.CONNECTED){
                        return  true;
                    }
                }
            }
        }
    return  false;
    }



}
