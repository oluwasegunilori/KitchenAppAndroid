package com.example.changeme.kitchenapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.changeme.kitchenapp.Model.User;
import com.example.changeme.kitchenapp.common.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;


/**
 * Created by SHEGZ on 12/4/2017.
 */
public class Signup extends AppCompatActivity {
    public int yy = 1;
    MaterialEditText editPhone, editpassword, editname, editsecurecode, editcode;
    Button signup, verifycode, resendcode;
    String parser =null;
    boolean takee = false;
    private FirebaseAuth mAuth;
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);
        parser = getIntent().getStringExtra(Activity_Verify.ext);



        editPhone = (MaterialEditText) findViewById(R.id.Editphoneup);

        editpassword = (MaterialEditText) findViewById(R.id.EditPasswordup);
        editname = (MaterialEditText) findViewById(R.id.Editnameup);
        editsecurecode = (MaterialEditText) findViewById(R.id.Editsecurecode);
        signup = (Button) findViewById(R.id.btnsignup);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        editPhone.setText(parser);
        showDia(yy);

        editsecurecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDia(yy);


            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Common.isConnectedToInternet(getBaseContext())) {

                    if (editPhone.getText().toString().length() == 11 && !editpassword.getText().toString().isEmpty()
                            && !editname.getText().toString().isEmpty() &&
                            !editsecurecode.getText().toString().isEmpty()) {


                        final android.app.AlertDialog waitingdialog = new SpotsDialog(Signup.this);
                        waitingdialog.show();
                        waitingdialog.setTitle("Please Wait");


                        table_user.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                int ibukunoluwa = 0;
                                if (!dataSnapshot.child(editPhone.getText().toString()).exists() ) {
                                    ibukunoluwa++;
                                    waitingdialog.dismiss();
                                    User user = new User(editname.getText().toString(), editpassword.getText().toString(), editsecurecode.getText().toString());
                                    table_user.child(editPhone.getText().toString()).setValue(user);
                                    Toast.makeText(Signup.this, "Signed Up Successfully  ", Toast.LENGTH_SHORT).show();
                                    Paper.book().write(Common.USER_KEY,editPhone.getText().toString());
                                    Paper.book().write(Common.PWD_KEY, editpassword.getText().toString());
                                    login(editPhone.getText().toString(), editpassword.getText().toString());
                                    Signup.this.finish();

                                }
                                if (dataSnapshot.child(editPhone.getText().toString()).exists()) {
                                    waitingdialog.dismiss();

                                    Toast.makeText(Signup.this, "User Has been Registered", Toast.LENGTH_SHORT).show();


                                }


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    } else {
                        Toast.makeText(Signup.this, "One or more fields have not being filled Properly!!", Toast.LENGTH_SHORT).show();

                    }


                } else {
                    Toast.makeText(Signup.this, "Check Your internet Connection!!", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

    private void showDia(int y) {
        if (y == 1) {
            ContextThemeWrapper ctw = new ContextThemeWrapper(Signup.this, R.style.MyDialogTheme);

            final AlertDialog.Builder alertdialog = new AlertDialog.Builder(ctw);
            alertdialog.setTitle("NOTE");
            alertdialog.setMessage("<Please make your alternative password as simple as possible. It helps you recover your password when you forget it>  ");

            alertdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            alertdialog.show();
        }
        yy++;
    }


    @Override
    public void onBackPressed() {
        Intent iny = new Intent(Signup.this, MainActivity.class);
        iny.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(iny);
    }

    private void login(final String phone, final String pwd) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");
        if(Common.isConnectedToInternet(getBaseContext()) ){




            final android.app.AlertDialog waitingdialog = new SpotsDialog(Signup.this);
            waitingdialog.show();



            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    if(dataSnapshot.child(phone).exists())
                    {



                        User user = dataSnapshot.child(phone).getValue(User.class);
                        user.setPhone(phone);
                        if (user.getPassword().equals(pwd) && Boolean.parseBoolean(user.getIsStaff()) == false) {
                            Intent homeIntent = new Intent(Signup.this, Home.class);
                            Common.currentuser = user;
                            waitingdialog.dismiss();
                            startActivity(homeIntent);
                            finish();


                        } else {

                            Toast.makeText(Signup.this, "Wrong Password !!!!!", Toast.LENGTH_SHORT ).show();

                        }
                    }
                    else {
                        waitingdialog.dismiss();
                        Toast.makeText(Signup.this, "Sign in Failed !", Toast.LENGTH_SHORT ).show();
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else{
            Toast.makeText(Signup.this, "Check Your internet Connection!!", Toast.LENGTH_SHORT ).show();
            return;
        }

    }
}



