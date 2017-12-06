package com.example.suraj.inventory_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.suraj.inventory_app.main.MainActivity;
import com.example.suraj.inventory_app.util.Password;
import com.example.suraj.inventory_app.util.ServerRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText userId, password;
    Button login;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userId = (EditText) findViewById(R.id.userId);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userId.getText().toString().equals(""))
                    Toast.makeText(LoginActivity.this, "Enter CSE LDAP ID", Toast.LENGTH_LONG).show();
                else if (password.getText().toString().equals(""))
                    Toast.makeText(LoginActivity.this, "Enter Password", Toast.LENGTH_LONG).show();
                else if (userId.getText().toString().equals("sysad") && convertToMD5(password.getText().toString()).equals(Password.password)) {
                    Intent go = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(go);
                    finish();
                } else
                    Toast.makeText(LoginActivity.this, "ID Password does not match", Toast.LENGTH_LONG).show();

            }
        });
    }

    public String convertToMD5(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(password.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

}
