package com.moto.user;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

import com.moto.main.R;
import com.moto.utils.Blur;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * 自定义ScrollView
 *
 * @author markmjw
 * @date 2013-09-13
 */
public class PullScrollView extends ScrollView {
    /** 阻尼系数,越小阻力就越大. */
    private static final float SCROLL_RATIO = 0.5f;

    /** 滑动至翻转的距离. */
    private static final int TURN_DISTANCE = 100;

    /** 头部view. */
    private View mHeader;

    /** 头部view高度. */
    private int mHeaderHeight;

    /** 头部view显示高度. */
    private int mHeaderVisibleHeight;

    /** ScrollView的content view. */
    private View mContentView;

    /** ScrollView的content view矩形. */
    private Rect mContentRect = new Rect();

    /** 首次点击的Y坐标. */
    private float mTouchDownY;

    /** 是否关闭ScrollView的滑动. */
    private boolean mEnableTouch = false;

    /** 是否开始移动. */
    private boolean isMoving = false;

    /** 是否移动到顶部位置. */
    private boolean isTop = false;

    /** 头部图片初始顶部和底部. */
    private int mInitTop, mInitBottom;

    /** 头部图片拖动时顶部和底部. */
    private int mCurrentTop, mCurrentBottom;

    /** 状态变化时的监听器. */
    private OnTurnListener mOnTurnListener;


    private Bitmap originbitmap;


    private boolean Isone = true;
    private boolean Istwo = true;
    private boolean Isthree = true;
    private boolean Isfour = true;

    private Context context;

//    private ArrayList<Bitmap> blurbitmaps = new ArrayList<Bitmap>();

    private enum State {
        /**顶部*/
        UP,
        /**底部*/
        DOWN,
        /**正常*/
        NORMAL
    }

    /** 状态. */
    private State mState = State.NORMAL;

    public PullScrollView(Context context) {
        super(context);
        init(context, null);
    }

