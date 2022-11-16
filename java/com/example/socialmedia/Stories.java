package com.example.socialmedia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Stories extends AppCompatActivity {
/*ArrayList<String> picurl=new ArrayList<String>();
ArrayList<String> username=new ArrayList<String>();
ArrayList<String> email=new ArrayList<String>();
*/
ArrayList<ExampleItem> example=new ArrayList<>();
ImageView myprofile;
TextView myname;

FirebaseAuth mAuth;
    //recyclerView
    private RecyclerView mRecyclerView;
    private  RecyclerView.Adapter mAdapter;
    private  RecyclerView.LayoutManager mLayoutManager;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);

        //id
        myprofile=findViewById(R.id.stories_profile);
        myname=findViewById(R.id.stories_name);

        //take value from main activity
        myname.setText(MainActivity.username.getText());
      // myprofile.setImageBitmap(MainActivity.profile1.g);
try {
    BitmapDrawable drawable = (BitmapDrawable) MainActivity.profile2.getDrawable();
    Bitmap bitmap = drawable.getBitmap();
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
    byte[] byteArray = stream.toByteArray();
    Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    myprofile.setImageBitmap(bmp);
}catch (Exception e){}

        //firebase
        mAuth=FirebaseAuth.getInstance();

        //for animation
        final ConstraintLayout mylayout=findViewById(R.id.StoriesLayout);

        //Bottom Navigation View
        BottomNavigationView bottomNavigationView;
        bottomNavigationView=findViewById(R.id.bottomnavigation);
        Menu menu=bottomNavigationView.getMenu();
        MenuItem menuItem=menu.getItem(1); //check
        menuItem.setChecked(true); //highlight ke liye
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navprofile:{  Intent i=new Intent(Stories.this,MainActivity.class);
                        /*Pair[] pairs=new Pair[1];

                        pairs[0]=new Pair<View,String>(mylayout,"layouttransition");

                        ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(Stories.this, pairs);
                        startActivity(i,options.toBundle());*/

                        startActivity(i);
                        return true;

                    }
                    case R.id.navexplore:{
                      return true;
                    }
                    case R.id.navaddsnap:{
                        Intent i=new Intent(Stories.this,Addsnap.class);
                        startActivity(i);
                    }

                }
                return false;
                //return error
            }
        });

        //example item mein sab store karo
        FirebaseDatabase.getInstance().getReference().child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //khud ka alag se store karenge not in these arraylist
                if(dataSnapshot.child("email").getValue().toString().equals(mAuth.getCurrentUser().getEmail())) {}
                else{
                    //datashot are users now
                    if (dataSnapshot.child("profilepicurl").exists()) {
                        //ekdefault url do jiska nahi hai
                       example.add(new ExampleItem(dataSnapshot.child("username").getValue().toString(),dataSnapshot.child("email").getValue().toString(),dataSnapshot.child("profilepicurl").getValue().toString(),dataSnapshot.getKey().toString()));
                        mAdapter.notifyDataSetChanged();

                       /* picurl.add(dataSnapshot.child("profilepicurl").getValue().toString());
                        username.add(dataSnapshot.child("username").getValue().toString());
                        email.add(dataSnapshot.child("email").getValue().toString());
                      */
                    }
                    else {

                        //ekdefault url do jiska nahi hai
                        example.add(new ExampleItem(dataSnapshot.child("username").getValue().toString(),dataSnapshot.child("email").getValue().toString(),"defaultprofilepic",dataSnapshot.getKey().toString()));
                        mAdapter.notifyDataSetChanged();
                        /* picurl.add("defaultprofilepic");
                        username.add(daatSnapshot.child("username").getValue().toString());
                        email.add(dataSnapshot.child("email").getValue().toString());
                        */
                    }

                }
//************************************************************************
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

        //recyclerview implement
        //if(example.size()>0) { //wait karna hoga untill data aaye
           // mRecyclerView.setHasFixedSize(true);
            mRecyclerView = findViewById(R.id.recycler);
            mLayoutManager = new LinearLayoutManager(this);
            mAdapter = new ExampleAdapter(example);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(mLayoutManager);




    }
   //rest jo activity hai unpe
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(Stories.this,MainActivity.class);
        startActivity(i);

    }

    public void viewyoursnap(View view){

        Intent i=new Intent(Stories.this,Stories2.class);
      i.putExtra("fkey",mAuth.getCurrentUser().getUid());
      startActivity(i);

    }
}
