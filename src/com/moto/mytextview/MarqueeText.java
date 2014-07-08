package com.moto.mytextview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by chen on 14-6-16.
 */
public class MarqueeText extends TextView implements Runnable {
    private int currentScrollX;// 当前滚动的位置

    private boolean isStop = false;

    private int textWidth;

    private boolean isMeasure = false;



    public MarqueeText(Context context) {

        super(context);

        // TODO Auto-generated constructor stub

    }



    public MarqueeText(Context context, AttributeSet attrs) {

        super(context, attrs);

    }



    public MarqueeText(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);

    }



    @Override

    protected void onDraw(Canvas canvas) {

        // TODO Auto-generated method stub

        super.onDraw(canvas);

        if (!isMeasure) {// 文字宽度只需获取一次就可以了

            getTextWidth();

            isMeasure = true;

        }

    }



            /**
             31
             * 获取文字宽度
             32
             */

    private void getTextWidth() {

        Paint paint = this.getPaint();

        String str = this.getText().toString();

        textWidth = (int) paint.measureText(str);

    }



//重写setText 在setText的时候重新计算text的宽度

    @Override

    public void setText(CharSequence text, BufferType type) {

        // TODO Auto-generated method stub

        super.setText(text, type);

        this.isMeasure = false;

    }

    @Override

    public void run() {

        currentScrollX += 1;// 滚动速度

        scrollTo(currentScrollX, 0);

        if (isStop) {

            return;

        }

        if (getScrollX() >=  textWidth) {

            scrollTo(-this.getWidth(), 0);

            currentScrollX = -this.getWidth();

//                        return;

        }

        postDelayed(this, 5);

    }


            // 开始滚动

    public void startScroll() {

        isStop = false;

        this.removeCallbacks(this);

        post(this);

    }



            // 停止滚动

    public void stopScroll() {

        isStop = true;

    }



            // 从头开始滚动

    public void startFor0() {

        currentScrollX = 0;

        startScroll();

    }

}
