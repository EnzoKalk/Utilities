package com.github.mikephil.charting.formatter;

import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;

public interface ColorFormatter {
    int getColor(int index, Entry e, IDataSet set);
}