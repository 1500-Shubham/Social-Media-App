package com.example.socialmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class chat extends AppCompatActivity {
Button send;
EditText msg;
String key;
FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        msg=findViewById(R.id.chat_message);
        send=findViewById(R.id.chat_sendbut);

        //getting jisse chat key
        key=getIntent().getStringExtra("fkey");
        Log.w("key",key);

        //firebase
        mAuth=FirebaseAuth.getInstance();
    }

    public void sendmsg(View view){
    //pehle un id ko msg send kar do
    //fir recycler implement kar dena

    //folder ban gaya jisse bhej rahe snap use for unique code
        Map<String,String> info = new HashMap<>();
        info.put("sender",mAuth.getCurrentUser().getUid());
        info.put("receiver",key);
        info.put("message",msg.getText().toString());
    FirebaseDatabase.getInstance().getReference().child("users").child(key).child("messages").push().setValue(info);

    }
}
