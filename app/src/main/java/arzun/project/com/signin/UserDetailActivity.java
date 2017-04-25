package arzun.project.com.signin;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class UserDetailActivity extends AppCompatActivity {
    TextView name,email,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        name = (TextView) findViewById(R.id.nameTextView);
        email = (TextView) findViewById(R.id.emailTextView);
        phone = (TextView) findViewById(R.id.phoneTextView);

        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);

        String uname= sharedPreferences.getString("NaME", "");
        String uemail = sharedPreferences.getString("Email", "");
        String uphone = sharedPreferences.getString("Phone", "");

        name.setText(uname);
        email.setText(uemail);
        phone.setText(uphone);
    }
}
