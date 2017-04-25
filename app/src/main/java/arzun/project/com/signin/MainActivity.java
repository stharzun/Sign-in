package arzun.project.com.signin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.AuthAccountRequest;
import com.google.android.gms.plus.Plus;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    Button btnLogin,btnSignup;
    EditText logEmail,logPassword;
    TextView frogetpw,term;


    LoginButton loginButton;
    TextView textView;
    CallbackManager callbackManager;

    SignInButton btnGoogle;
    GoogleApiClient googleApiClient;
    private static final int REQ_CODE=0001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin=(Button)findViewById(R.id.buttonLogin);
        btnSignup=(Button)findViewById(R.id.buttonSignup);

        logEmail=(EditText)findViewById(R.id.loginemail);
        logPassword=(EditText)findViewById(R.id.loginpassword);

        frogetpw=(TextView)findViewById(R.id.forgetpassword);
        term=(TextView)findViewById(R.id.termcondition);


        loginButton = (LoginButton) findViewById(R.id.fb_login_bn);
        textView = (TextView) findViewById(R.id.textView);
        callbackManager = CallbackManager.Factory.create();

        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .addApi(Plus.API)
                .build();
        btnGoogle=(SignInButton)findViewById(R.id.google_login_bn);




        term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder tcBuilder=new AlertDialog.Builder(MainActivity.this);
                View tcView=getLayoutInflater().inflate(R.layout.termsandcondition,null);
                tcBuilder.setView(tcView);
                final AlertDialog dialog=tcBuilder.create();
                dialog.show();

                Button btnOk=(Button)tcView.findViewById(R.id.okButton);

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });
        frogetpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
            }

        });
     /*
        * Normal Log In Process
        *
        *
        * */
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences= getSharedPreferences("userInfo",MODE_PRIVATE);
                String email=sharedPreferences.getString("Email","");
                String password=sharedPreferences.getString("Password","");
                if (logEmail.getText().toString().equals(email) && logPassword.getText().toString().equals(password)){
                    Intent loginSucess=new Intent(MainActivity.this,UserDetailActivity.class);
                    startActivity(loginSucess);
                }

                else
                    Toast.makeText(MainActivity.this, "Login UnSucess Try Again", Toast.LENGTH_SHORT).show();
            }
        });
     /*
        * Facebook Log In Process
        *
        *
        * */
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                textView.setText("Login Success \n" +
                        loginResult.getAccessToken().getUserId() +
                        "\n" + loginResult.getAccessToken().getToken());
                Toast.makeText(MainActivity.this, "Login Sucessfull with facebook", Toast.LENGTH_SHORT).show();

                String token=loginResult.getAccessToken().getToken();
                String id=loginResult.getAccessToken().getUserId();
                SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Name", token);
                editor.putString("Email", id);
                editor.apply();
                Intent loginSucess=new Intent(MainActivity.this,UserDetailActivity.class);
                startActivity(loginSucess);


            }

            @Override
            public void onCancel() {

                textView.setText("Login Cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                textView.setText("Login Fail");

            }
        });

        /*
        * Google Log In Process
        *
        *
        * */
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(signInIntent,REQ_CODE);
//                Toast.makeText(MainActivity.this, "Sign in sucess", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode==REQ_CODE){
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()){
            final GoogleSignInAccount account=result.getSignInAccount();
            
            String name=account.getDisplayName();
            final String email=account.getEmail();

            SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
               editor.putString("Name", name);
                editor.putString("Email", email);
            editor.apply();
            Intent loginSucess=new Intent(MainActivity.this,UserDetailActivity.class);
            startActivity(loginSucess);
        }
        else {
            Toast.makeText(this, "login fail", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
