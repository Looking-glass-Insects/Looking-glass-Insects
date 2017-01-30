package com.example.heyong.game2048;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class ItemView extends View {
    private String text = " ";
    private int textColor = Color.BLACK;
    private float textSize = 20;
    private int bgColor = Color.parseColor("#CCC0B3");

    private Paint paint;
    private Rect rect;

    private int id;

    public ItemView(Context context) {
        super(context);
        init(null, 0);
    }

    public ItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ItemView, defStyle, 0);

        text = a.getString(R.styleable.ItemView_text);
        textColor = a.getColor(R.styleable.ItemView_text_color, textColor);
        textSize = a.getDimension(R.styleable.ItemView_text_size, textSize);
        bgColor = a.getColor(R.styleable.ItemView_bg_color, bgColor);
        a.recycle();

        // Set up a default TextPaint object
        paint = new TextPaint();
        paint.setTextSize(textSize);
        rect = new Rect();
        if (text == null)
            text = " ";
        paint.getTextBounds(text, 0, text.length(), rect);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(bgColor);
        float r = Util.dip2px(getContext(), 5);
        canvas.drawRoundRect(getPaddingLeft(), getPaddingTop(), getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), getMeasuredHeight() - getPaddingBottom() - getPaddingTop(), r, r, paint);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        canvas.drawText(text, getWidth() / 2 - rect.width() / 2 - getPaddingLeft(), getHeight() / 2 + rect.height() / 2 , paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            float textWidth = rect.width();
            width = (int) (getPaddingLeft() + textWidth + getPaddingRight());
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            float textHeight = rect.height();
            height = (int) (getPaddingTop() + textHeight + getPaddingBottom());
        }
        setMeasuredDimension(width, height);
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        paint.setTextSize(textSize);
        paint.getTextBounds(text, 0, text.length(), rect);
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }
}
