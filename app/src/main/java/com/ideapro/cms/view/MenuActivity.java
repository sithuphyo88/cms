package com.ideapro.cms.view;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.ideapro.cms.R;


public class MenuActivity extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_project:
                        updateDisplay(new ProjectListFragment());
                        break;

                    case R.id.navigation_item_images:
                        updateDisplay(new ProjectListFragment());
                        break;

                    case R.id.navigation_item_location:
                        updateDisplay(new ProjectListFragment());
                        break;

                    case R.id.navigation_sub_item_01:
                        Toast.makeText(MenuActivity.this, "Navigation Sub Item 01 Clicked", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.navigation_sub_item_02:
                        Toast.makeText(MenuActivity.this, "Navigation Sub Item 02 Clicked", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        navigationView.setCheckedItem(R.id.navigation_item_project);
        navigationView.getMenu().performIdentifierAction(R.id.navigation_item_project, 0);
    }

    private void updateDisplay(Fragment fragment) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
