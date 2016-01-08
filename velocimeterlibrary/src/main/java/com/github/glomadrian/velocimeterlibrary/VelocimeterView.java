package com.github.glomadrian.velocimeterlibrary;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.github.glomadrian.velocimeterlibrary.painter.digital.Digital;
import com.github.glomadrian.velocimeterlibrary.painter.digital.DigitalBlurImp;
import com.github.glomadrian.velocimeterlibrary.painter.digital.DigitalImp;
import com.github.glomadrian.velocimeterlibrary.painter.inside.InsideVelocimeterMarkerPainter;
import com.github.glomadrian.velocimeterlibrary.painter.inside.InsideVelocimeterMarkerPainterImp;
import com.github.glomadrian.velocimeterlibrary.painter.inside.InsideVelocimeterPainterImp;
import com.github.glomadrian.velocimeterlibrary.painter.needle.LineBlurPainter;
import com.github.glomadrian.velocimeterlibrary.painter.needle.NeedlePainter;
import com.github.glomadrian.velocimeterlibrary.painter.needle.NeedlePainterImp;
import com.github.glomadrian.velocimeterlibrary.painter.progress.BlurProgressVelocimeterPainter;
import com.github.glomadrian.velocimeterlibrary.painter.progress.ProgressVelocimeterPainter;
import com.github.glomadrian.velocimeterlibrary.painter.progress.ProgressVelocimeterPainterImp;
import com.github.glomadrian.velocimeterlibrary.painter.velocimeter.InternalVelocimeterPainter;
import com.github.glomadrian.velocimeterlibrary.painter.velocimeter.InternalVelocimeterPainterImp;
import com.github.glomadrian.velocimeterlibrary.utils.DimensionUtils;

/**
 * @author Adrián García Lomas
 */

public class VelocimeterView extends View {

  private ValueAnimator progressValueAnimator;
  private ValueAnimator nidleValueAnimator;
  private Interpolator interpolator = new AccelerateDecelerateInterpolator();
  private InternalVelocimeterPainter internalVelocimeterPainter;
  private ProgressVelocimeterPainter progressVelocimeterPainter;
  private ProgressVelocimeterPainter blurProgressVelocimeterPainter;
  private InsideVelocimeterPainterImp insideVelocimeterPainter;
  private InsideVelocimeterMarkerPainter insideVelocimeterMarkerPainter;
  private NeedlePainter linePainter;
  private NeedlePainter blurLinePainter;
  private Digital digitalPainter;
  private Digital digitalBlurPainter;
  private int min = 0;
  private float progressLastValue = min;
  private float nidleLastValue = min;
  private int max = 120;
  private float value;
  private int duration = 1000;
  private long progressDelay = 350;
  private int margin = 15;
  private int insideProgressColor = Color.parseColor("#094e35");
  private int externalProgressColor = Color.parseColor("#9cfa1d");
  private int progressBlurColor = Color.parseColor("#44ff2b");
  private int internalVelocimeterColor = Color.WHITE;
  private int needdleColor = Color.RED;
  private int needleBlurColor = Color.RED;
  private int digitalNumberColor = Color.GREEN;
  private int digitalNumberBlurColor = Color.GREEN;
  private String units = "kmh";

  public VelocimeterView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public VelocimeterView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int size;
    int width = getMeasuredWidth();
    int height = getMeasuredHeight();

