package geofence.sample.com.viewutils;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import geofence.sample.com.R;

import geofence.sample.com.TypeFaceUtils;

/**
 * Created by sumandas on 04/12/2016.
 */

public class FontTextView extends TextView {
    public FontTextView(Context context) {
        super(context);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FontTextView);
        String customFont = typedArray.getString(R.styleable.FontTextView_custom_font);
        if(!TextUtils.isEmpty(customFont)) {
            try {
                setTypeface(TypeFaceUtils.get(context, customFont));
            } catch (Exception e) {

            }
        }
        typedArray.recycle();
    }
}
