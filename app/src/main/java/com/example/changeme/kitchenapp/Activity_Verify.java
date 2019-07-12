package com.example.changeme.kitchenapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.changeme.kitchenapp.common.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.concurrent.TimeUnit;

public class Activity_Verify extends AppCompatActivity {
    public final static String ext = "iure";
    public final static int exte = 1;
    public LinearLayout layout;
    String keeper = "";
    MaterialEditText editPhone, editsecurecode;
    Button sendcode, verifycode, resendcode;
    DatabaseReference tableuser;
    FirebaseDatabase database;
TextView tre, tre1;
    private RelativeLayout errorlayout;
    private  TextView errormessage, errortitle;
    private ImageView errorimage;
    private Button retry;
    private FirebaseAuth mAuth;
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

private ProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity__verify);
        tre = (TextView) findViewById(R.id.textnotice);
        tre1 = (TextView) findViewById(R.id.textnotice1);
        database = FirebaseDatabase.getInstance();

        tableuser = database.getReference("User");
        editPhone = (MaterialEditText) findViewById(R.id.editPhoneee);
        editsecurecode = (MaterialEditText) findViewById(R.id.editCode);
        sendcode = (Button) findViewById(R.id.btnSendCode);
        resendcode = (Button) findViewById(R.id.btnresend);
        layout = findViewById(R.id.verifycode);
        verifycode = (Button) findViewById(R.id.btnVerify);
        sendcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.isConnectedToInternet(getBaseContext())) {

                    if(validatePhoneNumber()) {
                        tableuser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                if(dataSnapshot.child(editPhone.getText().toString()).exists()) {
                                    Toast.makeText(Activity_Verify.this, "This Phone Number has been registered", Toast.LENGTH_SHORT).show();
                                    tre.setText("");
                                }else{
                                    startPhoneNumberVerification(editPhone.getText().toString());
                                    mDialog = new ProgressDialog(Activity_Verify.this);
                                    mDialog.setMessage("Verifying.....");
                                    mDialog.setCancelable(false);

                                    mDialog.show();
                                    tre.setText("A code will be sent shortly to your phone");

                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                }else{
                    Toast.makeText(Activity_Verify.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                    tre.setText("");
                }
            }else {
                    Toast.makeText(Activity_Verify.this, "Please Check Your Internet", Toast.LENGTH_SHORT).show();
                }
                }

        });

        resendcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.isConnectedToInternet(getBaseContext())) {

                    if(validatePhoneNumber()) {

                        tableuser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                if(dataSnapshot.child(editPhone.getText().toString()).exists()) {
                                    Toast.makeText(Activity_Verify.this, "This Phone Number has been registered", Toast.LENGTH_SHORT).show();

                                }else{


                                    resendVerificationCode(editPhone.getText().toString(),mResendToken);
                                    mDialog = new ProgressDialog(Activity_Verify.this);
                                    mDialog.setMessage("Verifying.....");
                                    mDialog.show();
                                    tre.setText("A code will resent to your phone");


                                    Toast.makeText(Activity_Verify.this, "Code will be Resent", Toast.LENGTH_SHORT).show();



                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
















                }else {

                     Toast.makeText(Activity_Verify.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();

                 }
                }else {

                    Toast.makeText(Activity_Verify.this, "Please Check Your Internet", Toast.LENGTH_SHORT).show();
                }
                }
        });

verifycode.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if (Common.isConnectedToInternet(getBaseContext())) {

            if(validateCode()) {
            verifyPhoneNumberWithCode(mVerificationId, editsecurecode.getText().toString());

            mDialog.dismiss();
            mDialog.setMessage("Verifying.....");
            mDialog.show();

        }
        else{
            Toast.makeText(Activity_Verify.this, "Please Enter Code", Toast.LENGTH_SHORT).show();

        }


    }else {
            Toast.makeText(Activity_Verify.this, "Please Check Your Internet", Toast.LENGTH_SHORT).show();
        }
        }
});




            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(PhoneAuthCredential credential) {
                    // This callback will be invoked in two situations:
                    // 1 - Instant verification. In some cases the phone number can be instantly
                    //     verified without needing to send or enter a verification code.
                    // 2 - Auto-retrieval. On some devices Google Play services can automatically
                    //     detect the incoming verification SMS and perform verificaiton without
                    //     user action.

                    mVerificationInProgress = false;
                    signInWithPhoneAuthCredential(credential);
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    // This callback is invoked in an invalid request for verification is made,
                    // for instance if the the phone number format is not valid.

                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        // Invalid re
                        // quest
                        // ...
                    } else if (e instanceof FirebaseTooManyRequestsException) {
                        // The SMS quota for the project has been exceeded
                        // ...
                        Toast.makeText(Activity_Verify.this, "Maximum SMS has been reached", Toast.LENGTH_SHORT).show();
                    }

                    // Show a message and update the UI
                    // ...
                }

                @Override
                public void onCodeSent(String verificationId,
                                       PhoneAuthProvider.ForceResendingToken token) {


                    // Save verification ID and resending token so we can use them later
                    mVerificationId = verificationId;
                    mResendToken = token;
                    keeper = editPhone.getText().toString();
                tre.setText("Code Has been sent");
                mDialog.dismiss();
                layout.setVisibility(View.VISIBLE);

                    // ...
                }
            };

        }


    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                editPhone.getText().toString(),        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);}

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            Toast.makeText(Activity_Verify.this, "Phone Number Successfully verified", Toast.LENGTH_SHORT).show();


                            // Sign in success, update UI with the signed-in user's information
                            Log.d("PhoneVERifier", "signInWithCredential:success");

                            FirebaseAuth.getInstance().signOut();
                            keeper = editPhone.getText().toString();
                            mDialog.dismiss();
                            Intent inter = new Intent(Activity_Verify.this, Signup.class);
                             inter.putExtra( ext, keeper);

                                startActivity(inter);
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            mDialog.dismiss();
                            Toast.makeText(Activity_Verify.this, "Unable to verify your Phone Number", Toast.LENGTH_SHORT).show();

                            Log.w("PhoneVERifier", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(Activity_Verify.this, "Verification Code not Correct", Toast.LENGTH_SHORT).show();
// The verification code entered was invalid
                            }
                        }
                    }
                });
    }


    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }
    private boolean validatePhoneNumber() {
        String phoneNumber = editPhone.getText().toString();
        if (phoneNumber.length() !=11) {

            return false;
        }
    return  true;
    }
    private boolean validateCode() {
        String coder = editsecurecode.getText().toString();
        if (coder.length() <1) {

            return false;
        }
        return true;
    }
    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                editPhone.getText().toString(),        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
// [END resend_verification]







    private void signOut() {
        mAuth.signOut();
    }






    }
