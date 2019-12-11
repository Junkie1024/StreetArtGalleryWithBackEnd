package com.example.streetart;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder>  {




    private ArrayList<categorydata> categorydataArrayList;
    private Context context;
    private View.OnClickListener RecycleAdapterListener;


    public RecycleAdapter(ArrayList<categorydata> categorydataArrayList, Context context) {
        this.categorydataArrayList = categorydataArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecycleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_items, parent, false);

        return new RecycleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.categoryName.setText("Category Name: "+categorydataArrayList.get(position).getCategoryName());
        holder.numberOfArt.setText("Number Of Art: "+categorydataArrayList.get(position).getNumberOfArt());

    }

    public void setOnClickListener(View.OnClickListener clickListener) {

        RecycleAdapterListener = clickListener;

    }

    @Override
    public int getItemCount() {
        return categorydataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView categoryName,numberOfArt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.cat_name);
            numberOfArt = itemView.findViewById(R.id.noOfart);

            itemView.setTag(this);

            itemView.setOnClickListener(RecycleAdapterListener);

        }
    }






}
