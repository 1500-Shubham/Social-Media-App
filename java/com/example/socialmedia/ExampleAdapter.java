package com.example.socialmedia;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
private ArrayList<ExampleItem> mexampleItems;
    //constructor to get data from main to exampleadapter
    public ExampleAdapter(ArrayList<ExampleItem> example)
    {mexampleItems=example;}

    // saara value constructor call pe mil jayega

    public static class ExampleViewHolder extends RecyclerView.ViewHolder{
    public ImageView mimageView,mcheckbox;  //for setting image
    public TextView mTextView1,mTextView2;

    public ExampleViewHolder(@NonNull View itemView) {//yeh itemView layout hai uske elements combine
            super(itemView); //View type hai
            mimageView=itemView.findViewById(R.id.circleImageView);
            mTextView1=itemView.findViewById(R.id.textView);
            mTextView2=itemView.findViewById(R.id.textView2);
            mcheckbox=itemView.findViewById(R.id.stories_checkbox);
        }
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.example_list, parent, false);
        //call view holder and pass the view
        ExampleViewHolder evh=new ExampleViewHolder(v);
    return evh;}

    @Override //yeh upar ka evh mila niche and v gaya upar ab dono ko link
    //position 0 1 2 type hota by default toh array mein use kar sakte
    public void onBindViewHolder(@NonNull final ExampleViewHolder holder, int position) {
final ExampleItem current=mexampleItems.get(position);
Picasso.get().load(current.getMprofileurl()).into(holder.mimageView);
        //image after converting
holder.mTextView1.setText(current.getMusername());
holder.mTextView2.setText(current.getMemail());

holder.mcheckbox.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
     holder.mcheckbox.setPressed(true);
     Intent i=new Intent(v.getContext(),chat.class);
     i.putExtra("fkey",current.getMkey());
     v.getContext().startActivity(i);

    }
});
    }

    @Override
    public int getItemCount() {
        return mexampleItems.size();
    }


}
