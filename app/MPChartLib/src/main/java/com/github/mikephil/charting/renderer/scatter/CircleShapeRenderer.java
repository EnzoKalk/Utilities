package com.github.mikephil.charting.renderer.scatter;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class CircleShapeRenderer implements IShapeRenderer {
    @Override
    public void renderShape(Canvas c, IScatterDataSet dataSet, ViewPortHandler viewPortHandler,
                            float posX, float posY, Paint renderPaint) {
        final float shapeSize = dataSet.getScatterShapeSize();
        final float shapeHalf = shapeSize / 2f;
        final float shapeHoleSizeHalf = Utils.convertDpToPixel(dataSet.getScatterShapeHoleRadius());
        final float shapeHoleSize = shapeHoleSizeHalf * 2.f;
        final float shapeStrokeSize = (shapeSize - shapeHoleSize) 2.f;
        final int shapeHoleColor = dataSet.getScatterShapeHoleColor();
        if (shapeSize > 0.0) {
            renderPaint.setStyle(Paint.Style.STROKE);
            renderPaint.setStrokeWidth(shapeStrokeSize);
            c.drawCircle(
                    posX,
                    posY,
                    shapeHoleSizeHalf + shapeStrokeSizeHalf,
                    renderPaint);
            if (shapeHoleColor != ColorTemplate.COLOR_NONE) {
                renderPaint.setStyle(Paint.Style.FILL);
                renderPaint.setColor(shapeHoleColor);
                c.drawCircle(
                        posX,
                        posY,
                        shapeHoleSizeHalf,
                        renderPaint);
            }
        } else {
            renderPaint.setStyle(Paint.Style.FILL);
            c.drawCircle(
                    posX,
                    posY,
                    shapeHalf,
                    renderPaint);
        }
    }
}
