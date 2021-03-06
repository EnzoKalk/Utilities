package com.github.mikephil.charting.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.lang.ref.WeakReference;
import java.util.List;

public class PieChartRenderer extends DataRenderer {
    protected PieChart mChart;
    protected Paint mHolePaint;
    protected Paint mTransparentCirclePaint;
    protected Paint mValueLinePaint;
    protected WeakReference<Bitmap> mDrawBitmap;
    protected Canvas mBitmapCanvas;
    protected Path mDrawCenterTextPathBuffer = new Path();
    protected RectF mDrawHighlightedRectF = new RectF();
    private TextPaint mCenterTextPaint;
    private Paint mEntryLabelsPaint;
    private StaticLayout mCenterTextLayout;
    private CharSequence mCenterTextLastValue;
    private RectF mCenterTextLastBounds = new RectF();
    private RectF[] mRectBuffer = {new RectF(), new RectF(), new RectF()};
    private Path mPathBuffer = new Path();
    private RectF mInnerRectBuffer = new RectF();
    private Path mHoleCirclePath = new Path();

    public PieChartRenderer(PieChart chart, ChartAnimator animator,
                            ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
        mChart = chart;
        mHolePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHolePaint.setColor(Color.WHITE);
        mHolePaint.setStyle(Style.FILL);
        mTransparentCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTransparentCirclePaint.setColor(Color.WHITE);
        mTransparentCirclePaint.setStyle(Style.FILL);
        mTransparentCirclePaint.setAlpha(105);
        mCenterTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mCenterTextPaint.setColor(Color.BLACK);
        mCenterTextPaint.setTextSize(Utils.convertDpToPixel(12f));
        mValuePaint.setTextSize(Utils.convertDpToPixel(13f));
        mValuePaint.setColor(Color.WHITE);
        mValuePaint.setTextAlign(Align.CENTER);
        mEntryLabelsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mEntryLabelsPaint.setColor(Color.WHITE);
        mEntryLabelsPaint.setTextAlign(Align.CENTER);
        mEntryLabelsPaint.setTextSize(Utils.convertDpToPixel(13f));
        mValueLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mValueLinePaint.setStyle(Style.STROKE);
    }

    public Paint getPaintHole() {
        return mHolePaint;
    }

    public Paint getPaintTransparentCircle() {
        return mTransparentCirclePaint;
    }

    public TextPaint getPaintCenterText() {
        return mCenterTextPaint;
    }

    public Paint getPaintEntryLabels() {
        return mEntryLabelsPaint;
    }

    @Override
    public void initBuffers() {
    }

    @Override
    public void drawData(Canvas c) {
        int width = (int) mViewPortHandler.getChartWidth();
        int height = (int) mViewPortHandler.getChartHeight();
        Bitmap drawBitmap = mDrawBitmap == null ? null : mDrawBitmap.get();
        if (drawBitmap == null
                || (drawBitmap.getWidth() != width)
                || (drawBitmap.getHeight() != height)) {
            if (width > 0 && height > 0) {
                drawBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
                mDrawBitmap = new WeakReference<>(drawBitmap);
                mBitmapCanvas = new Canvas(drawBitmap);
            } else
                return;
        }
        drawBitmap.eraseColor(Color.TRANSPARENT);
        PieData pieData = mChart.getData();
        for (IPieDataSet set : pieData.getDataSets()) {
            if (set.isVisible() && set.getEntryCount() > 0)
                drawDataSet(c, set);
        }
    }

