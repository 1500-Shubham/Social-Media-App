package com.example.socialmedia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

//main activity hi profile hai mera
public class MainActivity extends AppCompatActivity {
FirebaseAuth mAuth;

 String imagename; //for profile pic
 static ImageView profile1,profile2;
static TextView logout,updateprofile,username,email,phone;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //id
        profile1=findViewById(R.id.mainprofile1);
        profile2=findViewById(R.id.mainprofile2);
        logout=findViewById(R.id.mainlogout);
        updateprofile=findViewById(R.id.mainupdateprofile);
        username=findViewById(R.id.mainusername);
        phone=findViewById(R.id.mainphone);
        email=findViewById(R.id.mainemail);


        //Firebase
        mAuth=FirebaseAuth.getInstance();

        //FirebaseDatabaseSetValue
       FirebaseDatabase.getInstance().getReference().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                username.setText(dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("username").getValue().toString());
                email.setText("->"+dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("email").getValue().toString());
                phone.setText("->"+dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("phone").getValue().toString());
                //Toast.makeText(MainActivity.this,dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("profilepicname").getValue().toString(), Toast.LENGTH_SHORT).show();
                //use url and picasso
               if(dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("profilepicurl").exists()) {
                 //naya account pe picture name aur url nahi hoga baad mein add hoga
                   //starting mein dikkat ho sakta
                   Picasso.get().load(dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("profilepicurl").getValue().toString()).into(profile2);
                   Picasso.get().load(dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("profilepicurl").getValue().toString()).into(profile1);
               }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                   }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                //delete previous file                   //needed pehle ka data deleted profilepicname se
                if(dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("profilepicname").exists()) {
                    FirebaseStorage.getInstance().getReference().child("profilepics").child(dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("profilepicname").getValue().toString()).delete();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       //image UUID
        imagename= UUID.randomUUID().toString() +".jpg";

        //for animation
        final ConstraintLayout mylayout=findViewById(R.id.mainlayout);

        //Bottom Navigation View
        bottomNavigationView=findViewById(R.id.bottomnavigation);
        Menu menu=bottomNavigationView.getMenu();
        MenuItem menuItem=menu.getItem(0); //check
        menuItem.setChecked(true); //highlight ke liye
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navprofile:{//yahi pe ho
                        return true;
                    }
                    case R.id.navexplore:{
                     Intent i=new Intent(MainActivity.this,Stories.class);
                    /* Pair[] pairs=new Pair[1];

                     pairs[0]=new Pair<View,String>(mylayout,"layouttransition");

                     ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
                     startActivity(i,options.toBundle());*/
                    startActivity(i);
                     return true;
                    }
                    case R.id.navaddsnap:{
                        Intent i=new Intent(MainActivity.this,Addsnap.class);
                        startActivity(i);

                    }

                }
             return false;
             //return error
            }
        });


        }
    //logout
        @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MainActivity.this,LogIn.class);
        mAuth.signOut();
        startActivity(intent);
        finish();
    }
    public void logout(View view){
        logout.setPressed(true);
        Intent intent = new Intent(MainActivity.this,LogIn.class);
        mAuth.signOut();
        startActivity(intent);
        finish();
    }


    //updating profile pic
    public void updateprofile(View view){
        updateprofile.setPressed(true);

        if(checkSelfPermission((Manifest.permission.READ_EXTERNAL_STORAGE))!= PackageManager.PERMISSION_GRANTED){
            //is request ke baad hi function pe jayega permission wale
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else{
            getphoto();
        }



    }
    public  void getphoto(){ //abhi ghum ke aaye ho mobile folders se
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED&&grantResults.length>0){
                getphoto();}
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //data aa gaya
        Uri selectedimage = data.getData();
        if(requestCode==1&&resultCode==RESULT_OK&&data!=null){

            try{
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedimage);
                profile2.setImageBitmap(bitmap);
                profile1.setImageBitmap(bitmap);
                //afterchanging profile i will upload
                profilestorage();

            }
            catch (Exception e){
                Toast.makeText(this,"Try Again!!",Toast.LENGTH_SHORT).show();}
        }

    }

    public void profilestorage(){
    // Get the data from an ImageView as bytes
    profile2.setDrawingCacheEnabled(true);
    profile2.buildDrawingCache();
    Bitmap bitmap = ((BitmapDrawable) profile2.getDrawable()).getBitmap();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    byte[] data = baos.toByteArray();

    UploadTask uploadTask = FirebaseStorage.getInstance().getReference().child("profilepics").child(imagename).putBytes(data);
    uploadTask.addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception exception) {
            Toast.makeText(MainActivity.this,"Uploading Failed--CheckInternet",Toast.LENGTH_SHORT).show();
        }
    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
            // ...
            //update the database

           // *******$$$$ error aata Database Path kabhi uuid.jpg nahi ban sakta
            //isliye map use auto ek key bana dega uske andar jayega value

            FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("profilepicname").setValue(imagename);
              FirebaseStorage.getInstance().getReference().child("profilepics").child(imagename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                  @Override
                  public void onSuccess(Uri uri) {
                      FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("profilepicurl").setValue(uri.toString());

                  }
              });


        }
    });
}
}

