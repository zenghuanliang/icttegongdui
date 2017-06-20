package com.example.administrator.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import chartmanager.BarChartManager;
import chartmanager.LineChartManager;


public class cellRadioResourceAvailabilityActivity extends AppCompatActivity implements View.OnClickListener {

    private RadioButton attach;
    private RadioButton eps;
    private RadioButton dns;
    private RadioButton tcp;
    private RadioButton bigpkt;
    private LineChart mCombinedLineChart;
    private BarChart mBarChart;
    private Context context;
    Handler mHandler_five_succ;
    Handler mHandler_five_Time;
    ArrayList<Object> user_obj =new ArrayList<>();
    ArrayList<String> xVals = new ArrayList<>();
    ArrayList<Float> eps_F =new ArrayList<>();
    ArrayList<Float> attach_F =new ArrayList<>();
    ArrayList<Float> tcp_F =new ArrayList<>();
    ArrayList<Float> dns_F =new ArrayList<>();
    ArrayList<Float> http_F =new ArrayList<>();
    ArrayList<Object> user1_obj =new ArrayList<>();
    ArrayList<String> xVals1 = new ArrayList<>();
    ArrayList<Float> eps1_F =new ArrayList<>();
    ArrayList<Float> attach1_F =new ArrayList<>();
    ArrayList<Float> tcp1_F =new ArrayList<>();
    ArrayList<Float> dns1_F =new ArrayList<>();
    ArrayList<Float> http1_F =new ArrayList<>();
    final String url="http://172.16.201.21:8090/com.IDC/user?action=five_succ_rate";
    final String url1="http://172.16.201.21:8090/com.IDC/user?action=five_time_delay";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cell_radio_resource_availability);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mCombinedLineChart = (LineChart)findViewById(R.id.userCon_lineChart);
        mBarChart = (BarChart)findViewById(R.id.userCon_barChart);
        this.context = getApplicationContext();
        LineChartManager.setLineName("EPS成功率");
        LineChartManager.setLineName1("DNS成功率");
        LineChartManager.setLineName2("HTTP成功率");
        LineChartManager.setLineName3("ATTACH成功率");
        LineChartManager.setLineName4("TCP成功率");
        BarChartManager.setUnit("EPS时延");
        BarChartManager.setUnit1("ATTACH时延");
        BarChartManager.setUnit2("HTTP时延");
        BarChartManager.setUnit3("TCP时延");
        BarChartManager.setUnit4("DNS时延");
        new Get_Five_Succ_DataTask(url).execute();
        new Get_Five_Time_DataTask(url1).execute();
        LineChartManager.initFiveLineChart(context,mCombinedLineChart,xVals,generateEps_yVales(),generateDns_yVales(),generateHttp_yVales(),generateAttach_yVales(),generateTcp_yVales(),100,0);
        BarChartManager.initFiveBarChart(context,mBarChart,xVals1,generateEpsBarData(),generateAttachBarData(),generateHttpBarData(),generateTcpBarData(),generateDnsBarData(),700,0);
        Button five_succ_Button = (Button) findViewById(R.id.five_succ_Button);
        Button five_time_Button = (Button) findViewById(R.id.five_time_button);
        five_succ_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 1;
                mHandler_five_succ.sendMessage(msg);
            }
        });
        mHandler_five_succ = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x1:
                        LineChartManager.initFiveLineChart(context,mCombinedLineChart,xVals,generateEps_yVales(),generateDns_yVales(),generateHttp_yVales(),generateAttach_yVales(),generateTcp_yVales(),100,0);
                        break;

                }
            }
        };
        five_time_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 1;
                mHandler_five_Time.sendMessage(msg);
            }
        });

        mHandler_five_Time = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x1:
                        BarChartManager.initFiveBarChart(context,mBarChart,xVals1,generateEpsBarData(),generateAttachBarData(),generateHttpBarData(),generateTcpBarData(),generateDnsBarData(),700,0);
                        break;

                }
            }
        };
        initViews();
        initEvents();
    }
    //初始化  各种个 View
    private void initViews(){
        attach = (RadioButton) findViewById(R.id.attach);
        eps = (RadioButton) findViewById(R.id.eps);
        dns = (RadioButton) findViewById(R.id.dns);
        tcp = (RadioButton) findViewById(R.id.tcp);
        bigpkt = (RadioButton) findViewById(R.id.bigpkt);
    }

    //初始化 监听事件
    private void initEvents(){
        attach.setOnClickListener(this);
        eps.setOnClickListener(this);
        dns.setOnClickListener(this);
        tcp.setOnClickListener(this);
        bigpkt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.attach:
                Intent intent1 = new Intent(cellRadioResourceAvailabilityActivity.this, attachActivity.class);
                startActivity(intent1);
                break;
            case R.id.eps:
                Intent intent2 = new Intent(cellRadioResourceAvailabilityActivity.this, epsActivity.class);
                startActivity(intent2);
                break;
            case R.id.dns:
                Intent intent3 = new Intent(cellRadioResourceAvailabilityActivity.this, dnsActivity.class);
                startActivity(intent3);
                break;
            case R.id.tcp:
                Intent intent4 = new Intent(cellRadioResourceAvailabilityActivity.this, tcpActivity.class);
                startActivity(intent4);
                break;
            case R.id.bigpkt:
                Intent intent5 = new Intent(cellRadioResourceAvailabilityActivity.this, httpActivity.class);
                startActivity(intent5);
                break;
        }


    }

    private class Get_Five_Succ_DataTask extends AsyncTask<Void, Void, String> {

        String url="";

        public Get_Five_Succ_DataTask(String url) {
            this.url = url;
        }

        /**请求网络数据并返回结果
         *
         */
        @Override
        protected String doInBackground(Void... voids) {
            String result = "";
            try {
                result =JSONUtil1.getRequest(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        /**使用Gson处理返回的数据并把数据放进Arraylist<Object>里，同事完成Y轴
         * 构造数据组F的写入
         *
         */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result == null || result.equals("")) {
                Toast.makeText(cellRadioResourceAvailabilityActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    Gson gson = new Gson();
                    List<Map<String,Object>> list = gson.fromJson(result,new TypeToken<List<Map<String,Object>>>(){
                    }.getType());
                    if (list.get(0) !=null) {
                        Toast.makeText(cellRadioResourceAvailabilityActivity.this,"获取数据成功",Toast.LENGTH_SHORT).show();

                        for (Map<String, Object> m : list)
                        {
                            for (String k : m.keySet())
                            {
                                user_obj.add(m.get(k));
                            }
                        }
                       // Toast.makeText(cellRadioResourceAvailabilityActivity.this,user_obj.toString(), Toast.LENGTH_SHORT).show();


                        for(int i=1;i<user_obj.size();i=i+6){

                            xVals.add(user_obj.get(i).toString().substring(0,12));

                        }
                       // Toast.makeText(cellRadioResourceAvailabilityActivity.this,xVals.toString(), Toast.LENGTH_SHORT).show();

                        for(int i=0;i<user_obj.size();i=i+6){

                            eps_F.add(Float.parseFloat(user_obj.get(i).toString()));

                        }
                        //Toast.makeText(cellRadioResourceAvailabilityActivity.this,eps_F.toString(), Toast.LENGTH_SHORT).show();

                        for(int i=2;i<user_obj.size();i=i+6){

                            dns_F.add(Float.parseFloat(user_obj.get(i).toString()));

                        }
                      //  Toast.makeText(cellRadioResourceAvailabilityActivity.this,dns_F.toString(), Toast.LENGTH_SHORT).show();

                        for(int i=3;i<user_obj.size();i=i+6){

                            http_F.add(Float.parseFloat(user_obj.get(i).toString()));

                        }
                        //Toast.makeText(cellRadioResourceAvailabilityActivity.this,http_F.toString(), Toast.LENGTH_SHORT).show();

                        for(int i=4;i<user_obj.size();i=i+6){

                            attach_F.add(Float.parseFloat(user_obj.get(i).toString()));

                        }
                       // Toast.makeText(cellRadioResourceAvailabilityActivity.this,attach_F.toString(), Toast.LENGTH_SHORT).show();

                        for(int i=4;i<user_obj.size();i=i+6){

                            tcp_F.add(Float.parseFloat(user_obj.get(i).toString()));

                        }
                       // Toast.makeText(cellRadioResourceAvailabilityActivity.this,tcp_F.toString(), Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(cellRadioResourceAvailabilityActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JsonIOException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    Toast.makeText(cellRadioResourceAvailabilityActivity.this,"程序错误",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private ArrayList<Entry> generateEps_yVales() {

        //LineData d = new LineData();
        ArrayList<Entry> entries = new ArrayList<>();
        for (int index = 0; index < eps_F.size(); index++)
            entries.add(new Entry(eps_F.get(index)*100, index));
        return entries;
    }

    private ArrayList<Entry> generateDns_yVales() {

       // LineData d = new LineData();
        ArrayList<Entry> entries = new ArrayList<>();
        for (int index = 0; index < dns_F.size(); index++)
            entries.add(new Entry(dns_F.get(index)*100, index));
        return entries;
    }

    private ArrayList<Entry>  generateHttp_yVales() {

       // LineData d = new LineData();
        ArrayList<Entry> entries = new ArrayList<>();
        for (int index = 0; index < http_F.size(); index++)
            entries.add(new Entry(http_F.get(index)*100, index));
        return entries;
    }

    private ArrayList<Entry> generateAttach_yVales() {

       // LineData d = new LineData();
        ArrayList<Entry> entries = new ArrayList<>();
        for (int index = 0; index < attach_F.size(); index++)
            entries.add(new Entry(attach_F.get(index)*100, index));
        return entries;
    }

    private ArrayList<Entry> generateTcp_yVales() {

        //LineData d = new LineData();
        ArrayList<Entry> entries = new ArrayList<>();
        for (int index = 0; index < tcp_F.size(); index++)
            entries.add(new Entry(tcp_F.get(index)*100, index));
        return entries;

    }
    private ArrayList<BarEntry> generateEpsBarData() {
        ArrayList<BarEntry> yVals1_eps = new ArrayList<>();
        for (int i = 0; i < eps1_F.size(); i++) {
            yVals1_eps.add(new BarEntry(eps1_F.get(i), i));
        }
        return yVals1_eps;
    }

    private ArrayList<BarEntry> generateAttachBarData() {
        ArrayList<BarEntry> yVals_attach = new ArrayList<>();
        for (int i = 0; i < attach1_F.size(); i++) {
            yVals_attach.add(new BarEntry(attach1_F.get(i), i));
        }
        return  yVals_attach;
    }
    private  ArrayList<BarEntry> generateHttpBarData() {
        ArrayList<BarEntry> yVals1_http = new ArrayList<>();
        for (int i = 0; i < http1_F.size(); i++) {
            yVals1_http.add(new BarEntry(http1_F.get(i), i));
        }
        return yVals1_http;
    }

    private ArrayList<BarEntry> generateTcpBarData() {
        ArrayList<BarEntry> yVals1_tcp = new ArrayList<>();
        for (int i = 0; i < tcp1_F.size(); i++) {
            yVals1_tcp.add(new BarEntry(tcp1_F.get(i), i));
        }
        return yVals1_tcp;
    }

    private ArrayList<BarEntry> generateDnsBarData() {
        ArrayList<BarEntry> yVals1_dns = new ArrayList<>();
        for (int i = 0; i < dns1_F.size(); i++) {
            yVals1_dns.add(new BarEntry(dns1_F.get(i), i));
        }
        return yVals1_dns;
    }


    public void setLegend(Legend l, boolean LegendEnabled) {
        if (LegendEnabled) {
            l.setTextColor(Color.BLUE);
            l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
            l.setForm(Legend.LegendForm.LINE);
        }
        l.setEnabled(LegendEnabled);
    }
    public void setLeftYAxis(YAxis leftAxis, boolean yAxisEnabled, float maxValue, float minValue) {
        if (yAxisEnabled) {
            leftAxis.setDrawZeroLine(false);
            leftAxis.setAxisMaxValue(maxValue );
            leftAxis.setAxisMinValue(minValue);
            leftAxis.setTextColor(Color.BLUE);
            leftAxis.setDrawAxisLine(true);
            //颠倒显示
            //leftAxis.setInverted(true);
            //显示Y轴的坐标数 true:按平均值  false:按整数 y-axis max = 25, min = 2, default: 6,
            leftAxis.setLabelCount(6, true);
        }
        leftAxis.setEnabled(yAxisEnabled);
    }
    public void setRightYAxis(YAxis rightAxis, boolean yAxisEnabled, float mmaxValue, float mminValue) {
        if (yAxisEnabled) {
            rightAxis.setDrawZeroLine(false);
            rightAxis.setAxisMaxValue(mmaxValue );
            rightAxis.setAxisMinValue(mminValue);
            rightAxis.setTextColor(Color.BLUE);
            rightAxis.setDrawAxisLine(true);
            //颠倒显示
            //leftAxis.setInverted(true);
            //显示Y轴的坐标数 true:按平均值  false:按整数 y-axis max = 25, min = 2, default: 6,
            rightAxis.setLabelCount(6, true);
        }
        rightAxis.setEnabled(yAxisEnabled);
    }
    public void setyAxis(YAxis yAxis, boolean yAxisEnabled) {
        if (yAxisEnabled) {
            yAxis.setDrawAxisLine(true);
            yAxis.setDrawGridLines(true);
        }
        yAxis.setEnabled(yAxisEnabled);
    }
    public void setxAxis(XAxis xAxis, boolean xAxisEnabled) {
        if (xAxisEnabled) {
            xAxis.setTextColor(Color.BLUE);
            xAxis.setDrawGridLines(false);
            xAxis.setAvoidFirstLastClipping(true);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        }
        xAxis.setEnabled(xAxisEnabled);
    }

    private class Get_Five_Time_DataTask extends AsyncTask<Void, Void, String> {

        String url="";

        public Get_Five_Time_DataTask(String url) {
            this.url = url;
        }

        /**请求网络数据并返回结果
         *
         */
        @Override
        protected String doInBackground(Void... voids) {
            String result = "";
            try {
                result =JSONUtil1.getRequest(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        /**使用Gson处理返回的数据并把数据放进Arraylist<Object>里，同事完成Y轴
         * 构造数据组F的写入
         *
         */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result == null || result.equals("")) {
                Toast.makeText(cellRadioResourceAvailabilityActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    Gson gson = new Gson();
                    List<Map<String,Object>> list = gson.fromJson(result,new TypeToken<List<Map<String,Object>>>(){
                    }.getType());
                    if (list.get(0) !=null) {
                        Toast.makeText(cellRadioResourceAvailabilityActivity.this,"获取数据成功",Toast.LENGTH_SHORT).show();

                        for (Map<String, Object> m : list)
                        {
                            for (String k : m.keySet())
                            {
                                user1_obj.add(m.get(k));
                            }
                        }
                      //  Toast.makeText(cellRadioResourceAvailabilityActivity.this,user1_obj.toString(), Toast.LENGTH_SHORT).show();


                        for(int x=0;x<user1_obj.size();x=x+6){

                            xVals1.add(user1_obj.get(x).toString().substring(0,12));

                        }
                       // Toast.makeText(cellRadioResourceAvailabilityActivity.this,xVals1.toString(), Toast.LENGTH_SHORT).show();

                        for(int i=4;i<user1_obj.size();i=i+6){

                            eps1_F.add(Float.parseFloat(user1_obj.get(i).toString()));

                        }
                       // Toast.makeText(cellRadioResourceAvailabilityActivity.this,eps_F.toString(), Toast.LENGTH_SHORT).show();

                        for(int i=5;i<user1_obj.size();i=i+6){

                            dns1_F.add(Float.parseFloat(user1_obj.get(i).toString()));

                        }
                       // Toast.makeText(cellRadioResourceAvailabilityActivity.this,dns1_F.toString(), Toast.LENGTH_SHORT).show();

                        for(int i=2;i<user1_obj.size();i=i+6){

                            http1_F.add(Float.parseFloat(user1_obj.get(i).toString()));

                        }
                        //Toast.makeText(cellRadioResourceAvailabilityActivity.this,http1_F.toString(), Toast.LENGTH_SHORT).show();

                        for(int i=1;i<user1_obj.size();i=i+6){

                            attach1_F.add(Float.parseFloat(user1_obj.get(i).toString()));

                        }
                        //Toast.makeText(cellRadioResourceAvailabilityActivity.this,attach1_F.toString(), Toast.LENGTH_SHORT).show();

                        for(int i=3;i<user1_obj.size();i=i+6){

                            tcp1_F.add(Float.parseFloat(user1_obj.get(i).toString()));

                        }
                        //Toast.makeText(cellRadioResourceAvailabilityActivity.this,tcp1_F.toString(), Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(cellRadioResourceAvailabilityActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JsonIOException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    Toast.makeText(cellRadioResourceAvailabilityActivity.this,"程序错误",Toast.LENGTH_SHORT).show();
                }
            }
        }
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

