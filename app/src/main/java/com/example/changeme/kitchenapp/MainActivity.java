package com.example.changeme.kitchenapp;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.changeme.kitchenapp.Model.User;
import com.example.changeme.kitchenapp.common.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.paperdb.Paper;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements Animation.AnimationListener {
    Button butSignIn, butSignUp;
    ImageView imag;
    Animation animation;
    private LinearLayout lin;
    private RelativeLayout errorlayout;
    private  TextView errormessage, errortitle;
    private ImageView errorimage;
    private Button retry;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        errorlayout = findViewById(R.id.errorlayout);
        errormessage = findViewById(R.id.errormessage);
        errortitle = findViewById(R.id.errortitle);
        lin = findViewById(R.id.progress);
        progressBar= findViewById(R.id.linprogress);
        errormessage = findViewById(R.id.errormessage);
        errorimage = findViewById(R.id.errorimage);
        retry = findViewById(R.id.retry);
        butSignIn = (Button) findViewById(R.id.btnSignIn);
        butSignUp = (Button) findViewById(R.id.btnSignupm);
        imag = findViewById(R.id.imagesignup);



        Paper.init(this);


        butSignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(lin.getVisibility() == GONE) {
                    Intent sig = new Intent(MainActivity.this, Activity_Verify.class);
                    startActivity(sig);
                }else{

                }
            }
        });


        butSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(lin.getVisibility() == GONE) {
                Intent sign = new Intent(MainActivity.this, SignIn.class);
                startActivity(sign);
                }else{

                }
            }
        });

        String user = Paper.book().read(Common.USER_KEY);
        String pwd = Paper.book().read(Common.PWD_KEY);
        if(user !=null && pwd != null){
            if(!user.isEmpty() && !pwd.isEmpty()){
                login(user,pwd);
            }
        }


        animation = AnimationUtils.loadAnimation(this,
                R.anim.blink);
        animation.setAnimationListener(this);

        imag.startAnimation(animation);
    }

    private  void printKeyHash(){
        try{
            PackageInfo info = getPackageManager().getPackageInfo("com.example.changeme.kitchenapp",
                    PackageManager.GET_SIGNATURES);
            for(Signature signature:info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    private void login(final String phone, final String pwd) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");
        if(Common.isConnectedToInternet(getBaseContext()) ){

        lin.setVisibility(View.VISIBLE);






            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    if(dataSnapshot.child(phone).exists())
                    {



                        User user = dataSnapshot.child(phone).getValue(User.class);
                        user.setPhone(phone);
                        if (user.getPassword().equals(pwd) && Boolean.parseBoolean(user.getIsStaff()) == false) {
                            Intent homeIntent = new Intent(MainActivity.this, Home.class);
                            Common.currentuser = user;

                            startActivity(homeIntent);
                            finish();


                        } else {
                            lin.setVisibility(GONE);
                            Toast.makeText(MainActivity.this, "Wrong Password !!!!!", Toast.LENGTH_SHORT ).show();

                        }
                    }
                    else {
                       lin.setVisibility(GONE);
                        Toast.makeText(MainActivity.this, "Sign in Failed !", Toast.LENGTH_SHORT ).show();
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else{
            showErrorMessage(R.drawable.no_result,"Error", "Check your internet connection");
            return;
        }

    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    public void onBackPressed() {
         if(lin.getVisibility() == View.VISIBLE){
             this.moveTaskToBack(true);

        }else if(lin.getVisibility() == View.GONE) {
             super.onBackPressed();
             this.moveTaskToBack(true);



        }

    }

    private  void showErrorMessage(int imageview, String title, String message){
        if(errorlayout.getVisibility()== GONE){
            errorlayout.setVisibility(View.VISIBLE);
        }
        errorimage.setImageResource(imageview);
        errortitle.setText(title);
        errormessage.setText(message);



        new loadBackground().execute();
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.isConnectedToInternet(getBaseContext())){
                errorlayout.setVisibility(View.GONE);
                  recreate();

                }

            }
        });
    }


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private void loginasync(final String phone, final String pwd) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");
        if(Common.isConnectedToInternet(getBaseContext()) ){







            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    if(dataSnapshot.child(phone).exists())
                    {



                        User user = dataSnapshot.child(phone).getValue(User.class);
                        user.setPhone(phone);
                        if (user.getPassword().equals(pwd) && Boolean.parseBoolean(user.getIsStaff()) == false) {
                            Intent homeIntent = new Intent(MainActivity.this, Home.class);
                            Common.currentuser = user;

                            startActivity(homeIntent);
                            finish();


                        } else {

                            Toast.makeText(MainActivity.this, "Wrong Password !!!!!", Toast.LENGTH_SHORT ).show();

                        }
                    }
                    else {

                        Toast.makeText(MainActivity.this, "Sign in Failed !", Toast.LENGTH_SHORT ).show();
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else{
           new loadBackground().execute();
            return;
        }

    }

    public  class loadBackground extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {


            String user = Paper.book().read(Common.USER_KEY);
            String pwd = Paper.book().read(Common.PWD_KEY);
            if(user !=null && pwd != null){
                if(!user.isEmpty() && !pwd.isEmpty()){
                    loginasync(user,pwd);
                }
            }

            return null;

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            lin.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            lin.setVisibility(View.GONE);
        }
    }


}
