package com.github.glomadrian.velocimeterlibrary.painter.inside;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import com.github.glomadrian.velocimeterlibrary.utils.DimensionUtils;

/**
 * @author Adrián García Lomas
 */

public class InsideVelocimeterMarkerPainterImp implements InsideVelocimeterMarkerPainter {

  private Context context;
  private Paint paint;
  private RectF circle;
  private int width;
  private int height;
  private float startAngle = 160;
  private float finishAngle = 222;
  private int strokeWidth;
  private int externalStrokeWidth;
  private int blurMargin;
  private int margin;
  private int lineWidth;
  private int lineSpace;
  private int color;
  private Paint paint_number;
  private Path path;
  private String speed_number = " 0       10      20      30      40       50      60      70       80      90      100    110    120";

  public InsideVelocimeterMarkerPainterImp(int color, Context context) {
    this.context = context;
    this.color = color;
    initSize();
    initPainter();
  }

  private void initSize() {
    this.blurMargin = DimensionUtils.getSizeInPixels(15, context);
    this.externalStrokeWidth = DimensionUtils.getSizeInPixels(20, context);
    this.strokeWidth = DimensionUtils.getSizeInPixels(12, context);
    this.margin = DimensionUtils.getSizeInPixels(9, context);
    this.lineSpace = DimensionUtils.getSizeInPixels(30, context);
    this.lineWidth = DimensionUtils.getSizeInPixels(2, context);
  }

  private void initPainter() {
    // 设置小线条属性
    paint = new Paint();
    paint.setAntiAlias(true);  // 消除锯齿
    paint.setStrokeWidth(strokeWidth);  // 设置paint的外框宽度
    paint.setColor(color);
    paint.setStyle(Paint.Style.STROKE);
    paint.setPathEffect(new DashPathEffect(new float[]{lineWidth, lineSpace}, 0));
    // 设置数字属性
    paint_number = new Paint();
    paint_number.setAntiAlias(true);
    paint_number.setColor(color);
    paint_number.setStyle(Paint.Style.STROKE);
    paint_number.setTextSize(12);
    paint_number.setStrokeWidth(1);
  }

  private void initCircle() {
    int pading = externalStrokeWidth + (strokeWidth / 2) + margin + blurMargin;
    // 绘制圆弧
    circle = new RectF();
    circle.set(pading, pading, width - pading, height - pading);
    // 设置数字显示路径
    path = new Path();
    path.addArc(circle, startAngle - 3, finishAngle + 10);
  }

  @Override public void draw(Canvas canvas) {
    // 绘制线条
    canvas.drawArc(circle, startAngle, finishAngle, false, paint);
    // 以path为路径绘制数字
    canvas.drawTextOnPath(speed_number, path, 0, 18, paint_number);
  }

  @Override public void setColor(int color) {
    this.color = color;
  }

  @Override public int getColor() {
    return color;
  }

  @Override public void onSizeChanged(int height, int width) {
    this.width = width;
    this.height = height;
    initCircle();
  }
}
