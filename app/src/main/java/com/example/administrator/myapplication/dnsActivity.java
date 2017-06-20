package com.example.administrator.myapplication;

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
import com.github.mikephil.charting.components.Legend;
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

public class dnsActivity extends AppCompatActivity {
    CombinedChart mDnsCombinedChart;
    private final float minValue = 0;
    private final float maxValue = 100;
    private final float mminValue = 100;
    private final float mmaxValue = 200;
    Handler mHandler_dns;
    ArrayList<Object> dns_obj =new ArrayList<>();
    ArrayList<String> mTime = new ArrayList<>();
    ArrayList<Float> dns_LineChart_F =new ArrayList<>();
    ArrayList<Float> dns_BarChart_F =new ArrayList<>();
    final String url="http://172.16.201.21:8090/com.IDC/user?action=dns";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dns);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDnsCombinedChart= (CombinedChart)findViewById(R.id.Dns_CombinedChart);
        new Get_Dns_DataTask(url).execute();
        Button dns_button = (Button) findViewById(R.id.dns_update);
        dns_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Message msg = new Message();
                msg.what = 1;
                mHandler_dns.sendMessage(msg);
            }
        });


        mHandler_dns = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x1:
                        ShowChart(mDnsCombinedChart);
                        break;
                }
            }
        };


    }

    public void ShowChart(CombinedChart mCombinedChart){
        mCombinedChart.setDescription("");
        mCombinedChart.setBackgroundColor(Color.WHITE);
        mCombinedChart.setDrawGridBackground(false);
        mCombinedChart.setDrawBarShadow(false);
        // draw bars behind lines
        mCombinedChart.setDrawOrder(new CombinedChart.DrawOrder[] {
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.BUBBLE, CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.SCATTER
        });
     /*         * 获取头部信息         */
        setLegend(mCombinedChart.getLegend(), true);
        /*          *Y轴 右侧数据设置     */
        setyAxis(mCombinedChart.getAxisRight(), false);
        /*          *Y轴 左侧数据设置     */
        setLeftYAxis(mCombinedChart.getAxisLeft(), true, maxValue, minValue);
        setRightYAxis(mCombinedChart.getAxisRight(),true,mmaxValue,mminValue);
        /*          *X轴    数据设置      */
        setxAxis(mCombinedChart.getXAxis(), true);

       /* YAxis rightAxis = mCombinedChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinValue(0f);
        //rightAxis.setAxisMaxValue(100000f); // this replaces setStartAtZero(true)

        YAxis leftAxis = mCombinedChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)
        //leftAxis.setAxisMaxValue(1500f);
        XAxis xAxis = mCombinedChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
*/
        CombinedData data = new CombinedData(mTime);

        data.setData(generateLineData());
        data.setData(generateBarData());

        mCombinedChart.setData(data);
        mCombinedChart.invalidate();
    }


    private LineData generateLineData() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<>();

        for (int index = 0; index < dns_LineChart_F.size(); index++)
            entries.add(new Entry(dns_LineChart_F.get(index)*100, index));

        LineDataSet set = new LineDataSet(entries, "DNS成功率");
        set.setColor(Color.rgb(00, 128, 255));
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.rgb(00, 128, 255));
        set.setCircleRadius(5f);
        set.setFillColor(Color.rgb(00, 128, 255));
        set.setDrawCubic(true);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(00, 128, 255));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        d.addDataSet(set);

        return d;
    }
    private BarData generateBarData() {

        BarData d = new BarData();

        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int index = 0; index < dns_BarChart_F.size(); index++)
            entries.add(new BarEntry(dns_BarChart_F.get(index), index));

        BarDataSet set = new BarDataSet(entries, "DNS平均时延");
        set.setColor(Color.rgb(9,202,214));
        set.setValueTextColor(Color.rgb(9,202,214));
        set.setValueTextSize(10f);
        d.addDataSet(set);

        set.setAxisDependency(YAxis.AxisDependency.RIGHT);

        return d;
    }

    public void setLegend(Legend l, boolean LegendEnabled) {
        if (LegendEnabled) {
            l.setTextColor(Color.BLUE);
            l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
            l.setForm(Legend.LegendForm.CIRCLE);
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


    private class Get_Dns_DataTask extends AsyncTask<Void, Void, String> {

        String url="";

        public Get_Dns_DataTask(String url) {
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
                Toast.makeText(dnsActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    //JSONObject json = new JSONObject(result);
                    Gson gson = new Gson();
                    List<Map<String,Object>> list = gson.fromJson(result,new TypeToken<List<Map<String,Object>>>(){
                    }.getType());
                    if (list.get(0) !=null) {
                        Toast.makeText(dnsActivity.this,"获取数据成功",Toast.LENGTH_SHORT).show();

                        for (Map<String, Object> m : list)
                        {
                            for (String k : m.keySet())
                            {
                                dns_obj.add(m.get(k));
                            }
                        }

                        for(int i=0;i<dns_obj.size();i=i+3){

                            mTime.add(dns_obj.get(i).toString().substring(0,12));

                        }
                          // Toast.makeText(dnsActivity.this,mTime.toString(), Toast.LENGTH_SHORT).show();

                        for(int i=1;i<dns_obj.size();i=i+3){

                            dns_LineChart_F.add(Float.parseFloat(dns_obj.get(i).toString()));

                        }
                            //Toast.makeText(dnsActivity.this,dns_LineChart_F.toString(), Toast.LENGTH_SHORT).show();

                        for(int i=2;i<dns_obj.size();i=i+3){

                            dns_BarChart_F.add(Float.parseFloat(dns_obj.get(i).toString()));

                        }
                         //Toast.makeText(dnsActivity.this,dns_BarChart_F.toString(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(dnsActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JsonIOException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    Toast.makeText(dnsActivity.this,"程序错误",Toast.LENGTH_SHORT).show();
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
