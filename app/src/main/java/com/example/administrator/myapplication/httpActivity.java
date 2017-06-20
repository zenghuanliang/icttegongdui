package com.example.administrator.myapplication;

import android.content.Context;
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
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import chartmanager.BarChartManager;

public class httpActivity extends AppCompatActivity {
    private PieChart mHttpPieChart;
    private BarChart mHttpBarChart;
    private Context mContext;
    Handler mHandler_http;
    ArrayList<String> xVals_http = new ArrayList<>();
    ArrayList<Object> http_obj =new ArrayList<>();
    ArrayList<Float> http_F =new ArrayList<>();
    ArrayList<Float> http1_F =new ArrayList<>();
    ArrayList<String> xValues = new ArrayList<>();
    final String url="http://172.16.201.21:8090/com.IDC/user?action=http";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //       WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_http);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = getApplicationContext();
        mHttpPieChart = (PieChart)findViewById(R.id.http_pie_chart);
        mHttpBarChart = (BarChart)findViewById(R.id.http_bar_chart) ;
        new Get_Http_DataTask(url).execute();
        BarChartManager.setUnit("数据流量 单位：GB");
        BarChartManager.initSingleBarChart(mContext,mHttpBarChart,xValues,getBarData_yVals(),18000,0);
        Button http_button = (Button)findViewById(R.id.http_button);
        Button http1_button = (Button)findViewById(R.id.http1_button);
        http_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 1;
                mHandler_http.sendMessage(msg);
            }
        });
        http1_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 2;
                mHandler_http.sendMessage(msg);
            }
        });
        mHandler_http = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x1:
                        // 图的描述
                        mHttpPieChart.setDescription("HTTP主要内容流量分布");
                        // 以8个对应数据集做测试
                        setData();
                        break;

                    case 0x2:
                        // 图的描述
                        BarChartManager.initSingleBarChart(mContext,mHttpBarChart,xValues,getBarData_yVals(),18000,0);
                        break;
                }
            }
        };
       mHttpBarChart.invalidate();

    }


    private void setData() {

        //准备x"轴"数据：在i的位置，显示x[i]字符串
        ArrayList<String> xVals = new ArrayList<>();

        // 真实的饼状图百分比分区。
        // Entry包含两个重要数据内容：position和该position的数值。
        ArrayList<Entry> yVals = new ArrayList<>();

        for (int xi = 0; xi <xVals_http.size() ; xi++) {
            xVals.add(xi, xVals_http.get(xi));

            // y[i]代表在x轴的i位置真实的百分比占
            yVals.add(new Entry(http_F.get(xi)*100, xi));
        }

        PieDataSet yDataSet = new PieDataSet(yVals, "百分比占");

        // 每个百分比占区块绘制的不同颜色
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(9,202,214));
        colors.add(Color.GREEN);
        colors.add(Color.BLUE);
        colors.add(Color.rgb(128,255,0));
        colors.add(Color.rgb(00, 128, 255));
        colors.add(Color.rgb(255, 0, 0));
        colors.add(Color.rgb(00, 128, 255));
        colors.add(Color.rgb(255, 255, 0));

        yDataSet.setColors(colors);

        // 将x轴和y轴设置给PieData作为数据源
        PieData data = new PieData(xVals_http, yDataSet);

        // 设置成PercentFormatter将追加%号
        data.setValueFormatter(new PercentFormatter());

        // 文字的颜色
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(8f);

        // 最终将全部完整的数据喂给PieChart
        mHttpPieChart.setData(data);
        mHttpPieChart.invalidate();
    }

    private class Get_Http_DataTask extends AsyncTask<Void, Void, String> {

        String url="";

        public Get_Http_DataTask(String url) {
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
                Toast.makeText(httpActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    Gson gson = new Gson();
                    List<Map<String,Object>> list = gson.fromJson(result,new TypeToken<List<Map<String,Object>>>(){
                    }.getType());
                    if (list.get(0) !=null) {
                        Toast.makeText(httpActivity.this,"获取数据成功",Toast.LENGTH_SHORT).show();

                        for (Map<String, Object> m : list)
                        {
                            for (String k : m.keySet())
                            {
                                http_obj.add(m.get(k));
                            }
                        }
                        //Toast.makeText(httpActivity.this,http_obj.toString(), Toast.LENGTH_SHORT).show();


                        for(int i=0;i<http_obj.size();i=i+3){

                            xVals_http.add(http_obj.get(i).toString());

                        }
                        //Toast.makeText(httpActivity.this,xVals_http.toString(), Toast.LENGTH_SHORT).show();

                        for(int i=1;i<http_obj.size();i=i+3){

                            http_F.add(Float.parseFloat(http_obj.get(i).toString()));

                        }
                        //Toast.makeText(httpActivity.this,http_F.toString(), Toast.LENGTH_SHORT).show();

                        for(int i=2;i<http_obj.size();i=i+3){

                            http1_F.add(Float.parseFloat(http_obj.get(i).toString()));

                        }
                        //Toast.makeText(httpActivity.this,http1_F.toString(), Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < http_obj.size(); i=i+3) {
                            xValues.add(http_obj.get(i).toString());
                        }

                    } else {
                        Toast.makeText(httpActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JsonIOException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    Toast.makeText(httpActivity.this,"程序错误",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private ArrayList<BarEntry> getBarData_yVals() {

        ArrayList<BarEntry> yVals_http = new ArrayList<>();
        for (int i = 0; i < http1_F.size(); i++) {

            yVals_http.add(new BarEntry(http1_F.get(i)/1048576, i));
        }

        return yVals_http;
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
