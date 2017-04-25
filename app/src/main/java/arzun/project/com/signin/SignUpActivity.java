package arzun.project.com.signin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

public class SignUpActivity extends AppCompatActivity {
    EditText name,email,password,phone,code;
    TextView termcondtion;
    Button btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        name = (EditText) findViewById(R.id.editTextName);
        email = (EditText) findViewById(R.id.editTextEmail);
        password = (EditText) findViewById(R.id.editTextPassword);
        phone = (EditText) findViewById(R.id.editTextPhoneno);
        code = (EditText) findViewById(R.id.editTextCode);
        btnRegister = (Button) findViewById(R.id.buttonRegister);
        termcondtion = (TextView) findViewById(R.id.termancondition);


        termcondtion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder tcBuilder = new AlertDialog.Builder(SignUpActivity.this);
                View tcView = getLayoutInflater().inflate(R.layout.termsandcondition, null);
                tcBuilder.setView(tcView);
                final AlertDialog dialog = tcBuilder.create();
                dialog.show();

                Button btnOk = (Button) tcView.findViewById(R.id.okButton);

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });


        final AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(SignUpActivity.this, R.id.editTextName, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(SignUpActivity.this, R.id.editTextEmail, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        awesomeValidation.addValidation(SignUpActivity.this, R.id.editTextPhoneno, "^[2-9]{2}[0-9]{8}$", R.string.mobileerror);
        
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()){

                    AlertDialog.Builder dBuilder = new AlertDialog.Builder(SignUpActivity.this);
                    View dView = getLayoutInflater().inflate(R.layout.dailogbox, null);
                    Button btnConfirm = (Button) dView.findViewById(R.id.buttonConfirm);
                    Button btnCancel = (Button) dView.findViewById(R.id.buttonCancel);
                    dBuilder.setView(dView);
                    final AlertDialog dialog = dBuilder.create();
                    dialog.show();
                    btnConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Name", name.getText().toString());
                            editor.putString("Email", email.getText().toString());
                            editor.putString("Password", password.getText().toString());
                            editor.putString("Phone", phone.getText().toString());
                            editor.putString("Code", code.getText().toString());
                            editor.apply();

                            Toast.makeText(SignUpActivity.this, "Succesfull", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            name.setText("");
                            email.setText("");
                            password.setText("");
                            phone.setText("");
                            code.setText("");
                            dialog.dismiss();
                        }
                    });
                }
                else
                    Toast.makeText(SignUpActivity.this, "validation Unsucessfull", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
