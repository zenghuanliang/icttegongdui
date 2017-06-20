package chartmanager;

/**
 * Created by Administrator on 2017/5/26.
 */

import android.content.Context;
import android.graphics.Color;

import com.example.administrator.myapplication.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class LineChartManager {

    private static String lineName = null;
    private static String lineName1 = null;
    private static String lineName2 = null;
    private static String lineName3 = null;
    private static String lineName4 = null;


    /**
     * @Description:创建两条折线
     * @param context 上下文
     * @param mLineChart 折线图控件
     * @param xValues 折线在x轴的值
     * @param yValue 折线在y轴的值
     */
    public static void initSingleLineChart(Context context, LineChart mLineChart, ArrayList<String> xValues,
                                           ArrayList<Entry> yValue,float maxValue,float minValue) {
        initDataStyle(context,mLineChart,maxValue,minValue);
        //设置折线的样式

        LineDataSet lineDataSet = new LineDataSet(yValue, lineName);
        lineDataSet.setColor(Color.parseColor("#0080FF"));
        lineDataSet.setCircleColor(Color.parseColor("#0080FF"));

        LineData lineData = new LineData(xValues, lineDataSet);

        //将数据插入
        mLineChart.setData(lineData);

        //设置动画效果
        mLineChart.animateX(2000, Easing.EasingOption.Linear);
        mLineChart.invalidate();
        mLineChart.setDrawBorders(true);//是否添加边框
        mLineChart.setNoDataTextDescription("我需要数据");//没数据显示
        //lineChart.setDrawGridBackground(true);//是否显示表格颜色
        //lineChart.setBackgroundColor(Color.YELLOW);//背景颜色


    }
    /**
     * @Description:创建两条折线
     * @param context 上下文
     * @param mLineChart 折线图控件
     * @param xValues 折线在x轴的值
     * @param yValue 折线在y轴的值
     * @param yValue1 另一条折线在y轴的值
     */
    public static void initFiveLineChart(Context context, LineChart mLineChart, ArrayList<String> xValues,
                                           ArrayList<Entry> yValue, ArrayList<Entry> yValue1, ArrayList<Entry> yValue2,ArrayList<Entry> yValue3,ArrayList<Entry> yValue4,float maxValue,float minValue) {

        initDataStyle(context,mLineChart,maxValue,minValue);
        mLineChart.setNoDataTextDescription("暂时没有数据进行图表展示");
        mLineChart.setDescription("");
        mLineChart.setBackgroundColor(Color.WHITE);
        mLineChart.setDrawBorders(false);//是否画边框
        mLineChart.setDrawGridBackground(false);
        //mLineChart.setDrawLineShadow(false);
        mLineChart.setScaleYEnabled(false);
        mLineChart.setDoubleTapToZoomEnabled(false);
        setLegend(mLineChart.getLegend(), true);
        /*          *Y轴 右侧数据设置     */
        setyAxis(mLineChart.getAxisRight(), false);
        /*          *Y轴 左侧数据设置     */
        setLeftYAxis(mLineChart.getAxisLeft(), true, maxValue, minValue);
        setRightYAxis(mLineChart.getAxisRight(),true,maxValue,minValue);
        /*          *X轴    数据设置      */
        setxAxis(mLineChart.getXAxis(), true);
        //The first line.
        LineDataSet set1 = new LineDataSet(yValue, lineName);
        set1.setColor(Color.rgb(9,202,214));
        set1.setCircleColor(Color.rgb(9,202,214));
        set1.setLineWidth(1f);
        set1.setDrawValues(false);
        set1.setHighlightEnabled(true); //设置十字线 是否启用
        set1.setCircleRadius(1f);
        set1.setFillColor(Color.rgb(9,202,214));
        set1.setDrawCubic(true);
        set1.setDrawValues(true);
        set1.setValueTextSize(10f);
        set1.setValueTextColor(Color.rgb(9,202,214));
        set1.setHighLightColor(Color.TRANSPARENT);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        //The second line.
        LineDataSet set2 = new LineDataSet(yValue1, lineName1);
        set2.setColor(Color.rgb(00, 128, 255));
        set2.setCircleColor(Color.rgb(00, 128, 255));
        set2.setLineWidth(1f);
        set2.setDrawValues(false);
        set2.setHighlightEnabled(true); //设置十字线 是否启用
        set2.setCircleRadius(1f);
        set2.setFillColor(Color.rgb(00, 128, 255));
        set2.setDrawCubic(true);
        set2.setDrawValues(true);
        set2.setValueTextSize(10f);
        set2.setValueTextColor(Color.rgb(00, 128, 255));
        set2.setHighLightColor(Color.TRANSPARENT);
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        //The third line.
        LineDataSet set3 = new LineDataSet(yValue2, lineName2);
        set3.setColor(Color.rgb(255, 255, 0));
        set3.setCircleColor(Color.rgb(255, 255, 0));
        set3.setLineWidth(1f);
        set3.setDrawValues(false);
        set3.setHighlightEnabled(true); //设置十字线 是否启用
        set3.setHighLightColor(Color.TRANSPARENT);
        set3.setAxisDependency(YAxis.AxisDependency.LEFT);
        //The four line.
        LineDataSet set4 = new LineDataSet(yValue3, lineName3);
        set4.setColor(Color.rgb(255, 0, 0));
        set4.setCircleColor(Color.rgb(255, 0, 0));
        set4.setLineWidth(1f);
        set4.setHighlightEnabled(true); //设置十字线 是否启用
        set4.setCircleRadius(1f);
        set4.setFillColor(Color.rgb(255, 0, 0));
        set4.setDrawCubic(true);
        set4.setDrawValues(true);
        set4.setValueTextSize(10f);
        set4.setValueTextColor(Color.rgb(255, 0, 0));
        set4.setHighLightColor(Color.TRANSPARENT);
        set4.setAxisDependency(YAxis.AxisDependency.LEFT);
        //The five line.
        LineDataSet set5 = new LineDataSet(yValue4, lineName4);
        set5.setColor(Color.rgb(128,255,0));
        set5.setCircleColor(Color.rgb(128,255,0));
        set5.setLineWidth(1f);
        set5.setDrawValues(false);
        set5.setHighlightEnabled(true); //设置十字线 是否启用
        set5.setHighLightColor(Color.TRANSPARENT);
        set5.setAxisDependency(YAxis.AxisDependency.LEFT);

        //构建一个类型为LineDataSet的ArrayList 用来存放所有 y的LineDataSet   他是构建最终加入LineChart数据集所需要的参数
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        //将数据加入dataSets
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);
        dataSets.add(set4);
        dataSets.add(set5);
        //构建一个LineData  将dataSets放入
        LineData lineData = new LineData(xValues, dataSets);
        lineData.setValueTextSize(10f);
        //将数据插入
        mLineChart.setData(lineData);

        //设置动画效果
        //mLineChart.animateY(2000, Easing.EasingOption.Linear);
        //mLineChart.animateX(2000, Easing.EasingOption.Linear);
        mLineChart.invalidate();
    }

    /**
     *  @Description:初始化图表的样式
     * @param context
     * @param mLineChart
     */
    private static void initDataStyle(Context context, LineChart mLineChart,float maxValue,float minValue) {
        //设置图表是否支持触控操作
        mLineChart.setTouchEnabled(true);
        mLineChart.setScaleEnabled(false);

        //设置点击折线点时，显示其数值
        MyMarkerView mv = new MyMarkerView(context, R.layout.content_marker_view);
        mLineChart.setMarkerView(mv);
        //设置折线的描述的样式（默认在图表的左下角）
        Legend title = mLineChart.getLegend();

        title.setFormSize(12f);//字体
        title.setTextColor(Color.parseColor("#0080FF"));//设置颜色
        title.setFormSize(12f);//字体
        title.setForm(Legend.LegendForm.LINE);
        //设置x轴的样式
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setEnabled(true);
        setxAxis(xAxis,true);
        xAxis.setLabelsToSkip(1);
        setLeftYAxis(mLineChart.getAxisLeft(),true,maxValue,minValue);
        //设置右边y轴的样式
        YAxis yAxisRight = mLineChart.getAxisRight();
        yAxisRight.setEnabled(false);
    }

    /**
     * @Description:设置折线的名称
     * @param name
     */
    public static void setLineName(String name){
        lineName = name;
    }

    /**
     * @Description:设置另一条折线的名称
     * @param name
     */
    public static void setLineName1(String name){
        lineName1 = name;
    }
    public static void setLineName2(String name){
        lineName2 = name;
    }
    public static void setLineName3(String name){
        lineName3 = name;
    }
    public static void setLineName4(String name){
        lineName4 = name;
    }

    public static void setLegend(Legend l, boolean LegendEnabled) {
        if (LegendEnabled) {
            l.setTextColor(Color.BLUE);
            l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
            l.setForm(Legend.LegendForm.LINE);
        }
        l.setEnabled(LegendEnabled);
    }
    public static void setLeftYAxis(YAxis leftAxis, boolean yAxisEnabled, float maxValue, float minValue) {
        if (yAxisEnabled) {
            leftAxis.setDrawZeroLine(false);
            leftAxis.setAxisMaxValue(maxValue );
            leftAxis.setAxisMinValue(minValue);
            leftAxis.setTextColor(Color.BLUE);
            leftAxis.setDrawAxisLine(true);
            //颠倒显示
            //leftAxis.setInverted(true);
            //显示Y轴的坐标数 true:按平均值  false:按整数 y-axis max = 25, min = 2, default: 6,
            if (maxValue==1){
            leftAxis.setLabelCount(2, true);
            }
            if(maxValue==2){
                leftAxis.setLabelCount(3, true);
            }
            if(maxValue!=1&&maxValue!=2){
                leftAxis.setLabelCount(5, true);
            }
        }
        leftAxis.setEnabled(yAxisEnabled);
    }
    public static void setRightYAxis(YAxis rightAxis, boolean yAxisEnabled, float mmaxValue, float mminValue) {
        if (yAxisEnabled) {
            rightAxis.setDrawZeroLine(false);
            rightAxis.setAxisMaxValue(mmaxValue );
            rightAxis.setAxisMinValue(mminValue);
            rightAxis.setTextColor(Color.BLUE);
            rightAxis.setDrawAxisLine(true);
            //颠倒显示
            //leftAxis.setInverted(true);
            //显示Y轴的坐标数 true:按平均值  false:按整数 y-axis max = 25, min = 2, default: 6,
            rightAxis.setLabelCount(5, true);
        }
        rightAxis.setEnabled(yAxisEnabled);
    }
    public static void setyAxis(YAxis yAxis, boolean yAxisEnabled) {
        if (yAxisEnabled) {
            yAxis.setDrawAxisLine(true);
            yAxis.setDrawGridLines(true);
        }
        yAxis.setEnabled(yAxisEnabled);
    }
    public static void setxAxis(XAxis xAxis, boolean xAxisEnabled) {
        if (xAxisEnabled) {
            xAxis.setTextColor(Color.BLUE);
            xAxis.setDrawGridLines(false);
            xAxis.setAvoidFirstLastClipping(true);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        }
        xAxis.setEnabled(xAxisEnabled);
    }
}

