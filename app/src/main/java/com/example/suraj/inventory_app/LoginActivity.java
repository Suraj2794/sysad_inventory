package com.example.suraj.inventory_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.suraj.inventory_app.main.MainActivity;
import com.example.suraj.inventory_app.util.ServerRequest;
import com.example.suraj.inventory_app.util.UtilClass;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText userId,password;
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
                    Toast.makeText(LoginActivity.this,"Enter CSE LDAP ID",Toast.LENGTH_LONG).show();
                else if(password.getText().toString().equals(""))
                    Toast.makeText(LoginActivity.this,"Enter Password",Toast.LENGTH_LONG).show();
                else
                    makeLogin();
            }
        });
    }

    private void makeLogin() {

        String url = getString(R.string.url) + "/login";

        StringRequest jsonReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.cancel();
                        if (response.contains("True")) {
                            Intent go = new Intent(LoginActivity.this, MainActivity.class);

                            UtilClass.UpdateId(LoginActivity.this,userId.getText().toString());
                            startActivity(go);
                            finish();
                        } else if (response.contains("Username"))
                            Toast.makeText(getApplicationContext(), "User Not Found", Toast.LENGTH_SHORT).show();
                        else if (response.contains("Password"))
                            Toast.makeText(getApplicationContext(), "UserId-Password Combination not correct", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getApplicationContext(), "Error Occurred.", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.cancel();
                        if(UtilClass.isConnected(LoginActivity.this))
                            Toast.makeText(getApplicationContext(),"Error Occurred! Try Again", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(),"Connect to Internet", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("UserID", userId.getText().toString());
                param.put("Password", password.getText().toString());
                return param;
            }
        };

        loading = new ProgressDialog(LoginActivity.this);
        loading.setCancelable(false);
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.setMessage("Signing In.....");
        loading.show();
        ServerRequest.getInstance(getApplicationContext()).addRequestQueue(jsonReq);
    }
}
