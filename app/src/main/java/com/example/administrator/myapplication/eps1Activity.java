package com.example.administrator.myapplication;

import android.content.Context;
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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import chartmanager.LineChartManager;

public class eps1Activity extends AppCompatActivity {
    Handler mHandler_eps1;
    private Context context;
    private final float minValue = 80;
    private final float maxValue = 160;
    private LineChart mEps1LineChart;
    ArrayList<Object> eps1_obj =new ArrayList<>();
    ArrayList<String> xVals_eps1 = new ArrayList<>();
    ArrayList<Float> eps1_F =new ArrayList<>();
    final String url="http://172.16.201.21:8090/com.IDC/user?action=eps1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eps1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mEps1LineChart = (LineChart)findViewById(R.id.eps1Con_lineChart);
        this.context = getApplicationContext();
        mEps1LineChart.setDescription("EPS平均时延ms");
        LineChartManager.setLineName("EPS平均时延ms");
        new Get_Eps1_DataTask(url).execute();
        LineChartManager.initSingleLineChart(context,mEps1LineChart,xVals_eps1,generateEps1LineData(),160,80);
        Button eps1_button = (Button)findViewById(R.id.eps1_button);
        eps1_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 1;
                mHandler_eps1.sendMessage(msg);
            }
        });
        mHandler_eps1 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x1:
                        LineChartManager.initSingleLineChart(context,mEps1LineChart,xVals_eps1,generateEps1LineData(),160,80);
                        break;
                }
            }
        };

    }


    private ArrayList<Entry> generateEps1LineData() {

        ArrayList<Entry> entries = new ArrayList<>();
        for (int index = 0; index < eps1_F.size(); index++)
            entries.add(new Entry(eps1_F.get(index), index));
        return entries;
    }


    private class Get_Eps1_DataTask extends AsyncTask<Void, Void, String> {

        String url="";

        public Get_Eps1_DataTask(String url) {
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
                Toast.makeText(eps1Activity.this, "网络错误", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    Gson gson = new Gson();
                    List<Map<String,Object>> list = gson.fromJson(result,new TypeToken<List<Map<String,Object>>>(){
                    }.getType());
                    if (list.get(0) !=null) {
                        Toast.makeText(eps1Activity.this,"获取数据成功",Toast.LENGTH_SHORT).show();

                        for (Map<String, Object> m : list)
                        {
                            for (String k : m.keySet())
                            {
                                eps1_obj.add(m.get(k));
                            }
                        }
                      //  Toast.makeText(eps1Activity.this,eps1_obj.toString(), Toast.LENGTH_SHORT).show();


                        for(int i=0;i<eps1_obj.size();i=i+2){

                            xVals_eps1.add(eps1_obj.get(i).toString().substring(0,12));

                        }
                      //  Toast.makeText(eps1Activity.this,xVals_eps1.toString(), Toast.LENGTH_SHORT).show();

                        for(int i=1;i<eps1_obj.size();i=i+2){

                            eps1_F.add(Float.parseFloat(eps1_obj.get(i).toString()));

                        }

                    } else {
                        Toast.makeText(eps1Activity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JsonIOException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    Toast.makeText(eps1Activity.this,"程序错误",Toast.LENGTH_SHORT).show();
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
