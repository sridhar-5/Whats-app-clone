package com.amrita.whatsappclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    /**
     * Variables are to be created for All the views that are on the activity main . xml screen
     */
    private EditText phone_number;
    private String verification_id;
    private EditText code;
    private Button verify;

    //call back instance created here
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * since this is the first every activity that happens in this app we are initializing the firebase here
         */
        FirebaseApp.initializeApp(this);

        //here we are checking if the user is logged in or not if logged in directly we move to activitymain2

        UserIsLoggedIn();

        /**
         * reading all the inputs from the view screen (activity_main.xml)
         */

        //storing  the phone number in variable here
        phone_number = findViewById(R.id.phone_number);

        //storing the verification code here
        code = findViewById(R.id.Code);

        //button input here
        verify = findViewById(R.id.verification);

        /**
         * we are now setting up an onclick listener on to the button verify
         */

        verify.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (verification_id != null){
                    VerifyPhoneNumberWithCode();
                }
                else{

                    InitiatePhoneNumberVerification();
                }
            }
        });

        /**
         * this works as a nested class to handle the next state of the user after authntication after success or failure
         */

        mcallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                //if the verification is successful then
                SignInWithPhoneAuthCredentials(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
            }

            //This is checking for the code that is sent to the user and the firebase generated code is same or not


            @Override
            public void onCodeSent(@NonNull String Verification_id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(Verification_id, forceResendingToken);

                //storing the firebase generate code in a string
                verification_id = Verification_id;

                //change the text of the button to verify code since the code is already sent and verification should be done
                verify.setText(R.string.ButtonChangeOnCodeSent);
            }
        };
    }

    private void VerifyPhoneNumberWithCode() {
        PhoneAuthCredential credentials = PhoneAuthProvider.getCredential(verification_id, code.getText().toString());
        SignInWithPhoneAuthCredentials(credentials);
    }


    private void SignInWithPhoneAuthCredentials(PhoneAuthCredential phoneAuthCredential) {
        /**
         * we here set an on complete listener to check if the phone number verification is complete or not
         */
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //task is to check the if the task is i/e the log in is successful or not
                if (task.isSuccessful()) {
                    UserIsLoggedIn();
                }
            }
        });
    }

    private void UserIsLoggedIn() {
        //this function will check if the user is logged in or not if he is already logged in then we simply move on to the other steps
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            startActivity(new Intent(getApplicationContext(), MainpageActivity2.class));
            finish();
        }
    }

    private void InitiatePhoneNumberVerification() {

        //initializing the FirebaseAuth instance

        FirebaseAuth authentication = FirebaseAuth.getInstance();

        /**
         * initializing the phone auth options
         * To initiate phone number sign-in, presenting the user an interface that prompts them to type their phone number.
         * Then, pass their phone number to the PhoneAuthProvider.verifyPhoneNumber method to request that Firebase verify the user's phone number
         */
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(authentication)
                .setPhoneNumber(phone_number.toString())     //we are passing in the phone number to get it verified
                .setTimeout(60L, TimeUnit.SECONDS)    //passing the time out (Long)  and the unit of time out
                .setActivity(this)
                .setCallbacks(mcallbacks)        //mcallbacks this will check what happens next after the phone number verification. so it will basically handle  success or failure of the authnticaation
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);      //passing the phone number to phone auth provider to request verification

    }

    /**
     * The steps to be followed while we are making a phone number verification for an app:
     * 1. initialize an instance for firebaseauth class
     * 2. initialize phone auth options and pass in the parameters to verify the phone number
     * 3. now create ad fill all the methods that callback suggest s
     * 4. call backs uses phoneauthprovider.onverificationstatuschanged callbacks object
     * an override the necessary functions.
     */

}
