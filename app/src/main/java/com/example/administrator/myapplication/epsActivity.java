package com.example.administrator.myapplication;

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
import android.widget.Toast;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class epsActivity extends AppCompatActivity {
    CombinedChart mCombinedChart;
    Handler mHandler_eps;
    ArrayList<Object> eps_obj =new ArrayList<>();
    ArrayList<String> mTime = new ArrayList<>();
    ArrayList<Float> eps_LineChart_F =new ArrayList<>();
    ArrayList<Float> eps_BarChart1_F =new ArrayList<>();
    ArrayList<Float> eps_BarChart2_F =new ArrayList<>();
    final String url="http://172.16.201.21:8090/com.IDC/user?action=eps";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        new GetEpsDataTask(url).execute();
        Button eps_button = (Button) findViewById(R.id.eps_button);
        Button eps1Udate_button = (Button) findViewById(R.id.eps1Udate_button);
        eps_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 1;
                mHandler_eps.sendMessage(msg);
            }
        });
        eps1Udate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 2;
                mHandler_eps.sendMessage(msg);
            }
        });


        mHandler_eps = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x1:
                        ShowChart(mCombinedChart);
                        break;
                    case 0x2:
                    Intent intent = new Intent(epsActivity.this, eps1Activity.class);
                    startActivity(intent);
                    break;
                }
            }
        };
    }


    public void ShowChart(CombinedChart combinedChart){

        combinedChart = (CombinedChart)findViewById(R.id.eps_CombinedChart);
        combinedChart.setDescription("");
        combinedChart.setBackgroundColor(Color.WHITE);
        combinedChart.setDrawGridBackground(false);
        combinedChart.setDrawBarShadow(false);

        // draw bars behind lines
        combinedChart.setDrawOrder(new CombinedChart.DrawOrder[] {
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE
        });


        YAxis rightAxis = combinedChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinValue(0f);
        rightAxis.setAxisMaxValue(100f); // this replaces setStartAtZero(true)
        rightAxis.setLabelCount(6, true);
        YAxis leftAxis = combinedChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)
        //leftAxis.setAxisMaxValue(1500f);
        rightAxis.setLabelCount(6, true);
        XAxis xAxis = combinedChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        CombinedData data = new CombinedData(mTime);

        data.setData(generateLineData());
        //data.setData(generateBarData1());
        data.setData(generateBarData2());

        combinedChart.setData(data);
        combinedChart.invalidate();
    }


    private LineData generateLineData() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<>();

        for (int index = 0; index < eps_LineChart_F.size(); index++)
            entries.add(new Entry(eps_LineChart_F.get(index)*100, index));

        LineDataSet set = new LineDataSet(entries, "EPS成功率");
        set.setColor(Color.rgb(00, 128, 255));
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.rgb(00, 128, 255));
        set.setCircleRadius(3f);
        set.setFillColor(Color.rgb(00, 128, 255));
        set.setDrawCubic(true);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(00, 128, 255));

        set.setAxisDependency(YAxis.AxisDependency.RIGHT);

        d.addDataSet(set);

        return d;
    }
    private BarData generateBarData1() {

        BarData d = new BarData();

        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int index = 0; index < eps_BarChart1_F.size(); index++)
            entries.add(new BarEntry(eps_BarChart1_F.get(index), index));

        BarDataSet set = new BarDataSet(entries, "EPS尝试次数");
        set.setColor(Color.rgb(60, 220, 78));
        set.setValueTextColor(Color.rgb(60, 220, 78));
        set.setValueTextSize(10f);
        d.addDataSet(set);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        return d;
    }
    private BarData generateBarData2() {

        BarData d = new BarData();

        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int index = 0; index < eps_BarChart2_F.size(); index++)
            entries.add(new BarEntry(eps_BarChart2_F.get(index), index));

        BarDataSet set = new BarDataSet(entries, "EPS接入次数");
        set.setColor(Color.rgb(9,202,214));
        set.setValueTextColor(Color.rgb(9,202,214));
        set.setValueTextSize(10f);
        d.addDataSet(set);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        return d;
    }



    private class GetEpsDataTask extends AsyncTask<Void, Void, String> {

        String url="";

        public GetEpsDataTask(String url) {
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
                Toast.makeText(epsActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    //JSONObject json = new JSONObject(result);
                    Gson gson = new Gson();
                    List<Map<String,Object>> list = gson.fromJson(result,new TypeToken<List<Map<String,Object>>>(){
                    }.getType());
                    if (list.get(0) !=null) {
                        Toast.makeText(epsActivity.this,"获取数据成功",Toast.LENGTH_SHORT).show();

                        for (Map<String, Object> m : list)
                        {
                            for (String k : m.keySet())
                            {
                                eps_obj.add(m.get(k));
                            }
                        }
                       // Toast.makeText(epsActivity.this,eps_obj.toString(), Toast.LENGTH_SHORT).show();

                        for(int i=1;i<eps_obj.size();i=i+4){

                            mTime.add(eps_obj.get(i).toString().substring(0,12));

                        }
                       // Toast.makeText(epsActivity.this,mTime.toString(), Toast.LENGTH_SHORT).show();

                        for(int i=0;i<eps_obj.size();i=i+4){

                            eps_LineChart_F.add(Float.parseFloat(eps_obj.get(i).toString()));

                        }
                      //  Toast.makeText(epsActivity.this,eps_LineChart_F.toString(), Toast.LENGTH_SHORT).show();

                        for(int i=2;i<eps_obj.size();i=i+4){

                            eps_BarChart1_F.add(Float.parseFloat(eps_obj.get(i).toString()));

                        }

                        //Toast.makeText(epsActivity.this,eps_BarChart1_F.toString(), Toast.LENGTH_SHORT).show();
                        for(int i=3;i<eps_obj.size();i=i+4){

                            eps_BarChart2_F.add(Float.parseFloat(eps_obj.get(i).toString()));

                        }
                       // Toast.makeText(epsActivity.this,eps_BarChart2_F.toString(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(epsActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JsonIOException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    Toast.makeText(epsActivity.this,"工程师正在努力修改",Toast.LENGTH_SHORT).show();
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
