package com.example.suraj.inventory_app.main;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.suraj.inventory_app.R;
import com.example.suraj.inventory_app.util.Password;
import com.example.suraj.inventory_app.util.ServerRequest;
import com.example.suraj.inventory_app.util.UtilClass;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Get_History.OnFragmentInteractionListener {

    private static final int ISSUE_ICARD_SCAN_CODE = 1;
    private static final int ISSUE_QR_SCAN_CODE = 2;
    private static final int RETRUN_QR_SCAN_CODE = 3;
    private ProgressDialog loading;
    String student_scanned_id = null;
    boolean ISSUE_ICARD_SCAN_DONE = false;
    boolean LOGIN_DONE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (!LOGIN_DONE) {
            doLogin();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ISSUE_ICARD_SCAN_DONE = false;
    }

    private void doLogin() {
        AlertDialog.Builder pass = new AlertDialog.Builder(MainActivity.this);
        final EditText passText = new EditText(pass.getContext());
        passText.setHint("Sysad Password");
        passText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pass.setView(passText);
        pass.setTitle("Enter Password");
        pass.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        pass.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (convertToMD5(passText.getText().toString()).equals(Password.password)) {
                    LOGIN_DONE = true;
                } else {
                    Toast.makeText(MainActivity.this, "Incorrect Password.", Toast.LENGTH_LONG).show();
                    doLogin();
                }
            }
        });
        pass.show();
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            ISSUE_ICARD_SCAN_DONE = false;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fr = null;
        if (id == R.id.search) {
            fr = new Get_History();
        } else if (id == R.id.issue) {
            /*
            When User selects issue, first we will recheck he password to be ensured that the same
            user is doing request.
             */
            barcodeScan(ISSUE_ICARD_SCAN_CODE);

        } else if (id == R.id.component_return) {
            barcodeScan(RETRUN_QR_SCAN_CODE);

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        if (fr != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fr).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * This method is used to scan barcode.
     * The ZXing library does the scanning and return the result in onActivityResult()
     */
    void barcodeScan(int request_code) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        switch (request_code) {
            case ISSUE_ICARD_SCAN_CODE:
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setPrompt("Scan Barcode on I-card");
                startActivityForResult(integrator.createScanIntent(), ISSUE_ICARD_SCAN_CODE);
                break;
            case ISSUE_QR_SCAN_CODE:
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan QR Code on Component");
                startActivityForResult(integrator.createScanIntent(), ISSUE_QR_SCAN_CODE);
                break;
            case RETRUN_QR_SCAN_CODE:
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan QR Code on Component");
                startActivityForResult(integrator.createScanIntent(), RETRUN_QR_SCAN_CODE);
                break;
        }
    }

    /**
     * Here data will contain the scanned string from QR.
     * If we had many other startActivityForResult() then we would have used requestCode.
     * But here its fine. We don't need it.
     * <p>
     * Once we get the barcode we generate the issue request.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case ISSUE_ICARD_SCAN_CODE:
                    Log.e("Scanned Barcode is: ", "" + data);
                    student_scanned_id = data.getStringExtra("SCAN_RESULT");
                    barcodeScan(ISSUE_QR_SCAN_CODE);
                    break;
                case ISSUE_QR_SCAN_CODE:
                    Log.e("Scanned QR Code is: ", "" + data);
                    issueComponent(data.getStringExtra("SCAN_RESULT"));
                    break;
                case RETRUN_QR_SCAN_CODE:
                    Log.e("Scanned QR Code is: ", "" + data);
                    returnComponent(data.getStringExtra("SCAN_RESULT"));
                    break;
            }
        } else {
            Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * This method return the component to the user.
     *
     * @param component_id Id scanned from the QR
     */
    private void returnComponent(final String component_id) {
        String url = getString(R.string.url) + "/return";

        StringRequest barcode = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.cancel();
                        if (response.contains("Done")) {
                            Toast.makeText(getApplicationContext(), "System Returned", Toast.LENGTH_LONG).show();
                        } else if (response.contains("Inconsistent"))
                            Toast.makeText(getApplicationContext(), "System not Issued!", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getApplicationContext(), "Database Issue!", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.cancel();
                        if (UtilClass.isConnected(MainActivity.this))
                            Toast.makeText(getApplicationContext(), "Error Occurred! Try Again", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(), "Connect to Internet", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("component_id", component_id);
                Log.e("Param", param.toString());
                return param;
            }
        };

        loading = new ProgressDialog(MainActivity.this);
        loading.setCancelable(false);
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.setMessage("Checking Barcode.....");
        loading.show();
        ServerRequest.getInstance(getApplicationContext()).addRequestQueue(barcode);
    }

    /**
     * This method issues the component to the user.
     *
     * @param component_id Id scanned from the QR
     */
    private void issueComponent(final String component_id) {
        String url = getString(R.string.url) + "/issue";

        StringRequest barcode = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.cancel();
                        if (response.contains("Done")) {
                            Toast.makeText(getApplicationContext(), "System Issued", Toast.LENGTH_LONG).show();
                            ISSUE_ICARD_SCAN_DONE = false;
                            student_scanned_id = null;

                        } else if (response.contains("Inconsistent"))
                            Toast.makeText(getApplicationContext(), "System already Issued!", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getApplicationContext(), "Database Connection Error!", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.cancel();
                        if (UtilClass.isConnected(MainActivity.this))
                            Toast.makeText(getApplicationContext(), "Error Occurred! Try Again", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(), "Connect to Internet", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("user_id", student_scanned_id);
                param.put("component_id", component_id);
                Log.e("Param", param.toString());
                return param;
            }
        };

        loading = new ProgressDialog(MainActivity.this);
        loading.setCancelable(false);
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.setMessage("Checking Barcode.....");
        loading.show();
        ServerRequest.getInstance(getApplicationContext()).addRequestQueue(barcode);
    }
}
