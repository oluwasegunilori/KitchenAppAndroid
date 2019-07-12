package com.example.changeme.kitchenapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.changeme.kitchenapp.Interface.ItemClickListener;
import com.example.changeme.kitchenapp.Model.Banner;
import com.example.changeme.kitchenapp.Model.Category;
import com.example.changeme.kitchenapp.Model.Token;
import com.example.changeme.kitchenapp.Model.User;
import com.example.changeme.kitchenapp.Model.Utils;
import com.example.changeme.kitchenapp.ViewHolder.MenuViewHolder;
import com.example.changeme.kitchenapp.common.Common;
import com.example.changeme.kitchenapp.database.Database;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

import static android.view.View.GONE;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseDatabase database;
    DatabaseReference category;
    ProgressBar progressBar2;
    TextView txtFullname;
    RecyclerView reycler_menu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;
    String namer;
    SwipeRefreshLayout swipeRefreshLayout;
    CounterFab fab;
    ProgressBar progressBar, pros;
    HashMap<String,String> imagelist;
    SliderLayout mSlider;
    RelativeLayout lin;
    User user2;
    private RelativeLayout errorlayout;
    private  TextView errormessage, errortitle;
    private ImageView errorimage;
    private Button retry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Food is Ready");
        Paper.init(this);


        setSupportActionBar(toolbar);
        progressBar = findViewById(R.id.progress_load_photohome);
        pros = findViewById(R.id.onforget);
        lin = findViewById(R.id.progress);
        progressBar2 = findViewById(R.id.progress_load_photohome);
        errorlayout = findViewById(R.id.errorlayout);
        errormessage = findViewById(R.id.errormessage);
        errortitle = findViewById(R.id.errortitle);
        errormessage = findViewById(R.id.errormessage);
        errorimage = findViewById(R.id.errorimage);
        retry = findViewById(R.id.retry);
        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Common.isConnectedToInternet(getBaseContext())) {
                    swipeRefreshLayout.setRefreshing(false);

                    loadMenu();

                } else {
                    Toast.makeText(Home.this, "Check Your internet Connection!!", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(true);
                    return;
                }

            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (Common.isConnectedToInternet(getBaseContext())) {

                    loadMenu();
                } else {
                    Toast.makeText(Home.this, "Check Your internet Connection!!", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(true);
                    return;
                }

            }
        });
        fab =  findViewById(R.id.fabo);



        if (Common.currentuser != null) {

            updateToken(FirebaseInstanceId.getInstance().getToken());
            loadmain();
            fab.setCount(new Database(this).getCartCount(Common.currentuser.getPhone()));

        } else {
            lin.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            progressBar2.setVisibility(View.GONE);

            String user = Paper.book().read(Common.USER_KEY);
            String pwd = Paper.book().read(Common.PWD_KEY);
            if(user !=null && pwd != null){
                if(!user.isEmpty() && !pwd.isEmpty()){
                    login(user,pwd);
                    loadmain();
                    setupSlider();
                }
            }
        }






        Paper.init(this);




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartintent = new Intent(Home.this, Cart.class);
                startActivity(cartintent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerview = navigationView.getHeaderView(0);
        txtFullname = (TextView) headerview.findViewById(R.id.textFullName);


        reycler_menu =  findViewById(R.id.recylcer_menu);
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {

            reycler_menu.setLayoutManager(new GridLayoutManager(this, 3));
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {

            reycler_menu.setLayoutManager(new GridLayoutManager(this, 2));
        }else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {

            reycler_menu.setLayoutManager(new GridLayoutManager(this, 3));
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {

            reycler_menu.setLayoutManager(new GridLayoutManager(this, 1));
        }
        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(reycler_menu.getContext(),
                R.anim.layout_fall_down);
        reycler_menu.setLayoutAnimation(animationController);


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void setupSlider() {

        mSlider = findViewById(R.id.slider);
        imagelist = new HashMap<>();
        final DatabaseReference banners = database.getReference("Banner");

        banners.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                        Banner banner = postSnapshot.getValue(Banner.class);

                        imagelist.put(banner.getName()+"@@@"+banner.getId(), banner.getImage());

                    }
                    for(String key:imagelist.keySet()){
                        String[] keySplit = key.split("@@@");
                        final String nameoffood = keySplit[0];
                        String idoffood = keySplit[1];
                        final TextSliderView textSliderView = new TextSliderView(getBaseContext());
                        textSliderView.description(nameoffood)
                                .image(imagelist.get(key))
                                .setScaleType(BaseSliderView.ScaleType.Fit)
                                .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                    @Override
                                    public void onSliderClick(BaseSliderView slider) {
                                        if(Common.isConnectedToInternet(getBaseContext())){

                                            if(!(nameoffood.contains("-"))) {


                                                Intent intent = new Intent(Home.this, FoodDetails.class);
                                        intent.putExtras(textSliderView.getBundle());
                                        startActivity(intent);

                                            }else{
                               Toast.makeText(Home.this, R.string.notready, Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                        else{
                                            Toast.makeText(Home.this, "No internet Connection", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        textSliderView.bundle(new Bundle());
                        textSliderView.getBundle().putString("FoodId",idoffood);
                mSlider.addSlider(textSliderView);
                banners.removeEventListener(this);


                    }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mSlider.setPresetTransformer(SliderLayout.Transformer.Background2Foreground);
        mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSlider.setCustomAnimation(new DescriptionAnimation());
        mSlider.setDuration(3500);

    }


    private void updateToken(String token) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference("Tokens");
        Token data = new Token(token, false);
        tokens.child(Common.currentuser.getPhone()).setValue(data);
    }

    private void loadMenu() {
        if (Common.isConnectedToInternet(getBaseContext())) {

        if (Common.currentuser != null) {
            errorlayout.setVisibility(GONE);
            progressBar2.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(true);

            adapter.startListening();
            reycler_menu.setAdapter(adapter);




            fab.setCount(new Database(this).getCartCount(Common.currentuser.getPhone()));
            reycler_menu.getAdapter().notifyDataSetChanged();
            reycler_menu.scheduleLayoutAnimation();
            setupSlider();

            swipeRefreshLayout.setRefreshing(false);



            txtFullname.setText(Common.currentuser.getName());


    lin.setVisibility(View.GONE);

        }

        } else {
            showErrorMessage(R.drawable.no_result,"Error", "Check your internet connection");
        }


 }

    @Override
    protected void onResume() {

        super.onResume();
        Paper.init(this);


        if(Common.currentuser==null)
        {
            lin.setVisibility(View.VISIBLE);
            String user = Paper.book().read(Common.USER_KEY);
            String pwd = Paper.book().read(Common.PWD_KEY);
            if(user !=null && pwd != null){
                if(!user.isEmpty() && !pwd.isEmpty()){
                    login(user,pwd);
                    loadmain();
                    setupSlider();
                }
            }

        }else{
            loadMenu();
        }




    }
    private void login(final String phone, final String pwd) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");
        if(Common.isConnectedToInternet(getBaseContext()) ){






            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    if(dataSnapshot.child(phone).exists())
                    {



                        User user = dataSnapshot.child(phone).getValue(User.class);
                        user.setPhone(phone);
                        if (user.getPassword().equals(pwd) && Boolean.parseBoolean(user.getIsStaff()) == false) {

                            Common.currentuser = user;
                            loadmain();
                            fab.setCount(new Database(getBaseContext()).getCartCount(Common.currentuser.getPhone()));
                            txtFullname.setText(Common.currentuser.getName());
                            lin.setVisibility(GONE);


                        } else {

                            Toast.makeText(Home.this, "Please Sign in Again !!!!!", Toast.LENGTH_SHORT ).show();
                            Intent inty = new Intent(Home.this, SignIn.class);
                            inty.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(inty);
                        }
                    }
                    else {
                        lin.setVisibility(GONE);
                        Toast.makeText(Home.this, "Please Sign in Again !!!!!", Toast.LENGTH_SHORT ).show();
                        Intent inty = new Intent(Home.this, SignIn.class);
                        inty.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(inty);
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else{
            showErrorMessage(R.drawable.no_result,"Error", "Check your internet connection");
            return;
        }

    }
    @Override
    protected void onStop() {

        super.onStop();
if(adapter!=null){
        adapter.stopListening();}
        if(mSlider!= null) {
            mSlider.stopAutoCycle();

        }

          }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            this.moveTaskToBack(true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if(item.getItemId() == R.id.menu_Search) {
            if(Common.isConnectedToInternet(getBaseContext())) {

                startActivity(new Intent(Home.this, SearchActivity.class));
            }
            else{
                showErrorMessage(R.drawable.no_result,"Error", "Check your internet connection");

            }



        }

            //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            // Handle the camera action
        }

            else if(id == R.id.nav_fav){
            if (Common.isConnectedToInternet(getBaseContext())) {

                Intent orderintent = new Intent(Home.this, FavouriteActivity.class);
                startActivity(orderintent);
            }
            else {
                showErrorMessage(R.drawable.no_result,"Error", "Check your internet connection");

            }
        }
        else if (id == R.id.nav_cart) {

            Intent orderintent  = new Intent(Home.this, Cart.class);
            startActivity(orderintent);
        } else if (id == R.id.nav_orders) {
            if (Common.isConnectedToInternet(getBaseContext())) {
                Intent orderintent = new Intent(Home.this, Orders.class);
                startActivity(orderintent);
            } else {
                showErrorMessage(R.drawable.no_result,"Error", "Check your internet connection");

            }

        }

        else if (id == R.id.nav_logout) {

            if (Common.isConnectedToInternet(getBaseContext())) {

            Paper.book().destroy();

            Intent soff = new Intent(Home.this, SignIn.class);
                soff.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(soff);}
            else {
                showErrorMessage(R.drawable.no_result,"Error", "Check your internet connection");

            }

        }
        else if(id == R.id.nav_change_password){

            if (Common.isConnectedToInternet(getBaseContext())) {

                showChangePasswordDialog();
            }
            else {
                Toast.makeText(Home.this, "Check Your internet Connection!!", Toast.LENGTH_SHORT).show();

            }
        }
        else if(id == R.id.nav_change_name){

            if (Common.isConnectedToInternet(getBaseContext())) {

                showChangeName();
            }
            else {
                showErrorMessage(R.drawable.no_result,"Error", "Check your internet connection");

            }

        }
        else if(id == R.id.nav_settingd){
            showSettingDialog();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showSettingDialog() {
        if(Common.isConnectedToInternet(getBaseContext())) {
            ContextThemeWrapper ctw = new ContextThemeWrapper(Home.this, R.style.MyDialogTheme);
            AlertDialog.Builder alertdialog = new AlertDialog.Builder(ctw);
            alertdialog.setTitle("Settings");

            LayoutInflater inflater = LayoutInflater.from(this);
            View layout_pwd = inflater.inflate(R.layout.settings_layout, null);
            final CheckBox ckb = layout_pwd.findViewById(R.id.ckb_checkbox);

            Paper.init(this);
            String issubscribe = Paper.book().read("sub_new");
            if (issubscribe == null || TextUtils.isEmpty(issubscribe) || issubscribe.equals("false")) {
                ckb.setChecked(false);

            } else {
                ckb.setChecked(true);
            }

            alertdialog.setView(layout_pwd);

            alertdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (ckb.isChecked()) {

                        FirebaseMessaging.getInstance().subscribeToTopic(Common.topic_name);
                        Paper.book().write("sub_new", "true");

                    } else {

                        FirebaseMessaging.getInstance().unsubscribeFromTopic(Common.topic_name);
                        Paper.book().write("sub_new", "false");


                    }

                }
            });
            alertdialog.show();


        }else{
            showErrorMessage(R.drawable.no_result,"Error", "Check your internet connection");

        }
    }

    private void showChangeName() {


        ContextThemeWrapper ctw = new ContextThemeWrapper(Home.this, R.style.MyDialogTheme);
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(ctw);
        alertdialog.setTitle("Change Name");
        alertdialog.setMessage("Please fill Information");

        LayoutInflater inflater = LayoutInflater.from(this);
        View layout_pwd = inflater.inflate(R.layout.change_name, null);
        final MaterialEditText chanename = layout_pwd.findViewById(R.id.changename);


        alertdialog.setView(layout_pwd);

        alertdialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Common.isConnectedToInternet(getBaseContext())) {
                   if(chanename.getText().toString().length() > 1){
                    final android.app.AlertDialog waitingdialog = new SpotsDialog(Home.this);
                       waitingdialog.setCancelable(false);

                       waitingdialog.show();

                    Map<String, Object> update_name = new HashMap<>();
                    update_name.put("name", chanename.getText().toString());
                    FirebaseDatabase.getInstance()
                            .getReference("User")
                            .child(Common.currentuser.getPhone())
                            .updateChildren(update_name)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    waitingdialog.dismiss();
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Home.this, "Name has been updated", Toast.LENGTH_SHORT).show();
                                        txtFullname.setText(Common.currentuser.getName());
                                    }
                                }
                            });


                }
                else {
                       Toast.makeText(Home.this, "Please Enter a Valid Name", Toast.LENGTH_SHORT).show();
                   }
                }else{
                    showErrorMessage(R.drawable.no_result,"Error", "Check your internet connection");

                }
            }
            });

        alertdialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertdialog.show();








    }

    private void showChangePasswordDialog() {
        errorlayout.setVisibility(View.GONE);
        ContextThemeWrapper ctw = new ContextThemeWrapper(Home.this, R.style.MyDialogTheme);
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(ctw);
        alertdialog.setTitle("Change Password");
        alertdialog.setMessage("Please fill all Information");

        LayoutInflater inflater = LayoutInflater.from(this);
        View layout_pwd = inflater.inflate(R.layout.changepassword, null);
        final MaterialEditText editpass = layout_pwd.findViewById(R.id.editpassword);
        final MaterialEditText editnewpass = layout_pwd.findViewById(R.id.editNewpaassowrd);
        final MaterialEditText editrepeatpass = layout_pwd.findViewById(R.id.editrepeatpassowrd);

        alertdialog.setView(layout_pwd);

        alertdialog.setPositiveButton("CHANGE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Common.isConnectedToInternet(getBaseContext())) {
                    final android.app.AlertDialog waitingdialog = new SpotsDialog(Home.this);
                    waitingdialog.show();
//check old password
                    if (editpass.getText().toString().equals(Common.currentuser.getPassword())) {

                        if (editnewpass.getText().toString().equals(editrepeatpass.getText().toString()) && editnewpass.getText().toString().length() > 1) {

                            Map<String, Object> passupdate = new HashMap<>();
                            passupdate.put("password", editnewpass.getText().toString());
                            DatabaseReference user = FirebaseDatabase.getInstance().getReference("User");
                            user.child(Common.currentuser.getPhone()).
                                    updateChildren(passupdate)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            waitingdialog.dismiss();
                                            Toast.makeText(Home.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Home.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    });


                        } else {
                            waitingdialog.dismiss();
                            Toast.makeText(Home.this, "New Password doesn't match or password is empty", Toast.LENGTH_SHORT).show();

                        }

                    } else {

                        waitingdialog.dismiss();
                        Toast.makeText(Home.this, "Wrong Old Password", Toast.LENGTH_SHORT).show();

                    }

                }
            else {

                    showErrorMessage(R.drawable.no_result,"Error", "Check your internet connection");

                }
            }

            });


        alertdialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
            }
        });
        alertdialog.show();

    }
    private  void showErrorMessage(int imageview, String title, String message){
        if(errorlayout.getVisibility()== GONE){
            errorlayout.setVisibility(View.VISIBLE);
        }
        errorimage.setImageResource(imageview);
        errortitle.setText(title);
        errormessage.setText(message);

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.isConnectedToInternet(getBaseContext()))
                recreate();
                            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();

    }
    public void loadmain(){
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");

        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(category, Category.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MenuViewHolder viewHolder, int position, @NonNull Category model) {

                progressBar.setVisibility(GONE);
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(Utils.getRandomDrawbleColor());
                requestOptions.error(Utils.getRandomDrawbleColor());
                requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
                requestOptions.centerCrop();
                requestOptions.timeout(100000);

                Glide.with(getBaseContext())
                        .load(model.getImage())
                        .apply(requestOptions)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                viewHolder.progressBar.setVisibility(GONE);

                                Intent soff = new Intent(Home.this, MainActivity.class);
                                soff.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(soff);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                viewHolder.progressBar.setVisibility(GONE);

                                return false;
                            }
                        })
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(viewHolder.imageView);

                viewHolder.textmenuName.setText(model.getName());


                final Category clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        if (Common.isConnectedToInternet(getBaseContext())) {

                            Intent intent = new Intent(Home.this, FoodList.class);
                            intent.putExtra("CategoryID", adapter.getRef(position).getKey());
                            startActivity(intent);
                        } else {
                            showErrorMessage(R.drawable.no_result, "Error", "Check your internet connection");

                        }


                    }
                });
            }

            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.menu_item, parent, false);
                return new MenuViewHolder(itemView);

            }
        };


    }
    public  class loadBackground extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
           loadMenu();


            return null;



        }
    }

}