    if (width > height) {
      size = height;
    } else {
      size = width;
    }
    setMeasuredDimension(size, size);
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    internalVelocimeterPainter.onSizeChanged(h, w);
    progressVelocimeterPainter.onSizeChanged(h, w);
    insideVelocimeterPainter.onSizeChanged(h, w);
    insideVelocimeterMarkerPainter.onSizeChanged(h, w);
    blurProgressVelocimeterPainter.onSizeChanged(h, w);
    digitalPainter.onSizeChanged(h, w);
    digitalBlurPainter.onSizeChanged(h, w);
    linePainter.onSizeChanged(h, w);
    blurLinePainter.onSizeChanged(h, w);
  }

  private void init(Context context, AttributeSet attributeSet) {
    TypedArray attributes =
        context.obtainStyledAttributes(attributeSet, R.styleable.VelocimeterView);
    initAttributes(attributes);

    int marginPixels = DimensionUtils.getSizeInPixels(margin, getContext());
    setLayerType(LAYER_TYPE_SOFTWARE, null);
    linePainter = new NeedlePainterImp(needdleColor, max, getContext());
    blurLinePainter = new LineBlurPainter(needleBlurColor, max, getContext());
    internalVelocimeterPainter =
        new InternalVelocimeterPainterImp(insideProgressColor, marginPixels, getContext());
    progressVelocimeterPainter =
        new ProgressVelocimeterPainterImp(externalProgressColor, max, marginPixels, getContext());
    insideVelocimeterPainter =
        new InsideVelocimeterPainterImp(internalVelocimeterColor, getContext());
    insideVelocimeterMarkerPainter =
        new InsideVelocimeterMarkerPainterImp(internalVelocimeterColor, getContext());
    blurProgressVelocimeterPainter =
        new BlurProgressVelocimeterPainter(progressBlurColor, max, marginPixels, getContext());
    initValueAnimator();
    // 设置显示数字的大小及位置
    digitalPainter = new DigitalImp(digitalNumberColor, getContext(),
        DimensionUtils.getSizeInPixels(60, context), DimensionUtils.getSizeInPixels(45, context),
        units);
    digitalBlurPainter = new DigitalBlurImp(digitalNumberBlurColor, getContext(),
        DimensionUtils.getSizeInPixels(60, context), DimensionUtils.getSizeInPixels(45, context),
        units);
  }

  private void initAttributes(TypedArray attributes) {
    insideProgressColor =
        attributes.getColor(R.styleable.VelocimeterView_inside_progress_color, insideProgressColor);
    externalProgressColor = attributes.getColor(R.styleable.VelocimeterView_external_progress_color,
        externalProgressColor);
//    progressBlurColor =
//        attributes.getColor(R.styleable.VelocimeterView_progress_blur_color, progressBlurColor);
//    bottomVelocimeterColor =
//        attributes.getColor(R.styleable.VelocimeterView_bottom_velocimeter_color,
//            bottomVelocimeterColor);
//    showBottomVelocimeter =
//        attributes.getBoolean(R.styleable.VelocimeterView_show_bottom_bar,
//                showBottomVelocimeter);
    internalVelocimeterColor =
        attributes.getColor(R.styleable.VelocimeterView_internal_velocimeter_color,
            internalVelocimeterColor);
    needdleColor = attributes.getColor(R.styleable.VelocimeterView_needle_color, needdleColor);
    needleBlurColor =
        attributes.getColor(R.styleable.VelocimeterView_needle_blur_color, needleBlurColor);
    digitalNumberColor =
        attributes.getColor(R.styleable.VelocimeterView_digital_number_color, digitalNumberColor);
    digitalNumberBlurColor =
        attributes.getColor(R.styleable.VelocimeterView_digital_number_blur_color,
            digitalNumberBlurColor);
    max = attributes.getInt(R.styleable.VelocimeterView_max, max);
    units = attributes.getString(R.styleable.VelocimeterView_units);
    if (units == null) {
      units = "kmh";
    }
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    //digitalBlurPainter.draw(canvas);
    digitalPainter.draw(canvas);                 // 数字显示
    //blurProgressVelocimeterPainter.draw(canvas);
    internalVelocimeterPainter.draw(canvas);     // 内部颜色设置
    progressVelocimeterPainter.draw(canvas);     // 高亮圆环进度显示
    insideVelocimeterPainter.draw(canvas);       // 白色圆环显示
    insideVelocimeterMarkerPainter.draw(canvas); // 白色圆环线条显示
    linePainter.draw(canvas);                    // 指针显示
    //blurLinePainter.draw(canvas);//去掉阴影
    invalidate();
  }

  public void setValue(float value) {
    this.value = value;
    if (value <= max && value >= min) {
      animateProgressValue();
    }
  }

  public void setValue(float value, boolean animate) {
    this.value = value;
    if (value <= max && value >= min) {
      if (!animate) {
        updateValueProgress(value);
        updateValueNeedle(value);
      } else {
        animateProgressValue();
      }
    }
  }

  private void initValueAnimator() {
    progressValueAnimator = new ValueAnimator();
    progressValueAnimator.setInterpolator(interpolator);
    progressValueAnimator.addUpdateListener(new ProgressAnimatorListenerImp());
    nidleValueAnimator = new ValueAnimator();
    nidleValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    nidleValueAnimator.addUpdateListener(new NeedleAnimatorListenerImp());
  }

  private void animateProgressValue() {
    if (progressValueAnimator != null) {
      progressValueAnimator.setFloatValues(progressLastValue, value);
      progressValueAnimator.setDuration(duration + progressDelay);
      progressValueAnimator.start();
      nidleValueAnimator.setFloatValues(nidleLastValue, value);
      nidleValueAnimator.setDuration(duration);
      nidleValueAnimator.start();
    }
  }

  public void setProgress(Interpolator interpolator) {
    this.interpolator = interpolator;
    if (progressValueAnimator != null) {
      progressValueAnimator.setInterpolator(interpolator);
    }
  }

  public float getMax() {
    return max;
  }

  public void setMax(int max) {
    this.max = max;
  }

  private void updateValueProgress(float value) {
    progressVelocimeterPainter.setValue(value);
    //blurProgressVelocimeterPainter.setValue(value);  //  设置转盘阴影效果
  }

  private void updateValueNeedle(float value) {
    linePainter.setValue(value);
    //blurLinePainter.setValue(value);
    digitalPainter.setValue(value);
    //digitalBlurPainter.setValue(value);  // 设置数字阴影效果
  }

  private class ProgressAnimatorListenerImp implements ValueAnimator.AnimatorUpdateListener {
    @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
      Float value = (Float) valueAnimator.getAnimatedValue();
      updateValueProgress(value);
      progressLastValue = value;
    }
  }

  private class NeedleAnimatorListenerImp implements ValueAnimator.AnimatorUpdateListener {
    @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
      Float value = (Float) valueAnimator.getAnimatedValue();
      updateValueNeedle(value);
      nidleLastValue = value;
    }
  }
}