    public PullScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PullScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }



    private void init(Context context, AttributeSet attrs) {
        // set scroll mode

//        originbitmap = new SoftReference<Bitmap>(BitmapFactory.decodeResource(getResources(), R.drawable.cuttedbackground_me));
//        for(int i = 0; i < 5; i++)
//        {
//            blurbitmaps.add(originbitmap.get());
//        }
        setOverScrollMode(OVER_SCROLL_NEVER);

        if (null != attrs) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PullScrollView);

            if (ta != null) {
                mHeaderHeight = (int) ta.getDimension(R.styleable.PullScrollView_headerHeight, -1);
                mHeaderVisibleHeight = (int) ta.getDimension(R.styleable
                        .PullScrollView_headerVisibleHeight, -1);
                ta.recycle();
            }
        }
    }

    public void setOriginbitmap(Context context,Bitmap originbitmap) {

        this.context = context;
        this.originbitmap = originbitmap;
//        blurbitmaps.set(4,this.originbitmap.get());
        try {
           new BitmapBlurTask().execute(this.originbitmap, context).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        GetIconOne();
//        mHeader.setBackgroundDrawable(new BitmapDrawable(blurbitmaps.get(0)));
    }

    public class BitmapBlurTask extends AsyncTask<Object, String, Bitmap>{

        @Override
        protected Bitmap doInBackground(Object... params) {
            Bitmap bitmaps = (Bitmap)params[0];
//            int radius = (int)(Math.sqrt(Math.pow(bitmaps.get(9).getWidth(),2)+Math.pow(bitmaps.get(9).getHeight(),2))) / 2;
            Context context = (Context)params[1];
            Blur.getInstance(context,bitmaps);
//            mybitmap = new SoftReference<>(bitmaps.get(4));
//            for( int i = 3,j = 2; i >= 0; i--,j+=4)
//            {
//
//
//               try {
//                   String localIconNormal = "icon"+i;
//
//                   FileInputStream localStream = context.openFileInput(localIconNormal);
//
//                  mybitmap = new SoftReference<Bitmap>(BitmapFactory.decodeStream(localStream));
//               }catch (Exception ex)
//               {
//
//               }
//               bitmaps.set(i, mybitmap.get());
//
//            }

            return bitmaps;
        }

        protected void onPostExecute(ArrayList<Bitmap> result) {
            // TODO Auto-generated method stub

        }
    }

    /**
     * 设置Header
     *
     * @param view
     */
    public void setHeader(View view) {
        mHeader = view;
    }

    /**
     * 设置状态改变时的监听器
     *
     * @param turnListener
     */
    public void setOnTurnListener(OnTurnListener turnListener) {
        mOnTurnListener = turnListener;
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            mContentView = getChildAt(0);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (getScrollY() == 0) {
            isTop = true;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mTouchDownY = ev.getY();
            mCurrentTop = mInitTop = mHeader.getTop();
            mCurrentBottom = mInitBottom = mHeader.getBottom();
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mContentView != null) {
            doTouchEvent(ev);
        }

        // 禁止控件本身的滑动.
        return mEnableTouch || super.onTouchEvent(ev);
    }

    /**
     * 触摸事件处理
     *
     * @param event
     */
    private void doTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_MOVE:
                doActionMove(event);
                break;

            case MotionEvent.ACTION_UP:
                // 回滚动画
                if (isNeedAnimation()) {
                    rollBackAnimation();
                }

                if (getScrollY() == 0) {
                    mState = State.NORMAL;
                }
                SetTrue();
                GetIconOne();
                isMoving = false;
                mEnableTouch = false;
                break;

            default:
                break;
        }
    }

    /**
     * 执行移动动画
     *
     * @param event
     */
    private void doActionMove(MotionEvent event) {
        // 当滚动到顶部时，将状态设置为正常，避免先向上拖动再向下拖动到顶端后首次触摸不响应的问题
        if (getScrollY() == 0) {
            mState = State.NORMAL;
            // 滑动经过顶部初始位置时，修正Touch down的坐标为当前Touch点的坐标
            if (isTop) {
                isTop = false;
                mTouchDownY = event.getY();
            }
        }

        float deltaY = event.getY() - mTouchDownY;

        // 对于首次Touch操作要判断方位：UP OR DOWN
        if (deltaY < 0 && mState == State.NORMAL) {
            mState = State.UP;
        } else if (deltaY > 0 && mState == State.NORMAL) {
            mState = State.DOWN;
        }

        if (mState == State.UP) {
            deltaY = deltaY < 0 ? deltaY : 0;

            isMoving = true;
            mEnableTouch = false;

        } else if (mState == State.DOWN) {
            if (getScrollY() <= deltaY) {
                mEnableTouch = true;
                isMoving = true;
            }
            deltaY = deltaY < 0 ? 0 : deltaY;
        }

        if (isMoving) {
            // 初始化content view矩形
            if (mContentRect.isEmpty()) {
                // 保存正常的布局位置
                mContentRect.set(mContentView.getLeft(), mContentView.getTop(), mContentView.getRight(),
                        mContentView.getBottom());
            }

            // 计算header移动距离(手势移动的距离*阻尼系数*0.5)
            float headerMoveHeight = deltaY * 0.5f * SCROLL_RATIO;
            mCurrentTop = (int) (mInitTop + headerMoveHeight);
            mCurrentBottom = (int) (mInitBottom + headerMoveHeight);

            // 计算content移动距离(手势移动的距离*阻尼系数)
            float contentMoveHeight = deltaY * SCROLL_RATIO;

            // 修正content移动的距离，避免超过header的底边缘
            int headerBottom = mCurrentBottom - mHeaderVisibleHeight;
            int top = (int) (mContentRect.top + contentMoveHeight);
            int bottom = (int) (mContentRect.bottom + contentMoveHeight);
            int everyheight = getMaxScrollAmount() / 5;
            if (top <= headerBottom) {
                // 移动content view
                mContentView.layout(mContentRect.left, top, mContentRect.right, bottom);

                // 移动header view
                mHeader.layout(mHeader.getLeft(), mCurrentTop, mHeader.getRight(), mCurrentBottom);
            }

            if(contentMoveHeight >= 0)
            {
//                Log.e("ssss", blurbitmaps.size() + "");
                int count = (int)contentMoveHeight / everyheight;
               if(count == 1 && Isone)
               {
                   GetIcontwo();
                   Isone = false;
               }
                else if( count == 2 && Istwo)
               {
//                   Log.e("two",blurbitmaps.get((int)(contentMoveHeight / everyheight)).toString());
                   GetIconthree();
                   Istwo = false;
               }
                else if(count == 3 && Isthree)
               {
//                   Log.e("three",blurbitmaps.get((int)(contentMoveHeight / everyheight))+"");
//                   mHeader.setBackgroundDrawable(new BitmapDrawable(blurbitmaps.get((int)(contentMoveHeight / everyheight))));
                   GetIconfour();
                   Isthree = false;
               }
                else if(count == 4 && Isfour)
               {
//                   Log.e("four",blurbitmaps.get((int)(contentMoveHeight / everyheight))+"");
//                   mHeader.setBackgroundDrawable(new BitmapDrawable(blurbitmaps.get((int)(contentMoveHeight / everyheight))));
                   GetIconfive();
                   Isfour = false;
               }
            }
            else
            {
//                mHeader.setBackgroundDrawable(new BitmapDrawable(blurbitmaps.get(0)));
                GetIconOne();
                SetTrue();
            }
        }
    }

    private void SetTrue()
    {
        Isone = true;
        Istwo = true;
        Isthree = true;
        Isfour = true;
    }

    private void GetIconOne()
    {
        String localIconNormal = "icon"+0;
        Bitmap bitmap = originbitmap;
        FileInputStream localStream = null;
        try {
            localStream = context.openFileInput(localIconNormal);

        }catch (Exception e)
        {}
        try {
            bitmap = BitmapFactory.decodeStream(localStream);
        }catch (OutOfMemoryError o)
        {}
//                   Log.e("one",blurbitmaps.get((int)(contentMoveHeight / everyheight)).toString());
        mHeader.setBackgroundDrawable(new BitmapDrawable(bitmap));
//        if(bitmap != null && !bitmap.isRecycled())
//        {
//            bitmap.recycle();
//            bitmap = null;
//        }不能够recycle,否则会报错
    }
    private void GetIcontwo()
    {
        String localIconNormal = "icon"+1;
        Bitmap bitmap = originbitmap;
        FileInputStream localStream = null;
        try {
            localStream = context.openFileInput(localIconNormal);

        }catch (Exception e)
        {}
        try {
            bitmap = BitmapFactory.decodeStream(localStream);
        }catch (OutOfMemoryError o)
        {}
//                   Log.e("one",blurbitmaps.get((int)(contentMoveHeight / everyheight)).toString());
        mHeader.setBackgroundDrawable(new BitmapDrawable(bitmap));
//        if(bitmap != null && !bitmap.isRecycled())
//        {
//            bitmap.recycle();
//            bitmap = null;
//        }不能够recycle,否则会报错
    }
    private void GetIconthree()
    {
        String localIconNormal = "icon"+2;
        Bitmap bitmap = originbitmap;
        FileInputStream localStream = null;
        try {
            localStream = context.openFileInput(localIconNormal);

        }catch (Exception e)
        {}
        try {
            bitmap = BitmapFactory.decodeStream(localStream);
        }catch (OutOfMemoryError o)
        {}
//                   Log.e("one",blurbitmaps.get((int)(contentMoveHeight / everyheight)).toString());
        mHeader.setBackgroundDrawable(new BitmapDrawable(bitmap));
//        if(bitmap != null && !bitmap.isRecycled())
//        {
//            bitmap.recycle();
//            bitmap = null;
//        }//不能够recycle,否则会报错
    }
    private void GetIconfour()
    {
        String localIconNormal = "icon"+3;
        Bitmap bitmap = originbitmap;
        FileInputStream localStream = null;
        try {
            localStream = context.openFileInput(localIconNormal);

        }catch (Exception e)
        {}
        try {
            bitmap = BitmapFactory.decodeStream(localStream);
        }catch (OutOfMemoryError o)
        {}
//                   Log.e("one",blurbitmaps.get((int)(contentMoveHeight / everyheight)).toString());
        mHeader.setBackgroundDrawable(new BitmapDrawable(bitmap));
//        if(bitmap != null && !bitmap.isRecycled())
//        {
//            bitmap.recycle();
//            bitmap = null;
//        }不能够recycle,否则会报错
    }
    private void GetIconfive()
    {
        String localIconNormal = "icon"+4;
        Bitmap bitmap = originbitmap;
        FileInputStream localStream = null;
        try {
            localStream = context.openFileInput(localIconNormal);

        }catch (Exception e)
        {}
        try {
            bitmap = BitmapFactory.decodeStream(localStream);
        }catch (OutOfMemoryError o)
        {}
//                   Log.e("one",blurbitmaps.get((int)(contentMoveHeight / everyheight)).toString());
        mHeader.setBackgroundDrawable(new BitmapDrawable(bitmap));
//        if(bitmap != null && !bitmap.isRecycled())
//        {
//            bitmap.recycle();
//            bitmap = null;
//        }不能够recycle,否则会报错
    }

    private void rollBackAnimation() {
        TranslateAnimation tranAnim = new TranslateAnimation(0, 0,
                Math.abs(mInitTop - mCurrentTop), 0);
        tranAnim.setDuration(200);
        mHeader.startAnimation(tranAnim);

        mHeader.layout(mHeader.getLeft(), mInitTop, mHeader.getRight(), mInitBottom);

        // 开启移动动画
        TranslateAnimation innerAnim = new TranslateAnimation(0, 0, mContentView.getTop(), mContentRect.top);
        innerAnim.setDuration(200);
        mContentView.startAnimation(innerAnim);
        mContentView.layout(mContentRect.left, mContentRect.top, mContentRect.right, mContentRect.bottom);

        mContentRect.setEmpty();

        // 回调监听器
        if (mCurrentTop > mInitTop + TURN_DISTANCE && mOnTurnListener != null){
            mOnTurnListener.onTurn();
        }
    }

    /**
     * 是否需要开启动画
     */
    private boolean isNeedAnimation() {
        return !mContentRect.isEmpty() && isMoving;
    }

    /**
     * 翻转事件监听器
     *
     * @author markmjw
     */
    public interface OnTurnListener {
        /**
         * 翻转回调方法
         */
        public void onTurn();
    }
    public void destoryBitmap(){
//        int num = blurbitmaps.size();
//        for(int i = 0; i < num; i++) {
//            if (blurbitmaps.get(i) != null && !blurbitmaps.get(i).isRecycled()) {
//                blurbitmaps.get(i).recycle();
//                blurbitmaps.set(i, null);
//            }
//        }
        if(originbitmap != null && !originbitmap.isRecycled())
        {
            originbitmap.recycle();
            originbitmap = null;
        }
//        if(mybitmap.get() != null && !mybitmap.get().isRecycled())
//        {
//            mybitmap.get().recycle();
//            mybitmap = null;
//        }
//        blurbitmaps.clear();

    }


}
