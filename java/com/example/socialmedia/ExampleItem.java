package com.example.socialmedia;

public  class ExampleItem {
    //saara data store yaha karoge

    private String musername, memail,mprofileurl,mkey;

    ExampleItem(String text1, String text2,String text3,String keyusers)
    {   mprofileurl=text3;
        memail =text2;
        musername=text1;
        mkey=keyusers;

    }

    //proper way se store kar liye
    //when we need data
    //use getter
    //setter we will pass value

    public String getMprofileurl() {return mprofileurl;}
    public String getMusername(){return musername;}
    public String getMemail() { return memail; }
    public String getMkey() { return mkey; }

}

