package com.example.mynotesappsqllite;

import android.view.View;

public class CustomkOnClickListener implements View.OnClickListener {

    private int position;
    private OnItemClickCallback onItemClickCallback;

    public CustomkOnClickListener(int position, OnItemClickCallback onItemClickCallback) {
        this.position = position;
        this.onItemClickCallback = onItemClickCallback;
    }

    @Override
    public void onClick(View view) {
        onItemClickCallback.onItemClicked(view, position);
    }

    public interface OnItemClickCallback{
        void onItemClicked(View view, int position);
    }
}
