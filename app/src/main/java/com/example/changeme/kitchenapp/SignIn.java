package com.example.changeme.kitchenapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.changeme.kitchenapp.Model.User;
import com.example.changeme.kitchenapp.common.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class SignIn extends AppCompatActivity implements Animation.AnimationListener {
    EditText editPhone, editpassword;
    TextView forgotpassword, signup;
    ImageView imag;
    Button signin;
    Animation animation;
    LinearLayout lin;

    CheckBox checkBox;
    DatabaseReference table_user;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        editPhone = (MaterialEditText)findViewById(R.id.Editphone);
        imag = findViewById(R.id.imagesignup);
        lin = findViewById(R.id.progress);
        editpassword = (MaterialEditText)findViewById(R.id.EditPassword);
        signin =  findViewById(R.id.btnsignin);
        signup = findViewById(R.id.newUserClick);
        checkBox = findViewById(R.id.chbRemember);
            forgotpassword = findViewById(R.id.forgotpassword);
        Paper.init(this);



         database = FirebaseDatabase.getInstance();
          table_user = database.getReference("User");

         signup.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(SignIn.this, Activity_Verify.class);
                 startActivity(intent);
             }
         });
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showforgotpwdDialog();
            }
        });


        animation = AnimationUtils.loadAnimation(this,
                R.anim.blink);
        animation.setAnimationListener(this);

        imag.startAnimation(animation);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Common.isConnectedToInternet(getBaseContext()) ){


            if(checkBox.isChecked()){
                Paper.book().write(Common.USER_KEY,editPhone.getText().toString());
                Paper.book().write(Common.PWD_KEY, editpassword.getText().toString());

            }
            else{
                Paper.book().write(Common.USER_KEY,editPhone.getText().toString());
                Paper.book().write(Common.PWD_KEY, editpassword.getText().toString());
            }

    imag.startAnimation(animation);
    lin.setVisibility(View.VISIBLE);


                    table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        if(dataSnapshot.child(editPhone.getText().toString()).exists() && editPhone.getText().length()==11  && editpassword.getText().length()>1)
                        {

                             User user = dataSnapshot.child(editPhone.getText().toString()).getValue(User.class);
                        user.setPhone(editPhone.getText().toString());
                            if (user.getPassword().equals(editpassword.getText().toString()) && Boolean.parseBoolean(user.getIsStaff()) == false  ) {
                                Intent homeIntent = new Intent(SignIn.this, Home.class);
                                Common.currentuser = user;
                                startActivity(homeIntent);

                                finish();
                                table_user.removeEventListener(this);


                            } else {

                                lin.setVisibility(View.GONE);
                                Toast.makeText(SignIn.this, "Wrong Password !!!!!", Toast.LENGTH_SHORT ).show();

                            }
                        }
else {
                          lin.setVisibility(View.GONE);
                            Toast.makeText(SignIn.this, "Phone number/password not valid !", Toast.LENGTH_SHORT ).show();
                        }



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } else{
                    lin.setVisibility(View.GONE);
                    Toast.makeText(SignIn.this, "Check Your internet Connection!!", Toast.LENGTH_SHORT ).show();
                    return;
                }

            }




        });

    }

    private void showforgotpwdDialog() {
        ContextThemeWrapper ctw = new ContextThemeWrapper(SignIn.this, R.style.MyDialogTheme);
        AlertDialog.Builder builder = new AlertDialog.Builder(ctw);
        builder.setTitle("Forgot Password");
        builder.setMessage("Enter your secure code");


        LayoutInflater inflater = LayoutInflater.from(this);
        View forgot_view = inflater.inflate(R.layout.password_forgot, null);
        builder.setView(forgot_view);
        builder.setIcon(R.drawable.ic_history_black_48dp);
        builder.setView(forgot_view);

        final MaterialEditText editphone1 =  forgot_view.findViewById(R.id.putphone);

        final MaterialEditText editsecurecode1 = forgot_view.findViewById(R.id.putcode);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (Common.isConnectedToInternet(getBaseContext())) {

                    if(editsecurecode1.getText().toString().length() >0
                            &&editphone1.getText().toString().length()==11) {

                        table_user.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child(editphone1.getText().toString()).exists() ) {


                                    User user = dataSnapshot.child(editphone1.getText().toString()).getValue(User.class);

                                    if (user.getSecurecode().equals(editsecurecode1.getText().toString()) && Boolean.parseBoolean(user.getIsStaff()) == false) {
                                        Snackbar.make(getWindow().getDecorView().getRootView(), "Hello: "+ user.getName()+ "\n"+ "Your Password is: "+ user.getPassword(), Snackbar.LENGTH_LONG)
                                                .show();
                                    } else {
                                        Toast.makeText(SignIn.this, "Wrong Secure Code", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(SignIn.this, "Phone No doesn't not Exist", Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }else {
                        Toast.makeText(SignIn.this, "Invalid Phone No/ Secure Code", Toast.LENGTH_SHORT).show();

                    }



                } else {

                    Toast.makeText(SignIn.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();


                }

            }});
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();


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

    @Override
    public void onBackPressed() {
        if(lin.getVisibility() == View.VISIBLE){

        }else{
        super.onBackPressed();

    }
}}