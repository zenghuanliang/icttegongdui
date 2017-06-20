package com.example.administrator.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    /**
     * 声明4个Radiobutton
     *
     */

    private RadioButton kpi;
    private RadioButton user;
    private RadioButton traffic;
    private RadioButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initViews();
        initEvents();
    }

    private void initViews() {
        kpi = (RadioButton) findViewById(R.id.tab_image1);
        user = (RadioButton) findViewById(R.id.tab_image2);
        traffic = (RadioButton) findViewById(R.id.tab_image3);
        back = (RadioButton) findViewById(R.id.tab_image4);
    }
    private void initEvents(){
        kpi.setOnClickListener(this);
        user.setOnClickListener(this);
        traffic.setOnClickListener(this);
        back.setOnClickListener(this);
    }
    private void initImageBack() {
        kpi.setBackgroundResource(R.drawable.kpi);
        user.setBackgroundResource(R.drawable.user);
        traffic.setBackgroundResource(R.drawable.traffic);
        back.setBackgroundResource(R.drawable.back);
    }

    private void select(int i) {

        switch (i) {
            case 0:
                kpi.setBackgroundResource(R.drawable.kpi);
                        Intent intent0 = new Intent(MainActivity.this,KPI_Wireless_Access_RateActivity.class);
                        startActivity(intent0);
                break;
            case 1:
                user.setBackgroundResource(R.drawable.user);
                Intent intent1 = new Intent(MainActivity.this,cellRadioResourceAvailabilityActivity.class);
                startActivity(intent1);
                break;
            case 2:
                traffic.setBackgroundResource(R.drawable.traffic);
                Toast.makeText(MainActivity.this,"此模块还在更新中", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                back.setBackgroundResource(R.drawable.back);
                Intent intent2 = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent2);
                break;

        }

    }
    @Override
    public void onClick (View v){

        initImageBack(); //初始化 图片背景

        switch (v.getId()) {
            case R.id.tab_image1:
                select(0);
                break;
            case R.id.tab_image2:
                select(1);
                break;
            case R.id.tab_image3:
                select(2);
                break;
            case R.id.tab_image4:
                select(3);
                break;

        }
    }

    // 点击手机HOME键，使应用程序退到后台；当再次打开App时，当前显示页面还是刚才退出时的页面
    // 点击返回键，弹出提示窗口
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            // 监控返回键
            new AlertDialog.Builder(MainActivity.this).setTitle("提示")
                    .setIconAttribute(android.R.attr.alertDialogIcon)
                    .setMessage("确定要退出吗?")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.finish();
                        }})
                    .setNegativeButton("取消", null)
                    .create().show();
            return false;
        } else if(keyCode == KeyEvent.KEYCODE_MENU) {
            // 监控菜单键
            Toast.makeText(MainActivity.this, "Menu", Toast.LENGTH_SHORT).show();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            android.os.Process.killProcess(android.os.Process.myPid());  //获取PID
            System.exit(0);   //常规java的标准退出法，返回值为0代表正常退出
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}





