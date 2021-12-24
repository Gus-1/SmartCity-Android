package com.example.smartcity.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.auth0.android.jwt.DecodeException;
import com.auth0.android.jwt.JWT;
import com.example.smartcity.MainActivity;
import com.example.smartcity.R;
import com.example.smartcity.data.model.User;

import java.util.Date;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String JWTString = this.getSharedPreferences("JSONWEBTOKEN",Context.MODE_PRIVATE).getString("JSONWEBTOKEN","");

        if(!JWTString.isEmpty()) {
            try{
                JWT jwt = new JWT(JWTString);

                if(Objects.requireNonNull(jwt.getExpiresAt()).after(new Date())){
                    Toast.makeText(this, getString(R.string.welcome) + jwt.getClaim("value").asObject(User.class).getFirstname(), Toast.LENGTH_LONG).show();

                    startActivity(new Intent(this, MainActivity.class));
                } else {
                    this.deleteSharedPreferences("JSONWEBTOKEN");
                }

            } catch (DecodeException ignored) {}
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
