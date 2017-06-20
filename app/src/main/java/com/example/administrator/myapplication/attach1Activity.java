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

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import chartmanager.CombinedChartMagnager;

public class attach1Activity extends AppCompatActivity {
    CombinedChart mCombinedChart;
    private Context context;
    Handler mHandler_attach1;
    ArrayList<Object> attach1_obj =new ArrayList<>();
    ArrayList<String> mCity = new ArrayList<>();
    ArrayList<Float> attach1_LineChart_F =new ArrayList<>();
    ArrayList<Float> attach1_BarChart_F =new ArrayList<>();
    final String url="http://172.16.201.21:8090/com.IDC/user?action=attach1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attach1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mCombinedChart = (CombinedChart)findViewById(R.id.attach_CombinedChart);
        this.context = getApplicationContext();
        CombinedChartMagnager.setLineName("Attach平均时延ms");
        CombinedChartMagnager.setBarName("用户量");
        new GetDataTask(url).execute();
        CombinedChartMagnager.initDataStyle(context,mCombinedChart,1000,0,3000,0);
        Button attach1 = (Button) findViewById(R.id.attach1_update);
        attach1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Message msg = new Message();
                msg.what = 1;
                mHandler_attach1.sendMessage(msg);
            }
        });

        mHandler_attach1 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x1:
                        CombinedChartMagnager.initConbinedChart(context,mCombinedChart,mCity,generateLineData(),generateBarData(),1000,0,3000,0);
                        break;
                }
            }
        };
    }

    private ArrayList<Entry> generateLineData() {

        ArrayList<Entry> entries = new ArrayList<>();

        for (int index = 0; index < attach1_LineChart_F.size(); index++)
            entries.add(new Entry(attach1_LineChart_F.get(index), index));

        return entries;
    }
    private ArrayList<BarEntry> generateBarData() {

        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int index = 0; index < attach1_BarChart_F.size(); index++)
            entries.add(new BarEntry(attach1_BarChart_F.get(index), index));

        return entries;
    }
    private class GetDataTask extends AsyncTask<Void, Void, String> {

        String url="";

        public GetDataTask(String url) {
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
                Toast.makeText(attach1Activity.this, "网络错误", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    //JSONObject json = new JSONObject(result);
                    Gson gson = new Gson();
                    List<Map<String,Object>> list = gson.fromJson(result,new TypeToken<List<Map<String,Object>>>(){
                    }.getType());
                    if (list.get(0) !=null) {
                        Toast.makeText(attach1Activity.this,"获取数据成功",Toast.LENGTH_SHORT).show();

                        for (Map<String, Object> m : list)
                        {
                            for (String k : m.keySet())
                            {
                                attach1_obj.add(m.get(k));
                            }
                        }

                        for(int i=1;i<attach1_obj.size();i=i+3){

                            mCity.add(attach1_obj.get(i).toString());

                        }
                        //   Toast.makeText(attach1Activity.this,mCity.toString(), Toast.LENGTH_SHORT).show();

                        for(int i=0;i<attach1_obj.size();i=i+3){

                            attach1_LineChart_F.add(Float.parseFloat(attach1_obj.get(i).toString()));

                        }
                        //    Toast.makeText(attach1Activity.this,attach1_LineChart_F.toString(), Toast.LENGTH_SHORT).show();

                        for(int i=2;i<attach1_obj.size();i=i+3){

                            attach1_BarChart_F.add(Float.parseFloat(attach1_obj.get(i).toString()));

                        }
                        //   Toast.makeText(attach1Activity.this,attach1_BarChart_F.toString(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(attach1Activity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JsonIOException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    Toast.makeText(attach1Activity.this,"程序错误",Toast.LENGTH_SHORT).show();
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
