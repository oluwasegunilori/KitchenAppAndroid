package com.example.changeme.kitchenapp.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.changeme.kitchenapp.Model.Order;
import com.example.changeme.kitchenapp.R;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Order> implements View.OnClickListener{

    Context mContext;
    private ArrayList<Order> dataSet;
    private int lastPosition = -1;

    public CustomAdapter(ArrayList<Order> data, Context context) {
        super(context, R.layout.list_products, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Order dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_products, parent, false);
            viewHolder.p_name = (TextView) convertView.findViewById(R.id.productname);
            viewHolder.quantity = (TextView) convertView.findViewById(R.id.quantity);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        viewHolder.p_name.setText(dataModel.getProductname());
        viewHolder.quantity.setText(dataModel.getQuantity());
        viewHolder.price.setText(String.valueOf(Integer.parseInt(dataModel.getPrice()) * Integer.parseInt(dataModel.getQuantity())));
        // Return the completed view to render on screen
        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView p_name;
        TextView quantity;
        TextView price;
    }
}