package ar.edu.utn.frba.dadm.clases2018c2.clases_2018c2;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class AspectRatioFrameLayout extends FrameLayout {

    private float widthHeightRatio = 1;
    private int fixedEdge = UNDEFINED;

    public static final int UNDEFINED = 0;
    public static final int FIXED_WIDTH = -1;
    public static final int FIXED_HEIGHT = 1;

    public AspectRatioFrameLayout(@NonNull Context context) {
        super(context);
    }

    public AspectRatioFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioFrameLayout);
        widthHeightRatio = a.getFloat(R.styleable.AspectRatioFrameLayout_widthHeightRatio, widthHeightRatio);
        fixedEdge = a.getInt(R.styleable.AspectRatioFrameLayout_fixedEdge, fixedEdge);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        boolean fixedSizeIsWidth;
        switch (fixedEdge) {
            case FIXED_WIDTH:
                fixedSizeIsWidth = true;
                break;
            case FIXED_HEIGHT:
                fixedSizeIsWidth = false;
                break;
            case UNDEFINED:
            default:
                fixedSizeIsWidth = width > 0 && width < widthHeightRatio * height || height == 0;
                break;
        }
        width = fixedSizeIsWidth? width: (int)(widthHeightRatio * height);
        height = fixedSizeIsWidth? (int)((float)width / widthHeightRatio): height;
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
