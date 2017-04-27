package arzun.project.com.signin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.DeviceLoginManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.plus.Plus;

public class UserDetailActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    TextView name,email,phone;
    Button btnLogout;

    GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        name = (TextView) findViewById(R.id.nameTextView);
        email = (TextView) findViewById(R.id.emailTextView);
        phone = (TextView) findViewById(R.id.phoneTextView);

        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .addApi(Plus.API)
                .build();

        btnLogout=(Button)findViewById(R.id.logout);
        Bundle b=new Bundle();
        b=getIntent().getExtras();
        String iid=b.getString("ID");
        final int id=Integer.parseInt(iid);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (id){
                    case 1:
                        break;
                    case 2:
                        LoginManager.getInstance().logOut();
                        Intent i=new Intent(UserDetailActivity.this,MainActivity.class);
                        startActivity(i);
                        break;
                    case 3:
                        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                Intent i=new Intent(UserDetailActivity.this,MainActivity.class);
                                startActivity(i);
                            }
                        });
                        break;
                }
            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);

        String uname= sharedPreferences.getString("NaME", "");
        String uemail = sharedPreferences.getString("Email", "");
        String uphone = sharedPreferences.getString("Phone", "");

        name.setText(uname);
        email.setText(uemail);
        phone.setText(uphone);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
