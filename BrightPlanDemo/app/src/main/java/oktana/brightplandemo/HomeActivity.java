package oktana.brightplandemo;

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
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener,
        OnChartValueSelectedListener {

    private PieChart chart;
    private SeekBar seekBar;
    private TextView riskTolerance;

    private Typeface tf;

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

        int initProgress = 100;
        seekBar.setProgress(initProgress);
        riskTolerance.setText(String.valueOf(initProgress));

        seekBar.setOnSeekBarChangeListener(this);

        chart = (PieChart) findViewById(R.id.chart);
        chart.setUsePercentValues(false);
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

        setData(getResources().getStringArray(R.array.categories).length, 100);

        chart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = chart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        riskTolerance.setText(String.valueOf(seekBar.getProgress()));

        setData(getResources().getStringArray(R.array.categories).length, seekBar.getProgress());
    }

    private void setData(int count, float range) {

        // ****** GENERATE THE DATA SET ******
        ArrayList<Entry> categoriesRisk = new ArrayList<>();
        int[] categoriesRiskArray = getResources().getIntArray(R.array.categories_risks);

        for (int i = 0; i < count ; i++) {
            categoriesRisk.add(new Entry(categoriesRiskArray[i], i));
//            categoriesRisk.add(new Entry((float) (Math.random() * range) + range/ 5, i));
        }

        PieDataSet dataSet = new PieDataSet(categoriesRisk, null);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        // ****** END OF GENERATE THE DATA SET ******

        // ADD SOME COLORS
        dataSet.setColors(generateColors());

        // ****** ASSOCIATE THE DATA SET WITH THE CATEGORIES NAMES ******
        PieData data = new PieData(getResources().getStringArray(R.array.categories), dataSet);
        data.setValueFormatter(new LargeValueFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(tf);
        chart.setData(data);
        // ****** END OF ASSOCIATE THE DATA SET WITH THE CATEGORIES NAMES******

        // UNDO ALL HIGHLIGHTS
        chart.highlightValues(null);

        chart.invalidate();
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

        SpannableString s = new SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
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
