package com.example.jason.finalproj.jump;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;

/**
 * Created by BH Qin on 2017/12/22.
 */

public class Player {
    private Bitmap player_img;
    public float playerX,playerY;
    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private static final int JUMP = 1;
    private static final int STOP = 0;
    public int dir;
    public int act;
    private static final int MAXFRAME = 6; //跳起动画共6帧
    public int currentFrame;//当前帧
    public Player(Bitmap player_img){
        this.player_img = player_img;
        dir = RIGHT;
        act = STOP;
        playerX = JumpView.screenwidth/2 - player_img.getWidth()/2;//保持在屏幕中间
        playerY = JumpView.screenheight/2- player_img.getHeight()/2;
        currentFrame = 0;
    }
    public void draw(Canvas canvas, Paint paint){ //画朝左的还是朝右的图
        if(dir==RIGHT){
            canvas.drawBitmap(player_img,playerX,playerY,paint);
        }
        else {
            Matrix mirro = new Matrix();
            mirro.postScale(-1,1);
            Bitmap player_img_left = Bitmap.createBitmap(player_img,0,0,player_img.getWidth(),player_img.getHeight(),mirro,true);
            canvas.drawBitmap(player_img_left,playerX,playerY,paint);
        }
    }
    public void onTouchEvent(MotionEvent event){
        int pointX = (int) event.getX();
        //如果点击
        if(event.getAction() == MotionEvent.ACTION_DOWN ){
            //Log.i("test","Player keyDOWN");
            if(act == STOP){ // 处于静止状态才接受新事件
                act = JUMP;
                if(pointX < JumpView.screenwidth/2) dir = LEFT; //判断左右
                else dir = RIGHT;
                currentFrame = 0;
            }
        }
    }
    public void logic(){
        //处理玩家移动
        if(act == JUMP){
            switch (currentFrame){
                case 0://上跳
                    playerY = playerY - player_img.getHeight()/2;
                    break;
                case 1:
                    playerY= playerY - (player_img.getHeight()/2)*4/5;
                    break;
                case 2:
                    playerY= playerY - (player_img.getHeight()/2)*1/5;//最高点
                    break;
                case 3://下落
                    playerY= playerY + (player_img.getHeight()/2)*1/5;
                    break;
                case 4:
                    playerY=playerY + (player_img.getHeight()/2)*4/5;
                    break;
                case 5:
                    playerY = playerY + player_img.getHeight()/2;
                    act = STOP;
                    break;
                default:break;
            }
            currentFrame = (currentFrame+1)%MAXFRAME;
        }
    }
}
