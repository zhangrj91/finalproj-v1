package com.example.jason.finalproj.jump;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;


/**
 * Created by BH Qin on 2018/1/1.
 */

public class Box {
    private Bitmap box_img;
    public float boxX,boxY;
    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private static final int MOVE = 1;
    private static final int STOP = 0;
    private int dir;
    private int act;
    public boolean isAlive;
    private static final int MAXFRAME = 6; //移动动画共3帧
    private int currentFrame;//当前帧

    public Box(Bitmap box_img,float boxX,float boxY){
        this.box_img = box_img;
        isAlive = true;
        act = STOP;
        this.boxX = boxX;
        this.boxY = boxY;
    }

    public void Draw(Canvas canvas, Paint paint){
        canvas.save();
        canvas.drawBitmap(box_img,boxX,boxY,paint);
        canvas.restore();
    }

    public void onTouchEvent(MotionEvent event){
        int pointX = (int) event.getX();
        //如果点击
        if(event.getAction() == MotionEvent.ACTION_DOWN ){
            if(act == STOP){ // 处于静止状态才接受新事件
                act = MOVE;
                if(pointX < JumpView.screenwidth/2) dir = LEFT; //判断左右
                else dir = RIGHT;
                currentFrame = 0;
            }
        }
    }

    public void logic(){
        if(act == MOVE){
            if(currentFrame<4){ //前4帧移动
                boxX = (dir==LEFT)?(boxX + box_img.getWidth()/4-10):(boxX - box_img.getWidth()/4+30/4);
                boxY = boxY + box_img.getHeight()/4-10;
            }
            currentFrame = (currentFrame+1)%MAXFRAME;//人物一共跳6帧
            act = currentFrame==5?STOP:MOVE; //直到人物动画结束才接受新事件
        }
        isAlive = judge();
    }

    /**
     *
     * @return 是否出屏幕
     */
    public boolean judge(){
        if(boxX+box_img.getWidth()> JumpView.screenwidth||boxX<0) return false;
        if(boxY+box_img.getHeight()> JumpView.screenheight) return false;
        return true;
    }
}
