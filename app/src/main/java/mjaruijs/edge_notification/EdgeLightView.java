package mjaruijs.edge_notification;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;

import mjaruijs.edge_notification.values.Variables;

import static android.content.Context.WINDOW_SERVICE;

public class EdgeLightView extends View implements ValueAnimator.AnimatorUpdateListener {

    private ValueAnimator valueAnimator;
    private Bitmap bitmapMainColor;
    private Bitmap bitmapBackgroundColor;
    private Paint paint = new Paint();
    private Paint backgroundPaint;
    private int width;
    private int height;
    private float animatorFloat;
    public Variables vars;
//    private static final int SENSOR_SENSITIVITY = 4;
    private boolean screenOff;

    public EdgeLightView(Context context) {
        super(context);
    }

    public EdgeLightView(Context context, Variables vars, boolean screenOff) {
        super(context);
        this.vars = vars;
        this.screenOff = screenOff;
        valueAnimator = ValueAnimator.ofFloat(0.0f, 360.0f);
        valueAnimator.setDuration(3000);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(this);

        paint.setAntiAlias(true);
        paint.setDither(false);
        backgroundPaint = new Paint();
        backgroundPaint.setDither(true);
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setColor(Color.BLACK);
        animatorFloat = -999.0f;
    }

    private void setShader(float floatValue) {
        Matrix m = new Matrix();
        int y0;
        int y;
        m.postRotate(floatValue, 770f, 1480f);
        if(screenOff) {
            y0 = 760;
            y = 2200;
        } else {
            y0 = 760;
            y = 2200;
        }
        paint.setShader(new LinearGradient(770, y0, 770, y, vars.integers, vars.floats, Shader.TileMode.CLAMP));
        paint.getShader().setLocalMatrix(m);
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawRect(0.0f, 0.0f, (float) width, (float) height, backgroundPaint);
    }

    private void drawEdgeCorners(Canvas canvas, Paint paint, float f, float f2) {
        float width = (float)this.width;
        float height = (float)this.height;

        Path path = new Path();
        path.moveTo(f, f);
        path.lineTo(f + f2, f);
        path.cubicTo(f + f2, f, f, f, f, f + f2);
        path.lineTo(f, f);
        canvas.drawPath(path, paint);

        path = new Path();
        path.moveTo(width - f, f);
        path.lineTo(width - f, f + f2);
        path.cubicTo(width - f, f + f2, width - f, f, (width - f) - f2, f);
        path.lineTo((width - f) - f2, f);
        canvas.drawPath(path, paint);

        path = new Path();
        path.moveTo(f, height - f);
        path.lineTo(f, (height - f2) - f);
        path.cubicTo(f, (height - f2) - f, f, height - f, f + f2, height - f);
        path.lineTo(f + f2, height - f);
        canvas.drawPath(path, paint);

        path = new Path();
        path.moveTo(width - f, height - f);
        path.lineTo(width - f, (height - f2) - f);
        path.cubicTo(width - f, (height - f2) - f, width - f, height - f, (width - f) - f2, height - f);
        path.lineTo((width - f) - f2, height - f);
        canvas.drawPath(path, paint);
        Log.i(getClass().getSimpleName(), "CANVAS DRAWN");
    }

    private void callShader(EdgeLightView lightingEdgeView, float f, int i, Object obj) {
        if (obj == null) {
            if ((i & 4) != 0) {
                f = -2.5f;
            }
            lightingEdgeView.setShader(f);
        }
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void drawEdgeLines() {
        try {
            Display defaultDisplay = ((WindowManager) getContext().getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
            DisplayMetrics displayMetrics;
            if (hasNavBar(getContext())) {
                displayMetrics = new DisplayMetrics();
                defaultDisplay.getRealMetrics(displayMetrics);
                height = displayMetrics.heightPixels;
                width = displayMetrics.widthPixels;
            } else {
                displayMetrics = new DisplayMetrics();
                defaultDisplay.getMetrics(displayMetrics);
                height = displayMetrics.heightPixels;
                width = displayMetrics.widthPixels;
            }

            bitmapMainColor = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8);
            bitmapBackgroundColor = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8);

            Canvas mainColorCanvas = new Canvas(bitmapMainColor);
            Canvas backgroundCanvas = new Canvas(bitmapBackgroundColor);

            mainColorCanvas.drawRect(0.0f, 0.0f, (float) width, vars.strokeWidth, paint);
            mainColorCanvas.drawRect(0.0f, ((float) height) - vars.strokeWidth, (float) width, (float) height, paint);
            mainColorCanvas.drawRect(0.0f, vars.strokeWidth, vars.strokeWidth, ((float) height) - vars.strokeWidth, paint);
            mainColorCanvas.drawRect(((float) width) - vars.strokeWidth, vars.strokeWidth, (float) width, ((float) height) - vars.strokeWidth, paint);

            if (screenOff) {
                drawBackground(backgroundCanvas);
            }

            drawEdgeCorners(mainColorCanvas, paint, vars.strokeWidth, vars.cornerRadius);
        } catch (Exception e) {
            close();
            valueAnimator = null;
            bitmapMainColor = null;
            bitmapBackgroundColor = null;
        }
    }

    private boolean hasNavBar(Context context) {
        Display defaultDisplay = ((WindowManager) context.getSystemService(WINDOW_SERVICE)).getDefaultDisplay();

        DisplayMetrics realMetrics = new DisplayMetrics();
        defaultDisplay.getRealMetrics(realMetrics);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics);

        if (realMetrics.widthPixels > displayMetrics.widthPixels) {
            return true;
        }

        if (realMetrics.heightPixels > displayMetrics.heightPixels) {
            return true;
        }

        return false;
    }

    @Override
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        try {
            super.onSizeChanged(i, i2, i3, i4);
            callShader(this, 0.0f, 4, null);
            if (i2 <= 0 || i <= 0) {
                bitmapMainColor = null;
            } else {
                drawEdgeLines();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onDraw(Canvas canvas) {
        try {
            if (bitmapBackgroundColor != null) {
                canvas.drawBitmap(bitmapBackgroundColor, 0.0f, 0.0f, backgroundPaint);
            }
            if (bitmapMainColor != null) {
                canvas.drawBitmap(bitmapMainColor, 0.0f, 0.0f, paint);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        switch (i) {
            case VISIBLE:
                if (valueAnimator != null) {
                    valueAnimator.start();
                    return;
                }
                return;
            case INVISIBLE:
            case GONE:
                if (valueAnimator != null) {
                    valueAnimator.cancel();
                    return;
                }
                return;
            default:
                break;
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        valueAnimator.cancel();
    }

    public final void close() {
        setVisibility(View.GONE);
        valueAnimator.cancel();
    }
    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        try {
            if (bitmapMainColor != null && getAlpha() > 0.0f) {
                Object animatedValue = valueAnimator.getAnimatedValue();
                if (animatedValue != null) {
                    float floatValue = ((float) ((int) ((Float) animatedValue * ((float) 1000)))) / 1000.0f;
                    if (animatorFloat != floatValue) {
                        animatorFloat = floatValue;
                        setShader(floatValue);
                        invalidate(0, 0, width, height);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setAlpha(float f) {
        super.setAlpha(f);
        if (f == 0.0f && valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
//            if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
//                proximityClose = true;
//            } else {
//                proximityClose = false;
//            }
//        }
//    }

//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//    }
}
