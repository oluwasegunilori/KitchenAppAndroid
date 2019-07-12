package com.example.changeme.kitchenapp.ViewHolder;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.changeme.kitchenapp.Cart;
import com.example.changeme.kitchenapp.Model.Order;
import com.example.changeme.kitchenapp.R;
import com.example.changeme.kitchenapp.common.Common;
import com.example.changeme.kitchenapp.database.Database;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by SHEGZ on 1/1/2018.
 */





public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>{
    private List<Order>  listData = new ArrayList<>();
    private Cart cart;

    public  CartAdapter(List<Order> listData, Cart cart){
        this.listData = listData;
        this.cart = cart;
    }


    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(cart);
        View itemView = inflater.inflate(R.layout.cart_layout, parent, false);
        return  new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, final int position) {

//        TextDrawable drawable = TextDrawable.builder().buildRound("" +listData.get(position).getQuantity(), Color.RED);


        //holder.img_cartCount.setImageDrawable(drawable);

        holder.img_cartCount.setNumber(listData.get(position).getQuantity());
        holder.img_cartCount.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Order order = listData.get(position);
                order.setQuantity(String.valueOf(newValue));
                new Database(cart).updateCart(order);


                BigDecimal total = BigDecimal.ZERO;
                BigDecimal help = BigDecimal.ONE;
                List<Order> orders = new Database(cart).getCart(Common.currentuser.getPhone());

                for(Order item:orders) {
                    help =  BigDecimal.valueOf(Double.parseDouble(item.getPrice()));
                    help = help.multiply(BigDecimal.valueOf(Double.parseDouble(item.getQuantity())));
                    total = total.add(help);
                    Locale locale = new Locale("en", "US");
                    NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);


                    total.setScale(2, RoundingMode.UNNECESSARY);

                    cart.totalprice.setText("Total: "+"₦" + String.valueOf(total));
                }

                }
        });
        Locale locale = new Locale("en", "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt(listData.get(position).getPrice()))*(Integer.parseInt(listData.get(position).getQuantity()));
        holder.txt_price.setText("");
        holder.txt_cartname.setText(listData.get(position).getProductname());


    }




    @Override
    public int getItemCount() {
        return listData.size();
    }

    public Order getItem(int position){
        return listData.get(position);
    }

    public void removeItem(int position){
    listData.remove(position);
    notifyItemRemoved(position);

    }

    public void restoreItem(Order item,int position){
        listData.add(position,item);
        notifyItemInserted(position);

    }

}
