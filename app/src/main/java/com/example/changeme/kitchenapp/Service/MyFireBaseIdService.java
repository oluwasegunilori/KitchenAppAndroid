package com.example.changeme.kitchenapp.Service;

import com.example.changeme.kitchenapp.Model.Token;
import com.example.changeme.kitchenapp.common.Common;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by SHEGZ on 1/4/2018.
 */
public class MyFireBaseIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {

        super.onTokenRefresh();
    String tokenrefreshed = FirebaseInstanceId.getInstance().getToken();
if(Common.currentuser!= null)
        updateTokenToFirebase(tokenrefreshed);

    }

    private void updateTokenToFirebase(String tokenrefreshed) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference("Tokens");
        Token token = new Token(tokenrefreshed, false);
        tokens.child(Common.currentuser.getPhone()).setValue(token);



    }
}
