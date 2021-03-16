package com.amrita.whatsappclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainpageActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage2);

        /**
         * if we are logged in we also have to have a logout button to log out
         * we have a logout button on the main page 2
         */

        Button logout_button  = findViewById(R.id.log_out);

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserLogout();
            }
        });
    }

    private void UserLogout() {
        //we are signing the user out by calling this function
        FirebaseAuth.getInstance().signOut();

        // and this page (activity main 2) should only be visible to the users who are logged in or their phone numbers are verified
        //so we also have to change the page that the users are seeing

        Intent intent = new Intent(getApplicationContext(),MainpageActivity2.class);

        //and we are clearing all the activities that are startes and other things that are running that requires the user to be logged in

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //this is where we get directed to the previous page
        startActivity(intent);

        // and here we are finishing the activity (i.e) that activity that is directing the user to the authentication page again
        finish();
    }
}