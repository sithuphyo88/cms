package com.ideapro.cms.view;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ideapro.cms.R;
import com.ideapro.cms.utils.CommonUtils;
import com.ideapro.cms.view.MenuActivity;


public class LoginActivity extends ActionBarActivity {

    EditText txtUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setActionBar();
        txtUserName = (EditText) findViewById(R.id.txtUserName);

        Button butLogin = (Button)findViewById(R.id.butLogin);
        butLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(txtUserName.getText())) {
                        CommonUtils.CurrentUser.role = "admin";
                    } else if(txtUserName.getText().toString().toLowerCase().startsWith("admin")) {
                        CommonUtils.CurrentUser.role = "admin";
                } else {
                    CommonUtils.CurrentUser.role = "engineer";
                }

                CommonUtils.transitToActivity(LoginActivity.this, MenuActivity.class);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setActionBar() {
        ActionBar mActionBar = this.getSupportActionBar();
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.fragment_actionbar, null);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setBackgroundDrawable(getResources().getDrawable(R.color.home_actionbar_back_color));

        ImageButton imgRight = (ImageButton)mActionBar.getCustomView().findViewById(R.id.imgRight);
        imgRight.setVisibility(View.GONE);
    }
}
