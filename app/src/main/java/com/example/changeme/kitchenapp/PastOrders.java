package com.example.changeme.kitchenapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.changeme.kitchenapp.Model.Order;
import com.example.changeme.kitchenapp.Model.Request;
import com.example.changeme.kitchenapp.Model.Utils;
import com.example.changeme.kitchenapp.ViewHolder.OrderDetailsViewHolder;
import com.example.changeme.kitchenapp.common.Common;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PastOrders.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PastOrders#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PastOrders extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public  String myphone;
    ProgressBar progressBar;
    FirebaseDatabase database;
    DatabaseReference requests;
    String [] [] datare;
    String [] headers = {"Product", "Quantity", "Price"};
    FirebaseRecyclerAdapter<Request, OrderDetailsViewHolder> adapter;
    RecyclerView loadOrders;
    RecyclerView.LayoutManager layoutManager;
    private RelativeLayout errorlayout;
    private TextView errormessage, errortitle;
    private ImageView errorimage;
    private Button retry;
    private View rootView;

    private Context c;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PastOrders() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PastOrders.
     */
    // TODO: Rename and change types and number of parameters
    public static PastOrders newInstance(String param1, String param2) {
        PastOrders fragment = new PastOrders();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView =  inflater.inflate(R.layout.fragment_ongoing_orders, container, false);
        c = getContext();
        loadOrders = rootView.findViewById(R.id.listorders);
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");
        // FloatingActionButton     fcallle = (FloatingActionButton) findViewById(R.id.callere);
        progressBar = rootView.findViewById(R.id.progress_load_photoorders);
        errorlayout = rootView.findViewById(R.id.errorlayout);
        errormessage = rootView.findViewById(R.id.errormessage);
        errortitle = rootView.findViewById(R.id.errortitle);
        errormessage = rootView.findViewById(R.id.errormessage);
        errorimage = rootView.findViewById(R.id.errorimage);
        retry = rootView.findViewById(R.id.retry);
        loadOrders.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(c);
        if(Common.currentuser !=null) {
            myphone = Common.currentuser.getPhone();
        }else{
            Intent rt= new Intent(c, MainActivity.class);
            rt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(rt);

        }
        loadOrders.setLayoutManager(layoutManager);
        loadOrders(myphone);




        return  rootView;
    }
    private void loadOrders(String phone) {


        if (Common.isConnectedToInternet(getContext())) {



            final Query orderVyUser = requests.orderByChild("phoneplusstatus").equalTo(phone+"_2");



            orderVyUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                                           @Override
                                                           public void onDataChange(DataSnapshot dataSnapshot) {
                                                               if (dataSnapshot.exists()) {

                                                                   FirebaseRecyclerOptions<Request> foodoptions = new FirebaseRecyclerOptions.Builder<Request>()
                                                                           .setQuery(orderVyUser, Request.class)
                                                                           .build();
                                                                   adapter = new FirebaseRecyclerAdapter<Request, OrderDetailsViewHolder>(foodoptions) {
                                                                       @Override
                                                                       protected void onBindViewHolder(@NonNull OrderDetailsViewHolder viewHolder, final int position, @NonNull final Request model) {

                                                                           progressBar.setVisibility(View.GONE);



                                                                           viewHolder.buts.removeAllViews();
                                                                           String nametext="Food\n",pricetext="Price\n", quantity_text="Qty\n";
                                                                           for(int i = 0; i< model.getLifes().size(); i++) {
                                                                               Order or = model.getLifes().get(i);
                                                                               nametext = nametext+or.getProductname()+"\n";
                                                                               pricetext = pricetext+String.valueOf(Integer.parseInt(or.getPrice()) * Integer.parseInt(or.getQuantity()))+ "\n";
                                                                               quantity_text = quantity_text+or.getQuantity()+"\n";
                                                                           }
                                                                           viewHolder.p_name.setText(nametext);

                                                                           viewHolder.quant.setText(quantity_text);
                                                                           viewHolder.price.setText(pricetext);
                                                                           viewHolder.buts.addView(viewHolder.p_name);
                                                                           viewHolder.buts.addView(viewHolder.quant);
                                                                           viewHolder.buts.addView(viewHolder.price);

                                                                           viewHolder.order_id.setText("Order ID - " + adapter.getRef(position).getKey());

                                                                           viewHolder.order_status.setText(convertCodeToStatus(model.getStatus()));
                                                                           viewHolder.order_total.setText(model.getTotal());
                                                                           viewHolder.order_time.setText(Utils.DateToTimeFormat(model.getTime()));
                                                                           viewHolder.track.setVisibility(View.GONE);
                                                                           viewHolder.cancel.setVisibility(View.GONE);



                                                                           //   viewHolder.tableLayout.setHeaderAdapter(new SimpleTableHeaderAdapter(getContext(), headers));

/**                    CustomAdapter customAdapter;
 ArrayList<Order> listOfStrings = new ArrayList<Order>(model.getLifes().size());
 listOfStrings.addAll(model.getLifes());
 customAdapter = new CustomAdapter( listOfStrings, getContext());

 viewHolder.buts.setAdapter(customAdapter);

 viewHolder.buts.removeAllViews();
 String nametext="",pricetext="", quantity_text="";
 for(int i = 0; i< model.getLifes().size(); i++) {
 Order or = model.getLifes().get(i);
 nametext = nametext+or.getProductname()+"\n";
 pricetext = pricetext+String.valueOf(Integer.parseInt(or.getPrice()) * Integer.parseInt(or.getQuantity()))+ "\n";
 quantity_text = quantity_text+or.getQuantity()+"\n";
 }
 viewHolder.p_name.setText(nametext);
 viewHolder.quant.setText(quantity_text);
 viewHolder.price.setText(pricetext);

 viewHolder.buts.removeAllViews();
 for(int i = 0; i< model.getLifes().size(); i++) {
 final View addView = getLayoutInflater().inflate(
 R.layout.activity_order_details, null);
 Order or = model.getLifes().get(i);

 TextView goods_name = new TextView(getContext());
 TextView goods_quantoty = new TextView(getContext());
 TextView goods_price = new TextView(getContext());

 ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 6F);
 goods_name.setLayoutParams(params);
 ViewGroup.LayoutParams params2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2F);
 goods_quantoty.setLayoutParams(params);
 ViewGroup.LayoutParams params3 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2F);
 goods_price.setLayoutParams(params);
 goods_name.setText(or.getProductname());
 goods_quantoty.setText(or.getQuantity());
 goods_price.setText(String.valueOf(Integer.parseInt(or.getPrice()) * Integer.parseInt(or.getQuantity())));


 viewHolder.buts.addView(goods_name);
 viewHolder.buts.addView(goods_quantoty);
 viewHolder.buts.addView(goods_price);

 }

 viewHolder.order_id.setText("Order ID - " + adapter.getRef(position).getKey());

 viewHolder.order_status.setText(convertCodeToStatus(model.getStatus()));
 viewHolder.order_total.setText(model.getTotal());
 viewHolder.order_time.setText(Utils.DateToTimeFormat(model.getTime()));

 viewHolder.cancel.setVisibility(View.GONE);
 viewHolder.track.setVisibility(View.GONE);

 /**      datare = new String[model.getLifes().size()][3];
 for(int i = 0; i< model.getLifes().size(); i++){
 Order or = model.getLifes().get(i);
 datare[i][0]= or.getProductname();
 datare[i][1]= or.getQuantity();
 datare[i][2]= String.valueOf(Integer.parseInt(or.getPrice()) * Integer.parseInt(or.getQuantity()));

 }

 // Gets the layout params that will allow you to resize the layout

 viewHolder.tableLayout.setColumnWeight(0,6);
 viewHolder.tableLayout.setColumnWeight(1, 2);
 viewHolder.tableLayout.setColumnWeight(2,2);

 viewHolder.tableLayout.setDataAdapter(new SimpleTableDataAdapter(getContext(),datare));

 **/
                                                                       }



                                                                       @NonNull
                                                                       @Override
                                                                       public OrderDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                                                                           View itemView = LayoutInflater.from(parent.getContext())
                                                                                   .inflate(R.layout.activity_order_details,parent,false);
                                                                           return new OrderDetailsViewHolder(itemView,getContext());

                                                                       }
                                                                       @Override
                                                                       public Request getItem(int position) {
                                                                           return super.getItem(getItemCount() - 1 - position);
                                                                       }


                                                                       @Override
                                                                       public void onDataChanged() {
                                                                           loadOrders.removeAllViews();
                                                                           super.onDataChanged();
                                                                       }


                                                                   };
                                                                   adapter.startListening();
                                                                   loadOrders.setAdapter(adapter);


                                                               } else {

                progressBar.setVisibility(View.GONE);
                                                                   Toast.makeText(getActivity(), "No Orders", Toast.LENGTH_LONG).show();
                                                               }
                                                           }

                                                           @Override
                                                           public void onCancelled(DatabaseError databaseError) {

                                                           }
                                                       });


        }else {
            showErrorMessage(R.drawable.no_result,"Error", "Check your internet connection");


        }
    }
    private void showErrorMessage(int imageview, String title, String message) {
        if (errorlayout.getVisibility() == View.GONE) {
            errorlayout.setVisibility(View.VISIBLE);
        }
        errorimage.setImageResource(imageview);
        errortitle.setText(title);
        errormessage.setText(message);

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadOrders(myphone);
            }
        });
    }
    private String convertCodeToStatus(String status) {
        if(status.equals("0"))
            return "Order Status - Placed";
        else if(status.equals("1"))
            return "Order Status - Shipped ";
        else
            return "Order Status - Delivered";

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
