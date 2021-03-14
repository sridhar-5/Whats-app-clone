package com.amrita.whatsappclone;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    /**
     * Variables are to be created for All the views that are on the activity main . xml screen
     */
    private EditText phone_number;
    private EditText code;
    private Button verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * since this is the first every activity that happens in this app we are initializing the firebase here
         */
        FirebaseApp.initializeApp(this);

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

        verify.setOnContextClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                InitiatePhoneNumberVerification();
            }
        });
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
                .setCallbacks(mcallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);      //passing the phone number to phone auth provider to request verification


    }

}