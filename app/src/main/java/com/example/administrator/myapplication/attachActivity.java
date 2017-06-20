package com.example.administrator.myapplication;

import android.content.Context;
import android.content.Intent;
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

public class attachActivity extends AppCompatActivity {
    LineChart attachLineChart;
    private Context context;
    ArrayList<Object> obj1 =new ArrayList<>();
    ArrayList<String> xValues1 =new ArrayList<>();
    ArrayList<Float> F1 =new ArrayList<>();
    Handler mHandler1;
    final String url="http://172.16.201.21:8090/com.IDC/user?action=attach";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attach);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        attachLineChart = (LineChart) findViewById(R.id.attach1_chart);
        attachLineChart.setDescription("ATTACH成功率%");
        this.context = getApplicationContext();
        LineChartManager.setLineName("ATTACH成功率%");
        new GetSDataTask(url).execute();
        LineChartManager.initSingleLineChart(context,attachLineChart,xValues1,getLineData(),100,0);
        Button attach1 = (Button) findViewById(R.id.attach1);
        Button attach2 = (Button) findViewById(R.id.attach2);
        attach1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Message msg = new Message();
                msg.what = 1;
                mHandler1.sendMessage(msg);
            }
        });
        attach2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Message msg = new Message();
                msg.what = 2;
                mHandler1.sendMessage(msg);
            }
        });

        mHandler1 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x1:
                        LineChartManager.initSingleLineChart(context,attachLineChart,xValues1,getLineData(),100,0);
                        break;
                    case 0x2:
                       Intent intent = new Intent(attachActivity.this, attach1Activity.class);
                        startActivity(intent);
                        break;

                }
            }
        };



    }

    /**异步网络请求处理库：
     * 处理异步Http请求，并通过匿名内部类处理回调结果，Http异步请求均位于非UI线
     * 程，不会阻塞UI操作,通过线程池处理并发请求处理文件上传、下载,响应结果自动打包JSON格式.
     *
     */
    private class GetSDataTask extends AsyncTask<Void, Void, String> {

        String url="";

        public GetSDataTask(String url) {
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
                Toast.makeText(attachActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    //JSONObject json = new JSONObject(result);
                    Gson gson = new Gson();
                    List<Map<String,Object>> list = gson.fromJson(result,new TypeToken<List<Map<String,Object>>>(){
                    }.getType());
                    if (list.get(0) !=null) {
                        Toast.makeText(attachActivity.this,"获取数据成功",Toast.LENGTH_SHORT).show();

                        for (Map<String, Object> m : list)
                        {
                            for (String k : m.keySet())
                            {
                                obj1.add(m.get(k));
                            }
                        }


                        for(int i=1;i<obj1.size();i=i+2){

                            F1.add(Float.parseFloat(obj1.get(i).toString()));

                        }
                        for(int i=0;i<obj1.size();i=i+2){

                            xValues1.add(obj1.get(i).toString().substring(0, 12));

                        }
                        //Toast.makeText(attachActivity.this, xValues1.toString(), Toast.LENGTH_SHORT).show();

                    } else
                    {
                        Toast.makeText(attachActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JsonIOException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    Toast.makeText(attachActivity.this,"工程师正在努力修改",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



    /**显示图标样式.
     *
     */

    /**通过X轴和Y轴的数组数据，构造LineDate,并返回值。
     *
     */
    private ArrayList<Entry> getLineData() {
        ArrayList<Entry> yValues1 =new ArrayList<>();
        for(int j=0;j<F1.size();j++){

            yValues1.add(new Entry(F1.get(j)*100,j));
        }
        return yValues1;
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


