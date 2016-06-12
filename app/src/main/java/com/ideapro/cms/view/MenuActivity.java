package com.ideapro.cms.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.ideapro.cms.R;
import com.ideapro.cms.utils.CommonUtils;


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

                    case R.id.navigation_item_customer:
                        updateDisplay(new CustomerListFragment());
                        break;

                    case R.id.navigation_item_subContractor:
                        updateDisplay(new SubContractorListFragment());
                        break;

                    case R.id.navigation_item_user:
                        updateDisplay(new UserListFragment());
                        break;

                    case R.id.navigation_item_role:
                        updateDisplay(new RoleListFragment());
                        break;

                    case R.id.navigation_item_material_category:
                        updateDisplay(new MaterialCategoryListFragment());
                        break;

                    case R.id.navigation_item_settings:
                        updateDisplay(new SettingsFragment());
                        break;

                    case R.id.navigation_item_about_us:
                        updateDisplay(new AboutFragment());
                        break;

                    case R.id.navigation_item_exit:
                        CommonUtils.showConfirmDialogBox(MenuActivity.this, getString(R.string.message_confirmation_exit),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finishAffinity();
                                    }
                                },
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        return;
                                    }
                                });
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
        FragmentTransaction ftr = fragmentManager.beginTransaction();
        ftr.replace(R.id.frame_container, fragment);
        ftr.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ftr.addToBackStack(null);
        ftr.commit();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            Fragment currentFragment = CommonUtils.getVisibleFragment(getSupportFragmentManager());
            if(currentFragment instanceof ProjectListFragment) {
                CommonUtils.showConfirmDialogBox(this, getString(R.string.message_confirmation_exit),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }
}
