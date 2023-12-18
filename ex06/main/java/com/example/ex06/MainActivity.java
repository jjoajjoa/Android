package com.example.ex06;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    LinearLayout drawerView;
    TabLayout tab;
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        drawerView = findViewById(R.id.drawerView);
        tab = findViewById(R.id.tab);
        pager = findViewById(R.id.pager);

        getSupportActionBar().setTitle("Naver Search");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_menu_24);

        tab.addTab(tab.newTab().setText("Movie"));
        tab.getTabAt(0).setIcon(R.drawable.baseline_local_movies_24);
        tab.addTab(tab.newTab().setText("Shop"));
        tab.getTabAt(1).setIcon(R.drawable.baseline_shopping_cart_24);
        tab.addTab(tab.newTab().setText("Book"));
        tab.getTabAt(2).setIcon(R.drawable.baseline_menu_book_24);

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));    //페이저가 바뀔때 탭 변경

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {    //탭이 바뀔때 페이저 변경
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });//addOnTabSelectedListener

    }//onCreate

    class PagerAdapter extends FragmentPagerAdapter {    //프래그먼트어답타

        public PagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MovieFragment();
                case 1:
                    return new ShopFragment();
                case 2:
                    return new BookFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }//PagerAdapter

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(drawerView)) {
                drawerLayout.close();
            } else {
                drawerLayout.openDrawer(drawerView);
            }
        }
        return super.onOptionsItemSelected(item);
    }//onOptionsItemSelected

}//MainActivity