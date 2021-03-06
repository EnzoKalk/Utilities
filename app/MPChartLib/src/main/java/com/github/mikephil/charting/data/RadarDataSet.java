package com.github.mikephil.charting.data;

import android.graphics.Color;

import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class RadarDataSet extends LineRadarDataSet<RadarEntry> implements IRadarDataSet {
    protected boolean mDrawHighlightCircleEnabled = false;
    protected int mHighlightCircleFillColor = Color.WHITE;
    protected int mHighlightCircleStrokeColor = ColorTemplate.COLOR_NONE;
    protected int mHighlightCircleStrokeAlpha = (int) (0.3 * 255);
    protected float mHighlightCircleInnerRadius = 3.0f;
    protected float mHighlightCircleOuterRadius = 4.0f;
    protected float mHighlightCircleStrokeWidth = 2.0f;

    public RadarDataSet(List<RadarEntry> yVals, String label) {
        super(yVals, label);
    }

    @Override
    public boolean isDrawHighlightCircleEnabled() {
        return mDrawHighlightCircleEnabled;
    }

    @Override
    public void setDrawHighlightCircleEnabled(boolean enabled) {
        mDrawHighlightCircleEnabled = enabled;
    }

    @Override
    public int getHighlightCircleFillColor() {
        return mHighlightCircleFillColor;
    }

    public void setHighlightCircleFillColor(int color) {
        mHighlightCircleFillColor = color;
    }

    @Override
    public int getHighlightCircleStrokeColor() {
        return mHighlightCircleStrokeColor;
    }

    public void setHighlightCircleStrokeColor(int color) {
        mHighlightCircleStrokeColor = color;
    }

    @Override
    public int getHighlightCircleStrokeAlpha() {
        return mHighlightCircleStrokeAlpha;
    }

    public void setHighlightCircleStrokeAlpha(int alpha) {
        mHighlightCircleStrokeAlpha = alpha;
    }

    @Override
    public float getHighlightCircleInnerRadius() {
        return mHighlightCircleInnerRadius;
    }

    public void setHighlightCircleInnerRadius(float radius) {
        mHighlightCircleInnerRadius = radius;
    }

    @Override
    public float getHighlightCircleOuterRadius() {
        return mHighlightCircleOuterRadius;
    }

    public void setHighlightCircleOuterRadius(float radius) {
        mHighlightCircleOuterRadius = radius;
    }

    @Override
    public float getHighlightCircleStrokeWidth() {
        return mHighlightCircleStrokeWidth;
    }

    public void setHighlightCircleStrokeWidth(float strokeWidth) {
        mHighlightCircleStrokeWidth = strokeWidth;
    }

    @Override
    public DataSet<RadarEntry> copy() {
        List<RadarEntry> entries = new ArrayList<RadarEntry>();
        for (int i = 0; i < mEntries.size(); i++) {
            entries.add(mEntries.get(i).copy());
        }
        RadarDataSet copied = new RadarDataSet(entries, getLabel());
        copy(copied);
        return copied;
    }

    protected void copy(RadarDataSet radarDataSet) {
        super.copy(radarDataSet);
        radarDataSet.mDrawHighlightCircleEnabled = mDrawHighlightCircleEnabled;
        radarDataSet.mHighlightCircleFillColor = mHighlightCircleFillColor;
        radarDataSet.mHighlightCircleInnerRadius = mHighlightCircleInnerRadius;
        radarDataSet.mHighlightCircleStrokeAlpha = mHighlightCircleStrokeAlpha;
        radarDataSet.mHighlightCircleStrokeColor = mHighlightCircleStrokeColor;
        radarDataSet.mHighlightCircleStrokeWidth = mHighlightCircleStrokeWidth;
    }
}
