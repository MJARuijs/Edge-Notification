package mjaruijs.edge_notification.values;

import android.content.Context;

import mjaruijs.edge_notification.EdgeLightView;

public class Variables {
    public final Context context;
    public int[] integers;
    public float[] floats;
    public float strokeWidth;
    public float cornerRadius;

    public Variables(Context context) {
        this.context = context;
        this.integers = new int[]{};
        this.floats = new float[]{};
        init();
    }

    public EdgeLightView initView(boolean screenOff) { return new EdgeLightView(this.context, this, screenOff); }

    private void init() {
        this.strokeWidth = 3.0f;
        this.cornerRadius = 10.0f;
    }

    public void setGradientColors(int... iArr) {
        if (iArr.length == 1) {
            int[] iArr2 = new int[3];
            iArr2[0] = iArr[0];
            iArr2[2] = iArr[0];
            this.integers = iArr2;
            this.floats = new float[]{0.0f, 0.5f, 1.0f};
        } else if (iArr.length == 2) {
            this.integers = new int[]{iArr[0], iArr[1], iArr[0]};
            this.floats = new float[]{0.0f, 0.5f, 1.0f};
        } else if (iArr.length == 3) {
            this.integers = new int[]{iArr[0], iArr[1], iArr[2]};
            this.floats = new float[]{0.0f, 0.5f, 1.0f};
        } else {
            this.integers = new int[]{iArr[0], iArr[1], iArr[2], iArr[3]};
            this.floats = new float[]{0.0f, 0.30f, 0.70f, 1.0f};
        }
    }

    public void setStrokeWidth(float f) {
        this.strokeWidth = f;
    }

    public void setCornerRadius(float f) {
        cornerRadius = f;
    }

}
