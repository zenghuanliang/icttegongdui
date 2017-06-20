package chartmanager;

import android.content.Context;
import android.graphics.Color;

import com.example.administrator.myapplication.R;
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

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/4.
 */

public class CombinedChartMagnager {
    private static String lineName = null;
    private static String barName = null;

    public static void initConbinedChart(Context context, CombinedChart mCombinedChart, ArrayList<String> xValues,
                                         ArrayList<Entry> line_yValue, ArrayList<BarEntry> bar_yValue, float left_maxValue, float left_minValue, float right_maxValue, float right_minValue) {
        mCombinedChart.setDrawOrder(new CombinedChart.DrawOrder[] {
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE
        });
        initDataStyle(context, mCombinedChart, left_maxValue, left_minValue,right_maxValue,right_minValue);
        LineDataSet lineDataSet = new LineDataSet(line_yValue, lineName);
        lineDataSet.setColor(Color.rgb(00, 128, 255));
        lineDataSet.setLineWidth(2.5f);
        lineDataSet.setCircleColor(Color.rgb(00, 128, 255));
        lineDataSet.setCircleRadius(5f);
        lineDataSet.setFillColor(Color.rgb(00, 128, 255));
        lineDataSet.setDrawCubic(true);
        lineDataSet.setDrawValues(true);
        lineDataSet.setValueTextSize(10f);
        lineDataSet.setValueTextColor(Color.rgb(00, 128, 255));
        lineDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet);

        BarDataSet barDataSet = new BarDataSet(bar_yValue, barName);
        barDataSet.setColor(Color.rgb(9,202,214));
        barDataSet.setValueTextColor(Color.rgb(9,202,214));
        barDataSet.setValueTextSize(10f);
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        BarData barData = new BarData();
        barData.addDataSet(barDataSet);

        CombinedData data = new CombinedData(xValues);
        data.setData(lineData);
        data.setData(barData);

        mCombinedChart.setData(data);
        mCombinedChart.invalidate();
    }
    public static void initDataStyle(Context context, CombinedChart mCombinedChart, float left_maxValue, float left_minValue, float right_maxValue, float right_minValue){

        mCombinedChart.setTouchEnabled(true);
        mCombinedChart.setScaleEnabled(false);
        //设置点击折线点时，显示其数值
        MyMarkerView mv = new MyMarkerView(context, R.layout.content_marker_view);
        mCombinedChart.setMarkerView(mv);
        mCombinedChart.setBackgroundColor(Color.WHITE);
        mCombinedChart.setDrawGridBackground(false);
        mCombinedChart.setDrawBarShadow(false);
        // draw bars behind lines

     /*         * 获取头部信息         */
        setLegend(mCombinedChart.getLegend(), true);
        /*          *Y轴 右侧数据设置     */
        setyAxis(mCombinedChart.getAxisRight(), false);
        /*          *Y轴 左侧数据设置     */
        setLeftYAxis(mCombinedChart.getAxisLeft(), true, left_maxValue, left_minValue);
        setRightYAxis(mCombinedChart.getAxisRight(),true,right_maxValue,right_minValue);
        /*          *X轴    数据设置      */
        setxAxis(mCombinedChart.getXAxis(), true);

    }

    public static void setLineName(String name){
        lineName = name;
    }

    public static void setBarName(String name){
        barName = name;
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
            leftAxis.setLabelCount(6, true);
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
            rightAxis.setLabelCount(6, true);
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
