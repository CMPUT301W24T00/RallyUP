package com.example.rallyup.uiReference.attendees;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.rallyup.R;

/**
 * This is a class for a drawable based on a text
 */
public class TextDrawable extends Drawable {

    private final String text;
    private final Paint paint;

    /**
     * Constructs a text drawable based on a given context
     * @param context the context for this object
     * @param text the string that this object references
     */
    public TextDrawable(Context context, String text){
        this.text = text;

        this.paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.darkColor);

        paint.setColor(color);
        paint.setTextSize(240);
        paint.setAntiAlias(true);
        paint.setFakeBoldText(true);
        paint.setShadowLayer(6, 0, 0, Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
    }

    /**
     * This method takes a canvas that is to be drawn on
     * @param canvas The canvas to draw into
     */
    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawText(text, 145,230, paint);
    }

    /**
     * This method allows us to set the drawables transparency
     * @param alpha the parameter for transparency
     */
    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    /**
     * This method allows a user to set a color filter
     * @param colorFilter The color filter to apply, or {@code null} to remove the
     *            existing color filter
     */
    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        setColorFilter(colorFilter);
    }

    /**
     * This method returns the opacity of the drawable to the user
     * @return an integer for opacity
     */
    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
