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
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;

import mjaruijs.edge_notification.values.Variables;

import static android.content.Context.WINDOW_SERVICE;

public class EdgeLightView extends View implements ValueAnimator.AnimatorUpdateListener, SensorEventListener {

    private ValueAnimator valueAnimator;
    private Bitmap bitmap1;
    private Bitmap bitmap2;
    private Paint paint = new Paint();
    private Paint paint1;
    private int width;
    private int height;
    private float animatorFloat;
    public Variables vars;
    private static final int SENSOR_SENSITIVITY = 4;
    private boolean proximityClose;
    private boolean screenOff;

    public EdgeLightView(Context context) {
        super(context);
    }

    public EdgeLightView(Context context, Variables vars, boolean screenOff) {
        super(context);
        this.vars = vars;
        this.screenOff = screenOff;
        this.valueAnimator = ValueAnimator.ofFloat(0.0f, 360.0f);
        this.valueAnimator.setDuration(3000);
        this.valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        this.valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        this.valueAnimator.setInterpolator(new LinearInterpolator());
        this.valueAnimator.addUpdateListener(this);

        this.paint.setAntiAlias(true);
        this.paint.setDither(false);
        this.paint1 = new Paint();
        this.paint1.setDither(false);
        this.paint1.setAntiAlias(true);
        this.paint1.setColor(Color.BLACK);
        this.animatorFloat = -999.0f;
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
            this.paint.setShader(new LinearGradient(770, y0, 770, y, this.vars.integers, this.vars.floats, Shader.TileMode.CLAMP));
            paint.getShader().setLocalMatrix(m);
    }

    private void drawBackground(Canvas canvas, Paint paint, int width, int height) {
        Path path = new Path();

        path.moveTo(0, 0);
        path.lineTo(width, 0);
        path.lineTo(width, height);
        path.lineTo(0, height);
        path.lineTo(0, 0);
        canvas.drawPath(path, paint);

    }

    private void drawEdgeCorners(Canvas canvas, Paint paint, int i, int i2, float f, float f2) {
        float f3 = this.vars.f9605e ? f2 : 0.0f;
        Path path = new Path();

        path.moveTo(f, f);
        path.lineTo(f + f3, f);
        path.cubicTo(f + f3, f, f, f, f, f + f3);
        path.lineTo(f, f);
        canvas.drawPath(path, paint);
        float f4 = this.vars.f9606f ? f2 : 0.0f;
        path = new Path();
        path.moveTo(((float) i) - f, f);
        path.lineTo(((float) i) - f, f + f4);
        path.cubicTo(((float) i) - f, f + f4, ((float) i) - f, f, (((float) i) - f) - f4, f);
        path.lineTo((((float) i) - f) - f4, f);
        canvas.drawPath(path, paint);
        f4 = this.vars.f9604d ? f2 : 0.0f;
        path = new Path();
        path.moveTo(f, ((float) i2) - f);
        path.lineTo(f, (((float) i2) - f4) - f);
        path.cubicTo(f, (((float) i2) - f4) - f, f, ((float) i2) - f, f + f4, ((float) i2) - f);
        path.lineTo(f + f4, ((float) i2) - f);
        canvas.drawPath(path, paint);
        if (!this.vars.f9607g) {
            f2 = 0.0f;
        }
        path = new Path();
        path.moveTo(((float) i) - f, ((float) i2) - f);
        path.lineTo(((float) i) - f, (((float) i2) - f2) - f);
        path.cubicTo(((float) i) - f, (((float) i2) - f2) - f, ((float) i) - f, ((float) i2) - f, (((float) i) - f) - f2, ((float) i2) - f);
        path.lineTo((((float) i) - f) - f2, ((float) i2) - f);
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

    private void drawEdgeLines() {
        try {
            Display defaultDisplay = ((WindowManager) getContext().getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
            DisplayMetrics displayMetrics;
            if (hasNavBar(getContext())) {
                displayMetrics = new DisplayMetrics();
                defaultDisplay.getRealMetrics(displayMetrics);
                int i = displayMetrics.heightPixels;
                int i2 = displayMetrics.widthPixels;
                this.height = i;
                this.width = i2;
            } else {
                displayMetrics = new DisplayMetrics();
                defaultDisplay.getMetrics(displayMetrics);
                this.height = displayMetrics.heightPixels;
                this.width = displayMetrics.widthPixels;
            }
            this.bitmap1 = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ALPHA_8);
            if (this.vars.f9616p) {
                this.bitmap2 = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ALPHA_8);
            } else {
                this.bitmap2 = null;
            }
            Canvas canvas = new Canvas(this.bitmap1);
            Paint paint = new Paint();
            paint.setAntiAlias(false);
            canvas.drawRect(0.0f, 0.0f, (float) this.width, this.vars.f9608h, paint);
            canvas.drawRect(0.0f, ((float) this.height) - this.vars.f9608h, (float) this.width, (float) this.height, paint);
            canvas.drawRect(0.0f, this.vars.f9608h, this.vars.f9608h, ((float) this.height) - this.vars.f9608h, paint);
            canvas.drawRect(((float) this.width) - this.vars.f9608h, this.vars.f9608h, (float) this.width, ((float) this.height) - this.vars.f9608h, paint);
            if (this.vars.f9616p) {
                if (screenOff && proximityClose) {
                    drawBackground(canvas, paint, this.width, this.height);
                }
                drawEdgeCorners(canvas, paint, this.width, this.height, this.vars.f9608h, this.vars.f9609i);
                drawEdgeCorners(new Canvas(this.bitmap2), this.paint1, this.width, this.height, 0.0f, this.vars.f9609i);

            }
        } catch (Exception e) {
            close();
            this.valueAnimator = null;
            this.bitmap1 = null;
            this.bitmap2 = null;
        }
    }

