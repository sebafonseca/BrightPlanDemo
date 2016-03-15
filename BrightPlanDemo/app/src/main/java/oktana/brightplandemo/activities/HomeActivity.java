package oktana.brightplandemo.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import common.Utils;
import model.Category;
import model.Portfolio;
import model.Profile;
import oktana.brightplandemo.R;

public class HomeActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener,
        OnChartValueSelectedListener {

    private PieChart chart;
    private SeekBar seekBar;
    private TextView riskTolerance;
    private Typeface tf;
    private Portfolio portfolio = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_home);
        initViews();
    }

    private void initViews(){
        riskTolerance = (TextView) findViewById(R.id.riskTolerance);

        seekBar = (SeekBar) findViewById(R.id.seekBar);

        int initProgress = 1;
        seekBar.setProgress(initProgress);
        riskTolerance.setText(String.valueOf(initProgress));

        seekBar.setOnSeekBarChangeListener(this);

        chart = (PieChart) findViewById(R.id.chart);
        chart.setUsePercentValues(true);
        chart.setDescription("");
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);

        tf = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");

        chart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Light.ttf"));
        chart.setCenterText(generateCenterSpannableText());

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        // add a selection listener
        chart.setOnChartValueSelectedListener(this);

        populateData();

        setData(initProgress);

        chart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = chart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (progress == 0){
            seekBar.setProgress(1);
            return;
        }
        riskTolerance.setText(String.valueOf(seekBar.getProgress()));

        setData(seekBar.getProgress());
    }

    private void populateData(){

        try {
            Gson gson = new Gson();
            portfolio = gson.fromJson(Utils.loadJSONFromFile(getAssets(), "json/data.json"), Portfolio.class);
        }
        catch (Exception e){
            Log.i("JSON EXCEPTION", e.getMessage());
        }
    }

    private void setData(int risk) {

        if (portfolio != null) {
            // ****** SET THE DATA SET ******
            List<Category> categoriesList = portfolio.getCategoriesFromProfileIndex(risk);
            ArrayList<String> categories = new ArrayList<>();
            ArrayList<Entry> categoriesRisk = new ArrayList<>();
            int ind = 0;
            for (Category category: categoriesList){
                categories.add(ind, category.getName());
                categoriesRisk.add(new Entry(category.getLevel(), ind));
                ind ++;
            }

            PieDataSet dataSet = new PieDataSet(categoriesRisk, null);
            dataSet.setSliceSpace(3f);
            dataSet.setSelectionShift(5f);
            // ****** END OF SET THE DATA SET ******

            // ADD SOME COLORS
            dataSet.setColors(generateColors());

            // ****** ASSOCIATE THE DATA SET WITH THE CATEGORIES NAMES ******
            PieData data = new PieData(categories, dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.WHITE);
            data.setValueTypeface(tf);
            chart.setData(data);
            // ****** END OF ASSOCIATE THE DATA SET WITH THE CATEGORIES NAMES******

            // UNDO ALL HIGHLIGHTS
            chart.highlightValues(null);

            chart.invalidate();
        }
    }

    private ArrayList<Integer> generateColors(){
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        return colors;
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("My Portfolio\ndeveloped by Bright Plan Demo");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 12, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 12, s.length() - 13, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 12, s.length() - 13, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 12, s.length() - 16, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 16, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 16, s.length(), 0);
        return s;
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }
}
