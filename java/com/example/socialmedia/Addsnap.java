package com.example.socialmedia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Addsnap extends AppCompatActivity {
Button upload,confirm;
ImageView mysnap;
FirebaseAuth mAuth;
EditText caption;
String imagename; //for snaps naming
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addsnap);

        //id
        upload=findViewById(R.id.addsnap_upload);
        confirm=findViewById(R.id.addsnap_confirm);
        confirm.setEnabled(false);//jaise upload karoge waise snapstorage mein true kar dunga
        mysnap=findViewById(R.id.addsnap_image);
        caption=findViewById(R.id.addsnap_caption);

        //imagename
        imagename= UUID.randomUUID().toString()+".jpg";
        //firebase
        mAuth=FirebaseAuth.getInstance();
        //Bottom Navigation View
        BottomNavigationView bottomNavigationView;
        bottomNavigationView=findViewById(R.id.bottomnavigation);
        Menu menu=bottomNavigationView.getMenu();
        MenuItem menuItem=menu.getItem(2); //check
        menuItem.setChecked(true); //highlight ke liye
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navprofile:{  Intent i=new Intent(Addsnap.this,MainActivity.class);
                        /*Pair[] pairs=new Pair[1];

                        pairs[0]=new Pair<View,String>(mylayout,"layouttransition");

                        ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(Stories.this, pairs);
                        startActivity(i,options.toBundle());*/

                        startActivity(i);
                        return true;

                    }
                    case R.id.navexplore:{
                        Intent i=new Intent(Addsnap.this,Stories.class);
                        startActivity(i);
                        return true;
                    }
                    case R.id.navaddsnap:{
                        return true;
                    }

                }
                return false;
                //return error
            }
        });

    }

    //updating profile pic
    public void uploadfromphone(View view){
        upload.setPressed(true);

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
                mysnap.setImageBitmap(bitmap);
                //afterchanging profile i will upload
                 confirm.setEnabled(true);
            }
            catch (Exception e){
                Toast.makeText(this,"Try Again!!",Toast.LENGTH_SHORT).show();}
        }

    }

    public void snapstorage(View view){
        confirm.setEnabled(false);
        mysnap.setDrawingCacheEnabled(true);
        mysnap.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) mysnap.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = FirebaseStorage.getInstance().getReference().child("snaps").child(imagename).putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(Addsnap.this,"Uploading Failed--CheckInternet",Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                //update the database
                FirebaseStorage.getInstance().getReference().child("snaps").child(imagename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                    public void onSuccess(Uri uri) {
                            Map<String,String> snapmap = new HashMap<>();
                            snapmap.put("imagename",imagename);
                            snapmap.put("imageurl",uri.toString());
                            snapmap.put("caption",caption.getText().toString());

                            FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("snaps").push().setValue(snapmap);
                           //snaps ke baad apne aap ek key milega
                            Toast.makeText(Addsnap.this,"Uploaded Successfully",Toast.LENGTH_SHORT).show();
                        }
                });


            }
        });
    }
}