    protected float calculateMinimumRadiusForSpacedSlice(
            MPPointF center,
            float radius,
            float angle,
            float arcStartPointX,
            float arcStartPointY,
            float startAngle,
            float sweepAngle) {
        final float angleMiddle = startAngle + sweepAngle / 2.f;
        float arcEndPointX = center.x + radius * (float) Math.cos((startAngle + sweepAngle) * Utils.FDEG2RAD);
        float arcEndPointY = center.y + radius * (float) Math.sin((startAngle + sweepAngle) * Utils.FDEG2RAD);
        float arcMidPointX = center.x + radius * (float) Math.cos(angleMiddle * Utils.FDEG2RAD);
        float arcMidPointY = center.y + radius * (float) Math.sin(angleMiddle * Utils.FDEG2RAD);
        double basePointsDistance = Math.sqrt(
                Math.pow(arcEndPointX - arcStartPointX, 2) +
                        Math.pow(arcEndPointY - arcStartPointY, 2));
        float containedTriangleHeight = (float) (basePointsDistance / 2.0 *
                Math.tan((180.0 - angle) / 2.0 * Utils.DEG2RAD));
        float spacedRadius = radius - containedTriangleHeight;
        spacedRadius -= Math.sqrt(
                Math.pow(arcMidPointX - (arcEndPointX + arcStartPointX)mChart.getData().getYValueSum() * 2;
        float sliceSpace = spaceSizeRatio > minValueRatio ? 0f : dataSet.getSliceSpace();
        return sliceSpace;
    }

    protected void drawDataSet(Canvas c, IPieDataSet dataSet) {
        float angle = 0;
        float rotationAngle = mChart.getRotationAngle();
        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();
        final RectF circleBox = mChart.getCircleBox();
        final int entryCount = dataSet.getEntryCount();
        final float[] drawAngles = mChart.getDrawAngles();
        final MPPointF center = mChart.getCenterCircleBox();
        final float radius = mChart.getRadius();
        final boolean drawInnerArc = mChart.isDrawHoleEnabled() && !mChart.isDrawSlicesUnderHoleEnabled();
        final float userInnerRadius = drawInnerArc
                ? radius * (mChart.getHoleRadius() / 100.f)
                : 0.f;
        final float roundedRadius = (radius - (radius * mChart.getHoleRadius() 2f;
        final RectF roundedCircleBox = new RectF();
        final boolean drawRoundedSlices = drawInnerArc && mChart.isDrawRoundedSlicesEnabled();
        int visibleAngleCount = 0;
        for (int j = 0; j < entryCount; j++) {
            if ((Math.abs(dataSet.getEntryForIndex(j).getY()) > Utils.FLOAT_EPSILON)) {
                visibleAngleCount++;
            }
        }
        final float sliceSpace = visibleAngleCount <= 1 ? 0.f : getSliceSpace(dataSet);
        for (int j = 0; j < entryCount; j++) {
            float sliceAngle = drawAngles[j];
            float innerRadius = userInnerRadius;
            Entry e = dataSet.getEntryForIndex(j);
            if (!(Math.abs(e.getY()) > Utils.FLOAT_EPSILON)) {
                angle += sliceAngle * phaseX;
                continue;
            }
            if (dataSet.isHighlightEnabled() && mChart.needsHighlight(j) && !drawRoundedSlices) {
                angle += sliceAngle * phaseX;
                continue;
            }
            final boolean accountForSliceSpacing = sliceSpace > 0.f && sliceAngle <= 180.f;
            mRenderPaint.setColor(dataSet.getColor(j));
            final float sliceSpaceAngleOuter = visibleAngleCount == 1 ?
                    0.f :
                    sliceSpace / (Utils.FDEG2RAD * radius);
            final float startAngleOuter = rotationAngle + (angle + sliceSpaceAngleOuter / 2.f) * phaseY;
            float sweepAngleOuter = (sliceAngle - sliceSpaceAngleOuter) * phaseY;
            if (sweepAngleOuter < 0.f) {
                sweepAngleOuter = 0.f;
            }
            mPathBuffer.reset();
            if (drawRoundedSlices) {
                float x = center.x + (radius - roundedRadius) * (float) Math.cos(startAngleOuter * Utils.FDEG2RAD);
                float y = center.y + (radius - roundedRadius) * (float) Math.sin(startAngleOuter * Utils.FDEG2RAD);
                roundedCircleBox.set(x - roundedRadius, y - roundedRadius, x + roundedRadius, y + roundedRadius);
            }
            float arcStartPointX = center.x + radius * (float) Math.cos(startAngleOuter * Utils.FDEG2RAD);
            float arcStartPointY = center.y + radius * (float) Math.sin(startAngleOuter * Utils.FDEG2RAD);
            if (sweepAngleOuter >= 360.f && sweepAngleOuter % 360f <= Utils.FLOAT_EPSILON) {
                mPathBuffer.addCircle(center.x, center.y, radius, Path.Direction.CW);
            } else {
                if (drawRoundedSlices) {
                    mPathBuffer.arcTo(roundedCircleBox, startAngleOuter + 180, -180);
                }
                mPathBuffer.arcTo(
                        circleBox,
                        startAngleOuter,
                        sweepAngleOuter
                );
            }
            mInnerRectBuffer.set(
                    center.x - innerRadius,
                    center.y - innerRadius,
                    center.x + innerRadius,
                    center.y + innerRadius);
            if (drawInnerArc && (innerRadius > 0.f || accountForSliceSpacing)) {
                if (accountForSliceSpacing) {
                    float minSpacedRadius =
                            calculateMinimumRadiusForSpacedSlice(
                                    center, radius,
                                    sliceAngle * phaseY,
                                    arcStartPointX, arcStartPointY,
                                    startAngleOuter,
                                    sweepAngleOuter);
                    if (minSpacedRadius < 0.f)
                        minSpacedRadius = -minSpacedRadius;
                    innerRadius = Math.max(innerRadius, minSpacedRadius);
                }
                final float sliceSpaceAngleInner = visibleAngleCount == 1 || innerRadius == 0.f ?
                        0.f :
                        sliceSpace / (Utils.FDEG2RAD * innerRadius);
                final float startAngleInner = rotationAngle + (angle + sliceSpaceAngleInner / 2.f) * phaseY;
                float sweepAngleInner = (sliceAngle - sliceSpaceAngleInner) * phaseY;
                if (sweepAngleInner < 0.f) {
                    sweepAngleInner = 0.f;
                }
                final float endAngleInner = startAngleInner + sweepAngleInner;
                if (sweepAngleOuter >= 360.f && sweepAngleOuter % 360f <= Utils.FLOAT_EPSILON) {
                    mPathBuffer.addCircle(center.x, center.y, innerRadius, Path.Direction.CCW);
                } else {
                    if (drawRoundedSlices) {
                        float x = center.x + (radius - roundedRadius) * (float) Math.cos(endAngleInner * Utils.FDEG2RAD);
                        float y = center.y + (radius - roundedRadius) * (float) Math.sin(endAngleInner * Utils.FDEG2RAD);
                        roundedCircleBox.set(x - roundedRadius, y - roundedRadius, x + roundedRadius, y + roundedRadius);
                        mPathBuffer.arcTo(roundedCircleBox, endAngleInner, 180);
                    } else
                        mPathBuffer.lineTo(
                                center.x + innerRadius * (float) Math.cos(endAngleInner * Utils.FDEG2RAD),
                                center.y + innerRadius * (float) Math.sin(endAngleInner * Utils.FDEG2RAD));
                    mPathBuffer.arcTo(
                            mInnerRectBuffer,
                            endAngleInner,
                            -sweepAngleInner
                    );
                }
            } else {
                if (sweepAngleOuter % 360f > Utils.FLOAT_EPSILON) {
                    if (accountForSliceSpacing) {
                        float angleMiddle = startAngleOuter + sweepAngleOuter / 2.f;
                        float sliceSpaceOffset =
                                calculateMinimumRadiusForSpacedSlice(
                                        center,
                                        radius,
                                        sliceAngle * phaseY,
                                        arcStartPointX,
                                        arcStartPointY,
                                        startAngleOuter,
                                        sweepAngleOuter);
                        float arcEndPointX = center.x +
                                sliceSpaceOffset * (float) Math.cos(angleMiddle * Utils.FDEG2RAD);
                        float arcEndPointY = center.y +
                                sliceSpaceOffset * (float) Math.sin(angleMiddle * Utils.FDEG2RAD);
                        mPathBuffer.lineTo(
                                arcEndPointX,
                                arcEndPointY);
                    } else {
                        mPathBuffer.lineTo(
                                center.x,
                                center.y);
                    }
                }
            }
            mPathBuffer.close();
            mBitmapCanvas.drawPath(mPathBuffer, mRenderPaint);
            angle += sliceAngle * phaseX;
        }
        MPPointF.recycleInstance(center);
    }

    @Override
    public void drawValues(Canvas c) {
        MPPointF center = mChart.getCenterCircleBox();
        float radius = mChart.getRadius();
        float rotationAngle = mChart.getRotationAngle();
        float[] drawAngles = mChart.getDrawAngles();
        float[] absoluteAngles = mChart.getAbsoluteAngles();
        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();
        final float roundedRadius = (radius - (radius * mChart.getHoleRadius() 10f * 3.6f;
        if (mChart.isDrawHoleEnabled()) {
            labelRadiusOffset = (radius - (radius * holeRadiusPercent)) / 2f;
            if (!mChart.isDrawSlicesUnderHoleEnabled() && mChart.isDrawRoundedSlicesEnabled()) {
                rotationAngle += roundedRadius * 360 / (Math.PI * 2 * radius);
            }
        }
        final float labelRadius = radius - labelRadiusOffset;
        PieData data = mChart.getData();
        List<IPieDataSet> dataSets = data.getDataSets();
        float yValueSum = data.getYValueSum();
        boolean drawEntryLabels = mChart.isDrawEntryLabelsEnabled();
        float angle;
        int xIndex = 0;
        c.save();
        float offset = Utils.convertDpToPixel(5.f);
        for (int i = 0; i < dataSets.size(); i++) {
            IPieDataSet dataSet = dataSets.get(i);
            final boolean drawValues = dataSet.isDrawValuesEnabled();
            if (!drawValues && !drawEntryLabels)
                continue;
            final PieDataSet.ValuePosition xValuePosition = dataSet.getXValuePosition();
            final PieDataSet.ValuePosition yValuePosition = dataSet.getYValuePosition();
            applyValueTextStyle(dataSet);
            float lineHeight = Utils.calcTextHeight(mValuePaint, "Q")
                    + Utils.convertDpToPixel(4f);
            IValueFormatter formatter = dataSet.getValueFormatter();
            int entryCount = dataSet.getEntryCount();
            boolean isUseValueColorForLineEnabled = dataSet.isUseValueColorForLineEnabled();
            int valueLineColor = dataSet.getValueLineColor();
            mValueLinePaint.setStrokeWidth(Utils.convertDpToPixel(dataSet.getValueLineWidth()));
            final float sliceSpace = getSliceSpace(dataSet);
            MPPointF iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset());
            iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x);
            iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y);
            for (int j = 0; j < entryCount; j++) {
                PieEntry entry = dataSet.getEntryForIndex(j);
                if (xIndex == 0)
                    angle = 0.f;
                else
                    angle = absoluteAngles[xIndex - 1] * phaseX;
                final float sliceAngle = drawAngles[xIndex];
                final float sliceSpaceMiddleAngle = sliceSpace / (Utils.FDEG2RAD * labelRadius);
                final float angleOffset = (sliceAngle - sliceSpaceMiddleAngle 2.f;
                angle = angle + angleOffset;
                final float transformedAngle = rotationAngle + angle * phaseY;
                float value = mChart.isUsePercentValuesEnabled() ? entry.getY()
                        / yValueSum * 100f : entry.getY();
                String entryLabel = entry.getLabel();
                final float sliceXBase = (float) Math.cos(transformedAngle * Utils.FDEG2RAD);
                final float sliceYBase = (float) Math.sin(transformedAngle * Utils.FDEG2RAD);
                final boolean drawXOutside = drawEntryLabels &&
                        xValuePosition == PieDataSet.ValuePosition.OUTSIDE_SLICE;
                final boolean drawYOutside = drawValues &&
                        yValuePosition == PieDataSet.ValuePosition.OUTSIDE_SLICE;
                final boolean drawXInside = drawEntryLabels &&
                        xValuePosition == PieDataSet.ValuePosition.INSIDE_SLICE;
                final boolean drawYInside = drawValues &&
                        yValuePosition == PieDataSet.ValuePosition.INSIDE_SLICE;
                if (drawXOutside || drawYOutside) {
                    final float valueLineLength1 = dataSet.getValueLinePart1Length();
                    final float valueLineLength2 = dataSet.getValueLinePart2Length();
                    final float valueLinePart1OffsetPercentage = dataSet.getValueLinePart1OffsetPercentage() / 100.f;
                    float pt2x, pt2y;
                    float labelPtx, labelPty;
                    float line1Radius;
                    if (mChart.isDrawHoleEnabled())
                        line1Radius = (radius - (radius * holeRadiusPercent))
                                * valueLinePart1OffsetPercentage
                                + (radius * holeRadiusPercent);
                    else
                        line1Radius = radius * valueLinePart1OffsetPercentage;
                    final float polyline2Width = dataSet.isValueLineVariableLength()
                            ? labelRadius * valueLineLength2 * (float) Math.abs(Math.sin(
                            transformedAngle * Utils.FDEG2RAD))
                            : labelRadius * valueLineLength2;
                    final float pt0x = line1Radius * sliceXBase + center.x;
                    final float pt0y = line1Radius * sliceYBase + center.y;
                    final float pt1x = labelRadius * (1 + valueLineLength1) * sliceXBase + center.x;
                    final float pt1y = labelRadius * (1 + valueLineLength1) * sliceYBase + center.y;
                    if (transformedAngle % 360.0 >= 90.0 && transformedAngle % 360.0 <= 270.0) {
                        pt2x = pt1x - polyline2Width;
                        pt2y = pt1y;
                        mValuePaint.setTextAlign(Align.RIGHT);
                        if (drawXOutside)
                            mEntryLabelsPaint.setTextAlign(Align.RIGHT);
                        labelPtx = pt2x - offset;
                        labelPty = pt2y;
                    } else {
                        pt2x = pt1x + polyline2Width;
                        pt2y = pt1y;
                        mValuePaint.setTextAlign(Align.LEFT);
                        if (drawXOutside)
                            mEntryLabelsPaint.setTextAlign(Align.LEFT);
                        labelPtx = pt2x + offset;
                        labelPty = pt2y;
                    }
                    int lineColor = ColorTemplate.COLOR_NONE;
                    if (isUseValueColorForLineEnabled)
                        lineColor = dataSet.getColor(j);
                    else if (valueLineColor != ColorTemplate.COLOR_NONE)
                        lineColor = valueLineColor;
                    if (lineColor != ColorTemplate.COLOR_NONE) {
                        mValueLinePaint.setColor(lineColor);
                        c.drawLine(pt0x, pt0y, pt1x, pt1y, mValueLinePaint);
                        c.drawLine(pt1x, pt1y, pt2x, pt2y, mValueLinePaint);
                    }
                    if (drawXOutside && drawYOutside) {
                        drawValue(c,
                                formatter,
                                value,
                                entry,
                                0,
                                labelPtx,
                                labelPty,
                                dataSet.getValueTextColor(j));
                        if (j < data.getEntryCount() && entryLabel != null) {
                            drawEntryLabel(c, entryLabel, labelPtx, labelPty + lineHeight);
                        }
                    } else if (drawXOutside) {
                        if (j < data.getEntryCount() && entryLabel != null) {
                            drawEntryLabel(c, entryLabel, labelPtx, labelPty + lineHeight
                            2.f, dataSet
                                    .getValueTextColor(j));
                        }
                    }
                    if (drawXInside || drawYInside) {
                        float x = labelRadius * sliceXBase + center.x;
                        float y = labelRadius * sliceYBase + center.y;
                        mValuePaint.setTextAlign(Align.CENTER);
                        if (drawXInside && drawYInside) {
                            drawValue(c, formatter, value, entry, 0, x, y, dataSet.getValueTextColor(j));
                            if (j < data.getEntryCount() && entryLabel != null) {
                                drawEntryLabel(c, entryLabel, x, y + lineHeight);
                            }
                        } else if (drawXInside) {
                            if (j < data.getEntryCount() && entryLabel != null) {
                                drawEntryLabel(c, entryLabel, x, y + lineHeight
                                2f, dataSet.getValueTextColor(j));
                            }
                        }
                        if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {
                            Drawable icon = entry.getIcon();
                            float x = (labelRadius + iconsOffset.y) * sliceXBase + center.x;
                            float y = (labelRadius + iconsOffset.y) * sliceYBase + center.y;
                            y += iconsOffset.x;
                            Utils.drawImage(
                                    c,
                                    icon,
                                    (int) x,
                                    (int) y,
                                    icon.getIntrinsicWidth(),
                                    icon.getIntrinsicHeight());
                        }
                        xIndex++;
                    }
                    MPPointF.recycleInstance(iconsOffset);
                }
                MPPointF.recycleInstance(center);
                c.restore();
            }
            protected void drawEntryLabel (Canvas c, String label,float x, float y){
                c.drawText(label, x, y, mEntryLabelsPaint);
            }
            @Override
            public void drawExtras (Canvas c){
                drawHole(c);
                c.drawBitmap(mDrawBitmap.get(), 0, 0, null);
                drawCenterText(c);
            }
            protected void drawHole (Canvas c){
                if (mChart.isDrawHoleEnabled() && mBitmapCanvas != null) {
                    float radius = mChart.getRadius();
                    float holeRadius = radius * (mChart.getHoleRadius() / 100);
                    MPPointF center = mChart.getCenterCircleBox();
                    if (Color.alpha(mHolePaint.getColor()) > 0) {
                        mBitmapCanvas.drawCircle(
                                center.x, center.y,
                                holeRadius, mHolePaint);
                    }
                    if (Color.alpha(mTransparentCirclePaint.getColor()) > 0 &&
                            mChart.getTransparentCircleRadius() > mChart.getHoleRadius()) {
                        int alpha = mTransparentCirclePaint.getAlpha();
                        float secondHoleRadius = radius * (mChart.getTransparentCircleRadius() / 100);
                        mTransparentCirclePaint.setAlpha((int) ((float) alpha * mAnimator.getPhaseX() * mAnimator.getPhaseY()));
                        mHoleCirclePath.reset();
                        mHoleCirclePath.addCircle(center.x, center.y, secondHoleRadius, Path.Direction.CW);
                        mHoleCirclePath.addCircle(center.x, center.y, holeRadius, Path.Direction.CCW);
                        mBitmapCanvas.drawPath(mHoleCirclePath, mTransparentCirclePaint);
                        mTransparentCirclePaint.setAlpha(alpha);
                    }
                    MPPointF.recycleInstance(center);
                }
            }
            protected void drawCenterText (Canvas c){
                CharSequence centerText = mChart.getCenterText();
                if (mChart.isDrawCenterTextEnabled() && centerText != null) {
                    MPPointF center = mChart.getCenterCircleBox();
                    MPPointF offset = mChart.getCenterTextOffset();
                    float x = center.x + offset.x;
                    float y = center.y + offset.y;
                    float innerRadius = mChart.isDrawHoleEnabled() && !mChart.isDrawSlicesUnderHoleEnabled()
                            ? mChart.getRadius() * (mChart.getHoleRadius() 100f;
                    if (radiusPercent > 0.0) {
                        boundingRect.inset(
                                (boundingRect.width() - boundingRect.width() * radiusPercent) / 2.f,
                                (boundingRect.height() - boundingRect.height() * radiusPercent) 2.f)
                        ;
                        mCenterTextLayout.draw(c);
                        c.restore();
                        MPPointF.recycleInstance(center);
                        MPPointF.recycleInstance(offset);
                    }
                }
                @Override
                public void drawHighlighted (Canvas c, Highlight[]indices){
                    final boolean drawInnerArc = mChart.isDrawHoleEnabled() && !mChart.isDrawSlicesUnderHoleEnabled();
                    if (drawInnerArc && mChart.isDrawRoundedSlicesEnabled())
                        return;
                    float phaseX = mAnimator.getPhaseX();
                    float phaseY = mAnimator.getPhaseY();
                    float angle;
                    float rotationAngle = mChart.getRotationAngle();
                    float[] drawAngles = mChart.getDrawAngles();
                    float[] absoluteAngles = mChart.getAbsoluteAngles();
                    final MPPointF center = mChart.getCenterCircleBox();
                    final float radius = mChart.getRadius();
                    final float userInnerRadius = drawInnerArc
                            ? radius * (mChart.getHoleRadius() / 100.f)
                            : 0.f;
                    final RectF highlightedCircleBox = mDrawHighlightedRectF;
                    highlightedCircleBox.set(0, 0, 0, 0);
                    for (int i = 0; i < indices.length; i++) {
                        int index = (int) indices[i].getX();
                        if (index >= drawAngles.length)
                            continue;
                        IPieDataSet set = mChart.getData()
                                .getDataSetByIndex(indices[i].getDataSetIndex());
                        if (set == null || !set.isHighlightEnabled())
                            continue;
                        final int entryCount = set.getEntryCount();
                        int visibleAngleCount = 0;
                        for (int j = 0; j < entryCount; j++) {
                            if ((Math.abs(set.getEntryForIndex(j).getY()) > Utils.FLOAT_EPSILON)) {
                                visibleAngleCount++;
                            }
                        }
                        if (index == 0)
                            angle = 0.f;
                        else
                            angle = absoluteAngles[index - 1] * phaseX;
                        final float sliceSpace = visibleAngleCount <= 1 ? 0.f : set.getSliceSpace();
                        float sliceAngle = drawAngles[index];
                        float innerRadius = userInnerRadius;
                        float shift = set.getSelectionShift();
                        final float highlightedRadius = radius + shift;
                        highlightedCircleBox.set(mChart.getCircleBox());
                        highlightedCircleBox.inset(-shift, -shift);
                        final boolean accountForSliceSpacing = sliceSpace > 0.f && sliceAngle <= 180.f;
                        Integer highlightColor = set.getHighlightColor();
                        if (highlightColor == null)
                            highlightColor = set.getColor(index);
                        mRenderPaint.setColor(highlightColor);
                        final float sliceSpaceAngleOuter = visibleAngleCount == 1 ?
                                0.f :
                                sliceSpace / (Utils.FDEG2RAD * radius);
                        final float sliceSpaceAngleShifted = visibleAngleCount == 1 ?
                                0.f :
                                sliceSpace / (Utils.FDEG2RAD * highlightedRadius);
                        final float startAngleOuter = rotationAngle + (angle + sliceSpaceAngleOuter / 2.f) * phaseY;
                        float sweepAngleOuter = (sliceAngle - sliceSpaceAngleOuter) * phaseY;
                        if (sweepAngleOuter < 0.f) {
                            sweepAngleOuter = 0.f;
                        }
                        final float startAngleShifted = rotationAngle + (angle + sliceSpaceAngleShifted / 2.f) * phaseY;
                        float sweepAngleShifted = (sliceAngle - sliceSpaceAngleShifted) * phaseY;
                        if (sweepAngleShifted < 0.f) {
                            sweepAngleShifted = 0.f;
                        }
                        mPathBuffer.reset();
                        if (sweepAngleOuter >= 360.f && sweepAngleOuter % 360f <= Utils.FLOAT_EPSILON) {
                            mPathBuffer.addCircle(center.x, center.y, highlightedRadius, Path.Direction.CW);
                        } else {
                            mPathBuffer.moveTo(
                                    center.x + highlightedRadius * (float) Math.cos(startAngleShifted * Utils.FDEG2RAD),
                                    center.y + highlightedRadius * (float) Math.sin(startAngleShifted * Utils.FDEG2RAD));
                            mPathBuffer.arcTo(
                                    highlightedCircleBox,
                                    startAngleShifted,
                                    sweepAngleShifted
                            );
                        }
                        float sliceSpaceRadius = 0.f;
                        if (accountForSliceSpacing) {
                            sliceSpaceRadius =
                                    calculateMinimumRadiusForSpacedSlice(
                                            center, radius,
                                            sliceAngle * phaseY,
                                            center.x + radius * (float) Math.cos(startAngleOuter * Utils.FDEG2RAD),
                                            center.y + radius * (float) Math.sin(startAngleOuter * Utils.FDEG2RAD),
                                            startAngleOuter,
                                            sweepAngleOuter);
                        }
                        mInnerRectBuffer.set(
                                center.x - innerRadius,
                                center.y - innerRadius,
                                center.x + innerRadius,
                                center.y + innerRadius);
                        if (drawInnerArc &&
                                (innerRadius > 0.f || accountForSliceSpacing)) {
                            if (accountForSliceSpacing) {
                                float minSpacedRadius = sliceSpaceRadius;
                                if (minSpacedRadius < 0.f)
                                    minSpacedRadius = -minSpacedRadius;
                                innerRadius = Math.max(innerRadius, minSpacedRadius);
                            }
                            final float sliceSpaceAngleInner = visibleAngleCount == 1 || innerRadius == 0.f ?
                                    0.f :
                                    sliceSpace / (Utils.FDEG2RAD * innerRadius);
                            final float startAngleInner = rotationAngle + (angle + sliceSpaceAngleInner / 2.f) * phaseY;
                            float sweepAngleInner = (sliceAngle - sliceSpaceAngleInner) * phaseY;
                            if (sweepAngleInner < 0.f) {
                                sweepAngleInner = 0.f;
                            }
                            final float endAngleInner = startAngleInner + sweepAngleInner;
                            if (sweepAngleOuter >= 360.f && sweepAngleOuter % 360f <= Utils.FLOAT_EPSILON) {
                                mPathBuffer.addCircle(center.x, center.y, innerRadius, Path.Direction.CCW);
                            } else {
                                mPathBuffer.lineTo(
                                        center.x + innerRadius * (float) Math.cos(endAngleInner * Utils.FDEG2RAD),
                                        center.y + innerRadius * (float) Math.sin(endAngleInner * Utils.FDEG2RAD));
                                mPathBuffer.arcTo(
                                        mInnerRectBuffer,
                                        endAngleInner,
                                        -sweepAngleInner
                                );
                            }
                        } else {
                            if (sweepAngleOuter % 360f > Utils.FLOAT_EPSILON) {
                                if (accountForSliceSpacing) {
                                    final float angleMiddle = startAngleOuter + sweepAngleOuter / 2.f;
                                    final float arcEndPointX = center.x +
                                            sliceSpaceRadius * (float) Math.cos(angleMiddle * Utils.FDEG2RAD);
                                    final float arcEndPointY = center.y +
                                            sliceSpaceRadius * (float) Math.sin(angleMiddle * Utils.FDEG2RAD);
                                    mPathBuffer.lineTo(
                                            arcEndPointX,
                                            arcEndPointY);
                                } else {
                                    mPathBuffer.lineTo(
                                            center.x,
                                            center.y);
                                }
                            }
                        }
                        mPathBuffer.close();
                        mBitmapCanvas.drawPath(mPathBuffer, mRenderPaint);
                    }
                    MPPointF.recycleInstance(center);
                }
                protected void drawRoundedSlices (Canvas c){
                    if (!mChart.isDrawRoundedSlicesEnabled())
                        return;
                    IPieDataSet dataSet = mChart.getData().getDataSet();
                    if (!dataSet.isVisible())
                        return;
                    float phaseX = mAnimator.getPhaseX();
                    float phaseY = mAnimator.getPhaseY();
                    MPPointF center = mChart.getCenterCircleBox();
                    float r = mChart.getRadius();
                    float circleRadius = (r - (r * mChart.getHoleRadius() 2f;
                    float[] drawAngles = mChart.getDrawAngles();
                    float angle = mChart.getRotationAngle();
                    for (int j = 0; j < dataSet.getEntryCount(); j++) {
                        float sliceAngle = drawAngles[j];
                        Entry e = dataSet.getEntryForIndex(j);
                        if ((Math.abs(e.getY()) > Utils.FLOAT_EPSILON)) {
                            float x = (float) ((r - circleRadius)
                                    * Math.cos(Math.toRadians((angle + sliceAngle)
                                    * phaseY)) + center.x);
                            float y = (float) ((r - circleRadius)
                                    * Math.sin(Math.toRadians((angle + sliceAngle)
                                    * phaseY)) + center.y);
                            mRenderPaint.setColor(dataSet.getColor(j));
                            mBitmapCanvas.drawCircle(x, y, circleRadius, mRenderPaint);
                        }
                        angle += sliceAngle * phaseX;
                    }
                    MPPointF.recycleInstance(center);
                }
                public void releaseBitmap () {
                    if (mBitmapCanvas != null) {
                        mBitmapCanvas.setBitmap(null);
                        mBitmapCanvas = null;
                    }
                    if (mDrawBitmap != null) {
                        Bitmap drawBitmap = mDrawBitmap.get();
                        if (drawBitmap != null) {
                            drawBitmap.recycle();
                        }
                        mDrawBitmap.clear();
                        mDrawBitmap = null;
                    }
                }
            }
