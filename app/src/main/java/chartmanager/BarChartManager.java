package chartmanager;
import android.content.Context;
import android.graphics.Color;

import com.example.administrator.myapplication.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;

public class BarChartManager {
    private static String unit = null;
    private static String unit1 = null;
    private static String unit2 = null;
    private static String unit3 = null;
    private static String unit4 = null;

    public static void initSingleBarChart(Context context, BarChart barChart,
                                    ArrayList<String> xValues, ArrayList<BarEntry> yValues,float maxValue,float minValue) {
        initDataStyle(context,barChart,maxValue,minValue);

//        dataSet.setValueFormatter(new PercentFormatter(new DecimalFormat("%").format()));

        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, unit);

        barDataSet.setColor(Color.rgb(114, 188, 223));


        BarData barData = new BarData(xValues,barDataSet);

        barData.setValueTextSize(10f);
        barData.setGroupSpace(500f);//调节柱状图粗细 值越大越细
        barChart.setData(barData);
        barChart.setDrawHighlightArrow(true);
        barChart.invalidate();
    }

    public static void initFiveBarChart(Context context, BarChart barChart,
                                          ArrayList<String> xValues, ArrayList<BarEntry> yValues,ArrayList<BarEntry> yValues1,ArrayList<BarEntry> yValues2,ArrayList<BarEntry> yValues3,ArrayList<BarEntry> yValues4,float maxValue,float minValue) {
        initDataStyle(context,barChart,maxValue,minValue);

//        dataSet.setValueFormatter(new PercentFormatter(new DecimalFormat("%").format()));

        // y轴的数据集合
        BarDataSet set1 = new BarDataSet(yValues, unit);
        set1.setColor(Color.parseColor("#4F81BD"));
        set1.setValueTextColor(Color.parseColor("#4F81BD"));
        set1.setDrawValues(false);
        set1.setHighlightEnabled(true); //设置十字线 是否启用
        set1.setHighLightColor(Color.parseColor("#4F81BD"));

        BarDataSet set2 = new BarDataSet(yValues1, unit1);
        set2.setColor(Color.parseColor("#C0504D"));
        set2.setValueTextColor(Color.parseColor("#C0504D"));
        set2.setDrawValues(false);
        set2.setHighlightEnabled(true);
        set2.setHighLightColor(Color.parseColor("#C0504D"));

        BarDataSet set3 = new BarDataSet(yValues2, unit2);
        set3.setColor(Color.parseColor("#9BBA59"));
        set3.setValueTextColor(Color.parseColor("#9BBA59"));
        set3.setDrawValues(false);
        set3.setHighlightEnabled(true);
        set3.setHighLightColor(Color.parseColor("#9BBA59"));

        BarDataSet set4 = new BarDataSet(yValues3, unit3);
        set4.setColor(Color.parseColor("#8064A2"));
        set4.setValueTextColor(Color.parseColor("#8064A2"));
        set4.setDrawValues(false);
        set4.setHighlightEnabled(true);
        set4.setHighLightColor(Color.parseColor("#8064A2"));
        set4.setHighLightAlpha(50);
        set4.setAxisDependency(YAxis.AxisDependency.LEFT);

        BarDataSet set5 = new BarDataSet(yValues4, unit4);
        set5.setColor(Color.parseColor("#4BACC6"));
        set5.setValueTextColor(Color.parseColor("#4BACC6"));
        set5.setDrawValues(false);
        set5.setHighlightEnabled(true);
        set5.setHighLightColor(Color.parseColor("#4BACC6"));
        set5.setHighLightAlpha(50);
        set5.setAxisDependency(YAxis.AxisDependency.LEFT);


        ArrayList<IBarDataSet> barDataSets = new ArrayList<>();
        barDataSets.add(set1);
        barDataSets.add(set2);
        barDataSets.add(set3);
        barDataSets.add(set4);
        barDataSets.add(set5);

        BarData barData = new BarData(xValues,barDataSets);

        barData.setValueTextSize(10f);
        barData.setGroupSpace(500f);//调节柱状图粗细 值越大越细
        barChart.setData(barData);
        barChart.setDrawHighlightArrow(true);
        barChart.invalidate();
    }

    /**
     *  @Description:初始化图表的样式
     * @param context
     * @param barChart
     */
    private static void initDataStyle(Context context, BarChart barChart,float maxValue,float minValue) {
        //设置图表是否支持触控操作
        barChart.setTouchEnabled(true);
        barChart.setNoDataTextDescription("暂时没有数据进行图表展示");
        barChart.setDragEnabled(true);// 是否可以拖拽
        barChart.setScaleEnabled(true);// 是否可以缩放
        barChart.setPinchZoom(false);//
        barChart.setDescription("");
        barChart.setBackgroundColor(Color.WHITE);
        barChart.setDrawBorders(false);//是否画边框
        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);
        barChart.setScaleYEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);
        //设置点击折线点时，显示其数值
        MyMarkerView mv = new MyMarkerView(context, R.layout.content_marker_view);
        barChart.setMarkerView(mv);
        /*         * 获取头部信息         */
        setLegend(barChart.getLegend(), true);
        /*          *Y轴 右侧数据设置     */
        setyAxis(barChart.getAxisRight(), false);
        /*          *Y轴 左侧数据设置     */
        setLeftYAxis(barChart.getAxisLeft(), true, maxValue, minValue);
        setRightYAxis(barChart.getAxisRight(),true,maxValue,minValue);
        /*          *X轴    数据设置      */
        setxAxis(barChart.getXAxis(), true);

        barChart.setDragEnabled(true);// 是否可以拖拽
        barChart.setScaleEnabled(true);// 是否可以缩放

        barChart.setPinchZoom(false);//

//      barChart.setBackgroundColor();// 设置背景

        barChart.setDrawBarShadow(true);


    }

    /**
     * 设置单位值
     * @param barUnit
     */
    public static void  setUnit(String barUnit){
        unit = barUnit;
    }
    public static void  setUnit1(String barUnit){
        unit1 = barUnit;
    }
    public static void  setUnit2(String barUnit){
        unit2 = barUnit;
    }
    public static void  setUnit3(String barUnit){
        unit3 = barUnit;
    }
    public static void  setUnit4(String barUnit){
        unit4 = barUnit;
    }


    public static void setLegend(Legend l, boolean LegendEnabled) {
        if (LegendEnabled) {
            l.setTextColor(Color.BLUE);
            l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
            l.setForm(Legend.LegendForm.CIRCLE);
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