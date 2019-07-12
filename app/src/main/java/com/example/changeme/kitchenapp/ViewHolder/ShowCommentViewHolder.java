package com.example.changeme.kitchenapp.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.changeme.kitchenapp.R;

public class ShowCommentViewHolder extends RecyclerView.ViewHolder {

    public TextView txtUser, txtComment;
    public RatingBar ratingBar;
    public ShowCommentViewHolder(View itemView) {
        super(itemView);

        txtComment  = itemView.findViewById(R.id.textComment);
        txtUser  = itemView.findViewById(R.id.textUserPhone);
        ratingBar  = itemView.findViewById(R.id.ratevar);

    }
}
