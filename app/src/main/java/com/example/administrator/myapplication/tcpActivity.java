package com.example.administrator.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class tcpActivity extends AppCompatActivity {
    List<Map<String,Object>> list =null;
    ArrayList<Object> tcp_obj =new ArrayList<>();
    ArrayList<String> tcp_k =new ArrayList<>();

    final String url="http://172.16.201.21:8090/com.IDC/user?action=tcp";
    private ListView listView = null;
    private String[] data = new String[]{"TCP成功率","TCP时延","TCP核心网成功率","TCP核心网时延"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcp);
        listView = (ListView)findViewById(R.id.tcp);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(tcpActivity.this,android.R.layout.simple_list_item_1,data);
        //new getData(url).execute();
        //SimpleAdapter adapter = new SimpleAdapter(tcpActivity.this,list,android.R.layout.simple_list_item_2,tcp_k,tcp_obj)
        listView.setAdapter(adapter);


    }



    private class getData extends AsyncTask<Void, Void, String> {

        String url="";

        public getData (String url) {
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
                Toast.makeText(tcpActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    Gson gson = new Gson();
                     list = gson.fromJson(result,new TypeToken<List<Map<String,Object>>>(){
                    }.getType());
                    if (list.get(0) !=null) {

                        Toast.makeText(tcpActivity.this,"获取数据成功",Toast.LENGTH_SHORT).show();
                       for (Map<String, Object> m:list)
                        {
                            for (String k : m.keySet())
                            {
                                tcp_obj.add(m.get(k).toString());
                                tcp_k.add(k);
                            }
                        }

                        Toast.makeText(tcpActivity.this, tcp_k.toString(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(tcpActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JsonIOException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    Toast.makeText(tcpActivity.this,"工程师正在努力修改",Toast.LENGTH_SHORT).show();
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
