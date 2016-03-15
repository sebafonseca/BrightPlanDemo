package common;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class CustomValueFormatter implements ValueFormatter {

    DecimalFormat mFormat;

    public CustomValueFormatter() {
        mFormat = new DecimalFormat();
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        if (value == 0){
            return "";
        }
        return mFormat.format(value) + " %";
    }
}
