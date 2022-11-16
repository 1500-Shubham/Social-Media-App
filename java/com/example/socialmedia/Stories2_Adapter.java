package com.example.socialmedia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Stories2_Adapter extends RecyclerView.Adapter<Stories2_Adapter.ExampleViewHolder> {
private ArrayList<Stories2_exampleItme> mexampleItems;

Stories2_Adapter(ArrayList<Stories2_exampleItme> mexample){
    mexampleItems=mexample;
}
    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.examplelist_2, parent, false);
        ExampleViewHolder evh=new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
    final Stories2_exampleItme current=mexampleItems.get(position);
    Picasso.get().load(current.getImageurl()).into(holder.snaps);
    holder.caption.setText(current.getCaption());
    }

    @Override
    public int getItemCount() {
        return mexampleItems.size();
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
       public ImageView snaps;
       public TextView caption;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            snaps=itemView.findViewById(R.id.stories2exa_snaps);
            caption=itemView.findViewById(R.id.stories2exa_message);
        }
    }
}
