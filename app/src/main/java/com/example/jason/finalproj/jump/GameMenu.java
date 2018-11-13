package com.example.jason.finalproj.jump;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by BH Qin on 2017/12/21.
 */

public class GameMenu {
    private Bitmap box;
    private Bitmap player;
    private Bitmap play_btn;
    private Bitmap play_btn_press;
    private boolean isPress;
    private float btnX,btnY,playerX,playerY,boxX,boxY;
    private float Screenheight,Screenwidth;
    public GameMenu(Bitmap box,Bitmap play_btn,Bitmap play_btn_press,Bitmap player){
        this.box = box;
        this.player = player;
        this.play_btn = play_btn;
        this.play_btn_press = play_btn_press;
        Screenheight = JumpView.screenheight;
        Screenwidth = JumpView.screenwidth;
        isPress = false;
        btnX = Screenwidth/2-play_btn.getWidth()/2;
        btnY = Screenheight/2-play_btn.getHeight()/2;
        //Player初始高度为中心处于屏幕高度的4/5
        playerX = Screenwidth/2 - player.getWidth()/2;
        playerY = Screenheight*4/5- player.getHeight()/2;
        boxX = Screenwidth/2 - box.getWidth()/2;
        boxY = playerY+player.getHeight()-25;//人工调参了兄弟
    }
    public  void draw(Canvas canvas, Paint paint){ //绘制界面
        if(isPress){ //根据状态绘制btn
            canvas.drawBitmap(play_btn_press,btnX,btnY,paint);
        }
        else{
            canvas.drawBitmap(play_btn,btnX,btnY,paint);
        }
        canvas.drawBitmap(player,playerX,playerY,paint);
        canvas.drawBitmap(box,boxX,boxY,paint);
    }
    public void onTouchEvent(MotionEvent event){//相应触屏事件函数
        int pointX = (int) event.getX();
        int pointY = (int) event.getY();
        //如果点击或者在按钮上滑动
        if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE){
            if(pointX > btnX && pointX < btnX + play_btn.getWidth()){
                if(pointY > btnY && pointY < btnY + play_btn.getHeight()){//在按钮范围内
                    isPress = true;
                }
            }
        }else if(event.getAction() == MotionEvent.ACTION_UP){//抬起时手在别处此次按下就不算
            if(pointX > btnX && pointX < btnX + play_btn.getWidth()) {
                if (pointY > btnY && pointY < btnY + play_btn.getHeight()) {//在按钮范围内
                    isPress = false;
                    JumpView.boxInitY = JumpView.screenheight/2+player.getWidth()/2-25;
                    JumpView.gameState = JumpView.GAMING;
                    Log.i("test","toGaming");
                }
                else isPress = false;
            }
            else isPress = false;
        }
    }
}
