package com.example.administrator.myapplication;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.TRUE;

public class LineChartActivity extends AppCompatActivity {
    /**
     * 声明linechart
     * X轴和Y轴的数组以及  F是构造Y轴的数据；
     * 声明一个新的线程；
     */
    LineChart mLineChart;
    ArrayList<Object> obj =new ArrayList<>();
    List<String> xValues =new ArrayList<>();
    List<Entry> yValues =new ArrayList<>();
    ArrayList<Float> F =new ArrayList<>();
    Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        mLineChart = (LineChart) findViewById(R.id.line_chart);
        LineData mLineData = getLineData();
        showChart(mLineChart, mLineData, Color.rgb(0, 0, 0));//114, 188, 223
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
            }
        });

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x1:
                        LineData mLineData = getLineData();
                        showChart(mLineChart, mLineData, Color.rgb(0, 0, 0));//114, 188, 223
                        //break;
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
                Toast.makeText(LineChartActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    //JSONObject json = new JSONObject(result);
                    Gson gson = new Gson();
                    List<Map<String,Object>> list = gson.fromJson(result,new TypeToken<List<Map<String,Object>>>(){
                    }.getType());
                    if (list.get(0) !=null) {
                        Toast.makeText(LineChartActivity.this,"获取数据成功",Toast.LENGTH_SHORT).show();

                        for (Map<String, Object> m : list)
                        {
                            for (String k : m.keySet())
                            {
                                obj.add(m.get(k));
                            }
                        }


                        for(int i=1;i<obj.size();i=i+2){

                            F.add(Float.parseFloat(obj.get(i).toString()));

                        }


                    } else {
                        Toast.makeText(LineChartActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JsonIOException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    Toast.makeText(LineChartActivity.this,"工程师正在努力修改",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



    /**显示图标样式.
     *
     */
    private void showChart(LineChart lineChart, LineData lineData, int color) {
        if (lineChart == null) {
            Log.e("yasin", "line null");
        } else {
            Log.e("yasin", "line not null");
        }

        lineChart.setDrawBorders(false);//是否添加边框
        lineChart.setDescription("有风险的数据");//数据描述
        lineChart.setNoDataTextDescription("我需要数据");//没数据显示
        lineChart.setDrawGridBackground(true);//是否显示表格颜色
        //lineChart.setBackgroundColor(Color.YELLOW);//背景颜色
        lineChart.setData(lineData);//设置数据
        Legend legend = lineChart.getLegend();//设置比例图片标示，就是那一组Y的value
        legend.setForm(Legend.LegendForm.CIRCLE);//样式
        legend.setFormSize(6f);//字体
        legend.setTextColor(Color.WHITE);//设置颜色

        lineChart.setDescription("Attach成功率");
        //lineChart.animateY(3000);



        //LineData data = new LineData(xValues, dataSet);
        //mLineChart.setData(data);
        mLineChart.setDescription("Attach成功率");

        // add data
        lineChart.setData(lineData); // 设置数据

        // get the legend (only possible after setting data)
        Legend mLegend = lineChart.getLegend(); // 设置比例图标示，就是那个一组y的value的

        // modify the legend ...
        // mLegend.setPosition(LegendPosition.LEFT_OF_CHART);
        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
        mLegend.setFormSize(6f);// 字体
        mLegend.setTextColor(Color.BLUE);// 颜色
//      mLegend.setTypeface(mTf);// 字体

        //   lineChart.animateX(0); // 立即执行的动画,x轴
        lineChart.animateX(2000);//X轴的动画*/
    }

    /**通过X轴和Y轴的数组数据，构造LineDate,并返回值。
     *
     */
    private LineData getLineData() {

        final String url="http://172.16.201.21:8090/com.IDC/user?action=attach";
        new GetSDataTask(url).execute();

        for(int i=0;i<obj.size();i=i+2){

            xValues.add(obj.get(i).toString().substring(0, 12));

        }

        for(int j=0;j<F.size();j++){

            yValues.add(new Entry(F.get(j),j));

        }

        // create a dataset and give it a type
        // y轴的数据集合
        LineDataSet lineDataSet = new LineDataSet(yValues, "Attach成功率");

        //用y轴的集合来设置参数
        //设置填充
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(Color.rgb(9,202,214));
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineColor(Color.parseColor("#66CDAA"));
        xAxis.setAxisLineWidth(5);
        xAxis.setDrawGridLines(false);
        xAxis.setEnabled(TRUE);
        xAxis.setLabelsToSkip(1);
        YAxis yAxisLeft = mLineChart.getAxisLeft();

        yAxisLeft.setAxisLineColor(Color.parseColor("#66CDAA"));
        //yAxisLeft.setAxisMinValue(0);
        //yAxisLeft.setAxisMaxValue(2);
        yAxisLeft.setAxisLineWidth(5);
        yAxisLeft.setDrawGridLines(false);
        lineDataSet = new LineDataSet(yValues, "Attach成功率");
        lineDataSet.setColor(Color.parseColor("#0080FF"));
        lineDataSet.setCircleColor(Color.parseColor("#0080FF"));
        // create a data object with the datasets
        LineData lineData = new LineData(xValues, lineDataSet);
        return lineData;
    }
}
