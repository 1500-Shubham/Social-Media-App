package com.example.socialmedia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Stories2 extends AppCompatActivity {
     String key;//key mil gaya jispe click kiye
     ArrayList<Stories2_exampleItme> example2=new ArrayList<Stories2_exampleItme>();
     ImageView nopost;
    FirebaseAuth mAuth;
    //recyclerView
    private RecyclerView mRecyclerView;
    private  RecyclerView.Adapter mAdapter;
    private  RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories2);
        //getting this user key
        key=getIntent().getStringExtra("fkey");
        Log.w("key",key);

        nopost=findViewById(R.id.stories2_nopost);
        if(example2.size()==0)
        {nopost.setVisibility(View.VISIBLE);}
        //firebase
        mAuth=FirebaseAuth.getInstance();

        //data nikalo database
        FirebaseDatabase.getInstance().getReference().child("users").child(key).child("snaps").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //datasnapshot are snaps number
                if(dataSnapshot.exists()) { //signin time snaps folder bana dena taki yeh work kare
                    example2.add(new Stories2_exampleItme(dataSnapshot.child("imagename").getValue().toString(), dataSnapshot.child("imageurl").getValue().toString(), dataSnapshot.child("caption").getValue().toString()));
                    mAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //recycler
        mRecyclerView = findViewById(R.id.stories2_recyclerview);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new Stories2_Adapter(example2);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }
}
