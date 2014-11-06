package com.bird.note.test;

import com.bird.note.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

public class Eraser_Use_drawCircle extends Activity {

    private int SCREEN_W;

    private int SCREEN_H;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyDrawView(this));

    }

    class MyView extends View {
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Paint mPaint;
        int x = 0;

        int y = 0;

        int r = 0;
        
        public MyView(Context context) {
            super(context);
            setFocusable(true);
            setScreenWH();
            setBackGround();
            
            // 1.if cover is a image,you can open MENU_ITEM_COMMENT bellow
            Bitmap bm = createBitmapFromSRC();
            // if you want to set cover image's alpha,you can open MENU_ITEM_COMMENT bellow
            bm = setBitmapAlpha(bm, 100);
            // if you want to scale cover image,you can open MENU_ITEM_COMMENT bellow
            bm = scaleBitmapFillScreen(bm);

            // 2.if cover is color
            //Bitmap bm = createBitmapFromARGB(0x8800ff00, SCREEN_W, SCREEN_H);
            setCoverBitmap(bm);

        }

        private void setScreenWH() {
            // get screen info
            DisplayMetrics dm = new DisplayMetrics();
            dm = this.getResources().getDisplayMetrics();
            // get screen width
            int screenWidth = dm.widthPixels;
            // get screen height
            int screenHeight = dm.heightPixels;

            SCREEN_W = screenWidth;
            SCREEN_H = screenHeight;
        }

        private Bitmap createBitmapFromSRC() {
            return BitmapFactory.decodeResource(getResources(),
                                                R.drawable.ic_launcher);
        }

        /**
         * 
         * @param colorARGB should like 0x8800ff00
         * @param width
         * @param height
         * @return
         */
        private Bitmap createBitmapFromARGB(int colorARGB, int width, int height) {
            int[] argb = new int[width * height];

            for (int i = 0; i < argb.length; i++) {

                argb[i] = colorARGB;

            }
            return Bitmap.createBitmap(argb, width, height, Config.ARGB_8888);
        }

        /**
         * 
         * @param bm
         * @param alpha ,and alpha should be like ox00000000-oxff000000
         * @note set bitmap's alpha
         * @return
         */
       /* private Bitmap setBitmapAlpha(Bitmap bm, int alpha) {
            int[] argb = new int[bm.getWidth() * bm.getHeight()];
            bm.getPixels(argb, 0, bm.getWidth(), 0, 0, bm.getWidth(), bm
                    .getHeight());


            for (int i = 0; i < argb.length; i++) {

                argb[i] = ((alpha) | (argb[i] & 0x00FFFFFF));
            }
            return Bitmap.createBitmap(argb, bm.getWidth(), bm.getHeight(),
                                       Config.ARGB_8888);
        }*/
        
        /**
         * 
         * @param bm
         * @param alpha ,alpha should be between 0 and 255
         * @note set bitmap's alpha
         * @return
         */
        private Bitmap setBitmapAlpha(Bitmap bm, int alpha) {
            int[] argb = new int[bm.getWidth() * bm.getHeight()];
            bm.getPixels(argb, 0, bm.getWidth(), 0, 0, bm.getWidth(), bm
                    .getHeight());

            for (int i = 0; i < argb.length; i++) {

                argb[i] = ((alpha << 24) | (argb[i] & 0x00FFFFFF));
            }
            return Bitmap.createBitmap(argb, bm.getWidth(), bm.getHeight(),
                                       Config.ARGB_8888);
        }
        
        /**
         * 
         * @param bm
         * @note if bitmap is smaller than screen, you can scale it fill the screen.
         * @return 
         */
        private Bitmap scaleBitmapFillScreen(Bitmap bm) {
            return Bitmap.createScaledBitmap(bm, SCREEN_W, SCREEN_H, true);
        }

        
        private void setBackGround() {
            setBackgroundResource(R.drawable.mark_bg_blue);
        }

        /**
         * 
         * @param bm
         * @note set cover bitmap , which  overlay on background. 
         */
        private void setCoverBitmap(Bitmap bm) {
            // setting paint
            mPaint = new Paint();
            mPaint.setAlpha(0);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            mPaint.setAntiAlias(true);

            // converting bitmap into mutable bitmap
            mBitmap = Bitmap.createBitmap(SCREEN_W, SCREEN_H, Config.ARGB_8888);
            mCanvas = new Canvas();
            mCanvas.setBitmap(mBitmap);
            // drawXY will result on that Bitmap
            // be sure parameter is bm, not mBitmap
            mCanvas.drawBitmap(bm, 0, 0, null);
        }

       

        @Override
        protected void onDraw(Canvas canvas) {
         // draw a circle that is erasing bitmap
            mCanvas.drawCircle(x, y, r, mPaint);
            canvas.drawBitmap(mBitmap, 0, 0, null);
            super.onDraw(canvas);
        }
        
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            // set parameter to draw circle on touch event
            x = (int) event.getX();
            y = (int) event.getY();
            r = 20;
            // Atlast invalidate canvas
            invalidate();
            return true;
        }
    }
}
