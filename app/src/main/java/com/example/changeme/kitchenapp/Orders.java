package com.example.changeme.kitchenapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.changeme.kitchenapp.common.Common;

public class Orders extends AppCompatActivity {

    TabItem ongoing, past, newones;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("My Orders");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent irt = new Intent(Orders.this, SearchActivity.class);
                startActivity(irt);

            }
        });

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        if(Common.currentuser==null){
            Intent inte = new Intent(Orders.this, MainActivity.class);
            inte.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


            startActivity(inte);

        }
                final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
                ongoing = findViewById(R.id.ongoing);
                newones = findViewById(R.id.newones);
        mViewPager = (ViewPager) findViewById(R.id.container);
                past = findViewById(R.id.past);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());


        // Set up the ViewPager with the sections adapter.

        mViewPager.setAdapter(mSectionsPagerAdapter);


        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override

            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(Orders.this, R.color.colorPrimary));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(Orders.this, R.color.colorPrimary));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(Orders.this, R.color.colorPrimaryDark));
                    }
                } else if (tab.getPosition() == 1) {
                        toolbar.setBackgroundColor(ContextCompat.getColor(Orders.this, R.color.colorPrimaryDark));
                        tabLayout.setBackgroundColor(ContextCompat.getColor(Orders.this, R.color.colorPrimaryDark));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(ContextCompat.getColor(Orders.this, R.color.colorPrimaryDark));
                        }
                    }
                }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(Orders.this, R.color.colorPrimary));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(Orders.this, R.color.colorPrimary));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(Orders.this, R.color.colorPrimaryDark));
                    }
                }  else if (tab.getPosition() == 1) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(Orders.this, R.color.colorPrimaryDark));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(Orders.this, R.color.colorPrimaryDark));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(Orders.this, R.color.colorPrimaryDark));
                    }
                }
            }



            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                if (tab.getPosition() == 0) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(Orders.this, R.color.colorPrimary));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(Orders.this, R.color.colorPrimary));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(Orders.this, R.color.colorPrimaryDark));
                    }
                } else if (tab.getPosition() == 1) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(Orders.this, android.R.color.darker_gray));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(Orders.this, android.R.color.darker_gray));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(Orders.this, android.R.color.darker_gray));
                    }
                } else if (tab.getPosition() == 1) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(Orders.this, R.color.colorPrimaryDark));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(Orders.this, R.color.colorPrimaryDark));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(Orders.this, R.color.colorPrimaryDark));
                    }
                }
            }

        });


    }

    @Override
    protected void onResume() {
      super.onResume();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent soff = new Intent(Orders.this, Home.class);
        soff.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(soff);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private int noOfTabs;
        public SectionsPagerAdapter(FragmentManager fm, int noOfTabs) {
            super(fm);
            this.noOfTabs = noOfTabs;


        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0:
                    return new NewOrders();
                case 1:
                    return new OngoingOrders();
                    case 2 :
                        return new PastOrders();
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return noOfTabs;
        }
    }
}
