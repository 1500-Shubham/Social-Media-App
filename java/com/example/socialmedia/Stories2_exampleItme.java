package com.example.socialmedia;

public class Stories2_exampleItme {
    private String imagename,imageurl,caption;

    Stories2_exampleItme(String text1,String text2,String text3){
        imagename=text1;
        imageurl=text2;
        caption=text3;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getImagename() {
        return imagename;
    }


    public String getCaption() {
        return caption;
    }


}
