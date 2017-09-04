package mjaruijs.edge_notification.values;

import android.content.Context;
import android.graphics.Color;

import mjaruijs.edge_notification.EdgeLightView;

public class Variables {
    public final Context context;
    public int[] integers;
    public float[] floats;
    public boolean f9604d;
    public boolean f9605e;
    public boolean f9606f;
    public boolean f9607g;
    public float f9608h;
    public float f9609i;
    private int f9610j;
    public float f9611k;
    public float f9612l;
    public int f9613m;
    public boolean f9614n;
    public int f9615o;
    public boolean f9616p;
    public boolean displayOn;

    public Variables(Context context) {
        this.context = context;

        this.integers = new int[]{Color.GREEN, Color.BLACK, Color.GREEN};
        this.floats = new float[]{0.0f, 0.50f, 1.0f};
        this.f9611k = 0f;
        this.f9612l = 2f;
        this.f9613m = 0;
        this.f9614n = true;
        this.f9615o = -16777216;
        this.f9604d = true;
        this.f9607g = true;
        this.f9605e = true;
        this.f9606f = true;
        this.f9616p = true;
        this.displayOn = false;
        init();
    }

    public EdgeLightView initView(boolean screenOff) { return new EdgeLightView(this.context, this, screenOff); }

    public Variables m15252a(float f) {
        this.f9608h = f;
        return this;
    }

    private void init() {
        this.f9608h = 3.0f;
        this.f9609i = 10.0f;
    }


    public Variables m15255a(int... iArr) {
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
        return this;
    }

    public Variables m15256b(float f) {
        this.f9609i = f;
        return this;
    }

    public void setDisplayState(boolean state) {
        this.displayOn = state;
    }

    public Variables m15257b(int i) {
        this.f9615o = i;
        return this;
    }

    public Variables m15258b(boolean z) {
        this.f9605e = z;
        return this;
    }

    public Variables m15259c(boolean z) {
        this.f9606f = z;
        return this;
    }

    public Variables m15260d(boolean z) {
        this.f9604d = z;
        return this;
    }

    public Variables m15261e(boolean z) {
        this.f9607g = z;
        return this;
    }

    public Variables m15262f(boolean z) {
        this.f9616p = z;
        return this;
    }
}
