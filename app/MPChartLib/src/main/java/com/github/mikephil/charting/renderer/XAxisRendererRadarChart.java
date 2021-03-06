package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.PointF;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class XAxisRendererRadarChart extends XAxisRenderer {
    private RadarChart mChart;

    public XAxisRendererRadarChart(ViewPortHandler viewPortHandler, XAxis xAxis, RadarChart chart) {
        super(viewPortHandler, xAxis, null);
        mChart = chart;
    }

    @Override
    public void renderAxisLabels(Canvas c) {
        if (!mXAxis.isEnabled() || !mXAxis.isDrawLabelsEnabled())
            return;
        final float labelRotationAngleDegrees = mXAxis.getLabelRotationAngle();
        final MPPointF drawLabelAnchor = MPPointF.getInstance(0.5f, 0.25f);
        mAxisLabelPaint.setTypeface(mXAxis.getTypeface());
        mAxisLabelPaint.setTextSize(mXAxis.getTextSize());
        mAxisLabelPaint.setColor(mXAxis.getTextColor());
        float sliceangle = mChart.getSliceAngle();
        float factor = mChart.getFactor();
        MPPointF center = mChart.getCenterOffsets();
        MPPointF pOut = MPPointF.getInstance(0, 0);
        for (int i = 0; i < mChart.getData().getMaxEntryCountSet().getEntryCount(); i++) {
            String label = mXAxis.getValueFormatter().getFormattedValue(i, mXAxis);
            float angle = (sliceangle * i + mChart.getRotationAngle()) % 360f;
            Utils.getPosition(center, mChart.getYRange() * factor
                    + mXAxis.mLabelRotatedWidth 2.f,
                    drawLabelAnchor, labelRotationAngleDegrees);
        }
        MPPointF.recycleInstance(center);
        MPPointF.recycleInstance(pOut);
        MPPointF.recycleInstance(drawLabelAnchor);
    }

    @Override
    public void renderLimitLines(Canvas c) {
    }
}
