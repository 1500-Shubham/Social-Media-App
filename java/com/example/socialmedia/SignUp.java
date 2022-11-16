package com.example.socialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity implements View.OnClickListener{
EditText username,email,password,confirmpass,phone;
FirebaseAuth mAuth;
    Button signupnext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Views
        username=findViewById(R.id.signupname);
        email=findViewById(R.id.signupnemail);
        password=findViewById(R.id.signuppassword);
        confirmpass=findViewById(R.id.signupconfirmpassword);
        phone=findViewById(R.id.signupphone);
        signupnext=findViewById(R.id.signupnext);

        //Firebase
        mAuth=FirebaseAuth.getInstance();

    }

    public void login(){
        Intent intent = new Intent(SignUp.this,MainActivity.class);
        startActivity(intent);
        finish();
    } //going to main activity

    public void signup_next(View view){
        signupnext.setPressed(true);
        if(username.getText().toString().isEmpty()||email.getText().toString().isEmpty()||password.getText().toString().isEmpty()||confirmpass.getText().toString().isEmpty()||phone.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Fill All The Details!!", Toast.LENGTH_SHORT).show();
        }else if(phone.length()<10){
            Toast.makeText(SignUp.this, "Phone Number Is Invalid!! Need 10 digits", Toast.LENGTH_SHORT).show();
        }
        else if(password.getText().toString().equals(confirmpass.getText().toString())&&phone.length()==10)
        {  mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseDatabase.getInstance().getReference().child("users").child(task.getResult().getUser().getUid()).child("email").setValue(email.getText().toString());
                            FirebaseDatabase.getInstance().getReference().child("users").child(task.getResult().getUser().getUid()).child("password").setValue(password.getText().toString());
                            FirebaseDatabase.getInstance().getReference().child("users").child(task.getResult().getUser().getUid()).child("username").setValue(username.getText().toString());
                            FirebaseDatabase.getInstance().getReference().child("users").child(task.getResult().getUser().getUid()).child("phone").setValue(phone.getText().toString());
                            Toast.makeText(SignUp.this, "SignUp Successfully", Toast.LENGTH_SHORT).show();
                            login();
                        }
                        else {
                            Toast.makeText(SignUp.this, "Connection Error!! Check Your Internet", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

        }
        else if(password.length()<8){
            Toast.makeText(this, "Password Needs Min 8 Characters ", Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(this, "Password Doesn't Match!! Try Again", Toast.LENGTH_SHORT).show();
        }
    }



    public void backtologinpage(View view){
        Intent intent = new Intent(SignUp.this,LogIn.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SignUp.this,LogIn.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {

       if(username.length()>0||email.length()>0||phone.length()>0||password.length()>0) {
           if (v.getId() == R.id.signup || v.getId() == R.id.signuplayout) {
               InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
               inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
           }
       }
    }


}
