package presenter;

import android.os.AsyncTask;
import android.util.Log;

import com.example.administrator.myapplication.JSONUtil1;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017/4/30.
 */

public class getTcpData extends AsyncTask<Void, Void, String> {
    String url="";

    public getTcpData (String url) {
        this.url = url;
    }

    /**请求网络数据并返回结果
     *
     */
    @Override
    protected String doInBackground(Void... voids) {
        String result = "";
        JSONUtil1 js = new JSONUtil1();
        try {
            result =js.getRequest(url) ;

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

            try {
                Gson gson = new Gson();
                List<Map<String,Object>> list = gson.fromJson(result,new TypeToken<List<Map<String,Object>>>(){
                }.getType());
                if (list.get(0) !=null) {
                    Log.d(TAG,"数据获取成功");
                } else {
                    Log.d(TAG,"数据获取成功");
                }
            } catch (JsonIOException e) {
                e.printStackTrace();
            }catch (Exception e){
               e.printStackTrace();
            }
        }
}
