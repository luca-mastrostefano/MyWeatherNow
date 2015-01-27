package com.example.myweathernow.util;

import android.graphics.Color;
import android.util.Log;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.YLabels;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lucamastrostefano on 27/01/15.
 */
public class ChartUtil {

    public static void initializeTransparentGraph(LineChart chart){
        chart.setDescription("");
        chart.setNoDataTextDescription("Updating...");
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        chart.setTouchEnabled(false);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setFocusable(false);
        chart.setClickable(true);
        chart.setFocusableInTouchMode(false);
        chart.animateXY(1000,1000);
    }

    public static void fill(LineChart chart, List<WeatherInfo> details, boolean isProbChart){
        if(isProbChart){
            Log.i("ChartUtil", "filling probabilities chart");
            fillProbabilitiesChart(chart, details);
        }else{
            Log.i("ChartUtil", "filling intensities chart");
            fillIntensitiesChart(chart, details);
        }
    }

    private static void fillProbabilitiesChart(LineChart chart, List<WeatherInfo> details){
        chart.animateXY(1000,1000);
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < details.size(); i++) {
            xVals.add((i) + "h");
        }
        ArrayList<Entry> probabilities = new ArrayList<Entry>();
        for (int i = 0; i < details.size(); i++) {
            WeatherInfo weatherInfo = details.get(i);
            probabilities.add(new Entry((float)weatherInfo.getRainProbability(), i));
        }
        Map.Entry<String, String> test = new AbstractMap.SimpleEntry<String, String>("", "");

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(probabilities, "DataSet 1");
        set1.setColor(Color.parseColor("yellow"));
        set1.setCircleColor(Color.parseColor("yellow"));
        set1.setLineWidth(2f);
        set1.setCircleSize(4f);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);
        chart.setYRange(0, 1, true);
        chart.getYLabels().setPosition(YLabels.YLabelPosition.LEFT);
        chart.setDrawYValues(false);
        chart.setData(data);
        chart.refreshDrawableState();
    }

    private static void fillIntensitiesChart(LineChart chart, List<WeatherInfo> details){
        chart.animateXY(1000,1000);
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < details.size(); i++) {
            xVals.add((i) + "h");
        }
        ArrayList<Entry> intensities = new ArrayList<Entry>();
        for (int i = 0; i < details.size(); i++) {
            WeatherInfo weatherInfo = details.get(i);
            intensities.add(new Entry((float)weatherInfo.getRainIntensity(), i));
        }
        // create a dataset and give it a type
        LineDataSet set2 = new LineDataSet(intensities, "DataSet 1");
        set2.setColor(Color.parseColor("red"));
        set2.setCircleColor(Color.parseColor("red"));
        set2.setLineWidth(2f);
        set2.setCircleSize(4f);
        set2.setFillAlpha(65);
        set2.setFillColor(ColorTemplate.getHoloBlue());
        set2.setHighLightColor(Color.rgb(244, 117, 117));
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set2); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);
        //chart.resetYRange(true);
        chart.getYLabels().setPosition(YLabels.YLabelPosition.RIGHT);
        chart.setDrawYValues(false);
        chart.setDrawXLabels(false);
        chart.setData(data);
        chart.refreshDrawableState();
    }
}
