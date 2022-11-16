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

public class LogIn extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;
    EditText email,password;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        //Views
        email=findViewById(R.id.loginemail);
        password=findViewById(R.id.loginpassword);
        login=findViewById(R.id.logingetstarted);
        //Firebase
        mAuth = FirebaseAuth.getInstance();
      if(mAuth.getCurrentUser()!=null){
          login();
      }
    }
   public void login(){
    Intent intent = new Intent(LogIn.this,MainActivity.class);
    startActivity(intent);
    finish();
   } //going to main activity

   public void logingetstarted(View view){
       login.setPressed(true);
       if(email.length()!=0&&password.length()!=0) {
           mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                   .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()) {
                               // Sign in success, update UI with the signed-in user's information
                               login();
                           } else {
                               Toast.makeText(LogIn.this, "Have You SignedUp!!", Toast.LENGTH_SHORT).show();
                           }

                           // ...
                       }
                   });
       }
       else{
           Toast.makeText(LogIn.this, "FILL THE DETAILS!!", Toast.LENGTH_SHORT).show();

       }
   }

   public void login_signup(View  view){
       Intent intent = new Intent(LogIn.this,SignUp.class);
       startActivity(intent);
       finish();
   }


    @Override
    public void onClick(View v) {
        if (email.length() > 0||password.length()>0) {
            if (v.getId() == R.id.login || v.getId() == R.id.loginlayout) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }
    }
}