    private boolean hasNavBar(Context context) {
        Display defaultDisplay = ((WindowManager) context.getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        defaultDisplay.getRealMetrics(displayMetrics);
        int i = displayMetrics.heightPixels;
        int i2 = displayMetrics.widthPixels;
        DisplayMetrics displayMetrics2 = new DisplayMetrics();
        defaultDisplay.getMetrics(displayMetrics2);
        return i2 - displayMetrics2.widthPixels > 0 || i - displayMetrics2.heightPixels > 0;
    }

    @Override
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        Log.i(getClass().getSimpleName(), "SIZE CHANGED" + i + " " + i2 + " " + i3 + " " + i4);
        try {
            super.onSizeChanged(i, i2, i3, i4);
            callShader(this, 0.0f, 4, null);
            if (i2 <= 0 || i <= 0) {
                this.bitmap1 = null;
            } else {
                drawEdgeLines();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onDraw(Canvas canvas) {
        try {
            if (this.bitmap1 != null) {
                canvas.drawBitmap(this.bitmap1, 0.0f, 0.0f, this.paint);
            }
            if (this.bitmap2 != null) {
                canvas.drawBitmap(this.bitmap2, 0.0f, 0.0f, this.paint1);
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
                if (this.valueAnimator != null) {
                    this.valueAnimator.start();
                    return;
                }
                return;
            case INVISIBLE:
            case GONE:
                if (this.valueAnimator != null) {
                    this.valueAnimator.cancel();
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
        this.valueAnimator.cancel();
    }

    public final void close() {
        Log.i(getClass().getSimpleName(), "CLOSE");
        setVisibility(View.GONE);
        this.valueAnimator.cancel();
    }
    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        try {
            if (this.bitmap1 != null && getAlpha() > 0.0f) {
                Object animatedValue = this.valueAnimator.getAnimatedValue();
                if (animatedValue != null) {
                    float floatValue = ((float) ((int) ((Float) animatedValue * ((float) 1000)))) / 1000.0f;
                    if (this.animatorFloat != floatValue) {
                        this.animatorFloat = floatValue;
                        setShader(floatValue);
                        invalidate(0, 0, this.width, this.height);
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
        if (f == 0.0f && this.valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
                proximityClose = true;
                Log.i(getClass().getSimpleName(), "Close");
            } else {
                proximityClose = false;
                Log.i(getClass().getSimpleName(), "Far");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
