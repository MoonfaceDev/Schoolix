package com.moonface.schoolix;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.Matrix4f;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.renderscript.ScriptIntrinsicColorMatrix;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;

public class ElevatedImageView extends AppCompatImageView {

    public ElevatedImageView(Context context) {
        super(context);
        init(null);
    }
    public ElevatedImageView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        init(attributeSet);
    }
    public ElevatedImageView(Context context, AttributeSet attributeSet, int style){
        super(context, attributeSet, style);
        init(attributeSet);
    }
    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ElevatedImageView);

        int elevation = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? Math.round(customElevation) : 0;

        customElevation = a.getDimensionPixelSize(R.styleable.ElevatedImageView_compatElevation, elevation);

        clipShadow = a.getBoolean(R.styleable.ElevatedImageView_clipShadow, false);

        isTranslucent = a.getBoolean(R.styleable.ElevatedImageView_isTranslucent, false);

        forceClip = a.getBoolean(R.styleable.ElevatedImageView_forceClip, false);

        a.recycle();
    }

    private boolean clipShadow = false;

    private Bitmap shadowBitmap;

    private float customElevation = 0f;

    private Rect rect = new Rect();

    private boolean forceClip = false;

    boolean isTranslucent = false;

    private RenderScript rs;
    private ScriptIntrinsicBlur blurScript;
    private ScriptIntrinsicColorMatrix colorMatrixScript;

    @Override
    public void setElevation(float elevation) {
        customElevation = elevation;
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (!isInEditMode() && canvas != null) {
            if (shadowBitmap == null && customElevation > 0) {
                generateShadow();
            }
            Rect bounds = getDrawable().copyBounds();
            if (shadowBitmap != null) {
                canvas.save();

                if (!clipShadow) {
                    canvas.getClipBounds(rect);
                    rect.inset(Math.round(-2 * getBlurRadius()), Math.round(-2 * getBlurRadius()));
                    if (forceClip) {
                        canvas.clipRect(rect);
                    } else {
                        canvas.save();
                        canvas.clipRect(rect);
                    }
                    canvas.drawBitmap(shadowBitmap, Math.round(bounds.left) - getBlurRadius(), bounds.top - getBlurRadius() / 2f, null);
                }

                canvas.restore();
            }
        }
        super.onDraw(canvas);
    }

    @Override
    public void invalidate() {
        shadowBitmap = null;
        super.invalidate();
    }

    @Override
    public void onDetachedFromWindow() {
        if (!isInEditMode()) {
            blurScript.destroy();
            colorMatrixScript.destroy();
            rs.destroy();
        }
        super.onDetachedFromWindow();
    }

    @Override
    public void onAttachedToWindow() {
        if (!isInEditMode()) {

            if (forceClip) {
                ((ViewGroup) getParent()).setClipChildren(false);
            }
            rs = RenderScript.create(getContext());
            Element element = Element.U8_4(rs);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                blurScript = ScriptIntrinsicBlur.create(rs, element);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                colorMatrixScript = ScriptIntrinsicColorMatrix.create(rs, element);
            }
        }
        super.onAttachedToWindow();
    }

    private Float getBlurRadius(){
        float maxElevation = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24f, getResources().getDisplayMetrics());
        return Math.min(25f * (customElevation / maxElevation), 25f);
    }

    private Bitmap getShadowBitmap(Bitmap bitmap) {
        Allocation allocationIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allocationOut = Allocation.createTyped(rs, allocationIn.getType());

        Matrix4f matrix = isTranslucent ? new Matrix4f(new float[]{
                    0.4f, 0f, 0f, 0f,
                    0f, 0.4f, 0f, 0f,
                    0f, 0f, 0.4f, 0f,
                    0f, 0f, 0f, 0.6f}) : new Matrix4f(new float[]{
                    0f, 0f, 0f, 0f,
                    0f, 0f, 0f, 0f,
                    0f, 0f, 0f, 0f,
                    0f, 0f, 0f, 0.4f});

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            colorMatrixScript.setColorMatrix(matrix);
            colorMatrixScript.forEach(allocationIn, allocationOut);
            blurScript.setRadius(getBlurRadius());

            blurScript.setInput(allocationOut);
            blurScript.forEach(allocationIn);
        }

        allocationIn.copyTo(bitmap);

        allocationIn.destroy();
        allocationOut.destroy();

        return bitmap;
    }

    private void generateShadow() {
        if(getDrawable() != null) {
            shadowBitmap = getShadowBitmap(getBitmapFromDrawable());
        }
    }

    private Bitmap getBitmapFromDrawable(){
        Drawable drawable = getDrawable();

        float blurRadius = getBlurRadius();

        int width = getWidth() + 2 * Math.round(blurRadius);
        int height = getHeight() + 2 * Math.round(blurRadius);

        Bitmap bitmap = width <= 0 || height <= 0 ? Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) : Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        Matrix imageMatrix = getImageMatrix();
        canvas.translate(getPaddingLeft() + blurRadius, getPaddingTop() + blurRadius);
        if (imageMatrix != null) {
            canvas.concat(imageMatrix);
        }
        drawable.draw(canvas);

        return bitmap;
    }
}
