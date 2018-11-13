package com.example.jason.finalproj.jump;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.jason.finalproj.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by BH Qin on 2017/12/27.
 */

public class JumpView extends SurfaceView implements SurfaceHolder.Callback,Runnable {
    //划分游戏状态
    public static final int GAME_MENU = 0;
    public static final int GAMING = 1;
    public static final int GAME_WIN = 2;
    public static final int GAME_LOSE = 3;
    public static final int GAME_PAUSE = -1;
    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    public static int gameState = GAME_MENU;
    //游戏基础资源
    public static float screenwidth;
    public static float screenheight;
    public static float boxInitY;
    private Resources res = this.getResources();//声明Resources实例以便加载图片
    private Bitmap background;
    private Bitmap play_btn;
    private Bitmap play_btn_press;
    private Bitmap player_img;
    private Bitmap box_img;
    private Bitmap music_play;
    //游戏系统资源
    private SoundPool soundPool;
    private Canvas canvas;
    private Paint paint;
    private SurfaceHolder surfaceHolder;
    private Thread mthread;
    private boolean talive;//线程消亡标识
    private int soundId;
    private Handler handler;
    //游戏状态和元素实例
    private GameMenu gameMenu;
    private Player player;
    private List<Box> boxes;
    //更新地图
    private Integer currentLevel;//当前速度等级
    private int upleveltimes;//要多少次之后升一个等级
    private int currenttimes;//判断什么时候升级
    private int currentJumptimes;//距离上次播放音乐跳了几次
    private int currentPosInMap;//当前在map什么位置
    private Integer points;//分数
    private int[] mymap;
    //音乐播放
    private boolean musicplaying;
    private boolean needplay;
    private int currentmusic;
    private static final int MUSICDONE = 1;
    //判断游戏结束
    private boolean isDead;
    public JumpView(Context context) {
        super(context);
        //Log.i("test","构造函数");
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        paint = new Paint();
        gameState = GAME_MENU;
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC,100);
        soundId = soundPool.load(context, R.raw.jumpmusic2,1);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
      //  Log.i("test","sufaceCreated");
        initGame();
        talive = true;
        mthread = new Thread(this);
    //    Log.i("test","mthread.start");
        mthread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
     //   Log.i("test","sufaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
      //  Log.i("test","sufaceDestroyed");
        talive = false;
    }

    /**
     *  自定义的各种函数
     */

    /**
     * 游戏初始化
     */
    public void initGame(){
       // Log.i("test","initGame");
        if(gameState == GAME_MENU) {
          //  Log.i("test","new object");
            screenwidth = this.getWidth();
            screenheight = this.getHeight();
            background = BitmapFactory.decodeResource(res, R.drawable.bg);
            play_btn = BitmapFactory.decodeResource(res, R.drawable.play_button);
            play_btn_press = BitmapFactory.decodeResource(res, R.drawable.play_button_press);
            music_play = BitmapFactory.decodeResource(res,R.drawable.musicplay);
            Bitmap temp = BitmapFactory.decodeResource(res, R.drawable.dinosaur);//源图片太大了，缩小一点
            Bitmap box_temp = BitmapFactory.decodeResource(res,R.drawable.box);
            Matrix smaller = new Matrix();
            smaller.postScale(0.2f, 0.2f);
            Matrix smallerBox = new Matrix();
            smallerBox.postScale(0.75f,0.75f);
            player_img = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), smaller, true);
            box_img = Bitmap.createBitmap(box_temp,0,0,box_temp.getWidth(),box_temp.getHeight(),smallerBox,true);
            gameMenu = new GameMenu(box_img, play_btn, play_btn_press, player_img);
            player = new Player(player_img);
            boxes = new ArrayList<Box>();
            boxInitY = screenheight/2+player_img.getHeight()/2-25;
            addBox(screenwidth/2 - box_img.getWidth()/2,boxInitY);
            mymap = new int[]{ 0,0,0,1,1,0,1,1,0,1,
                    1,1,0,1,0,0,1,0,1,0,
                    1,0,1,0,1,0,0,1,0,1,
                    0,1,0,1,0,0,0,0,0,1,
                    1,1,1,1,1,1,0,0,1,0,
                    1,0,0,1,1,0,0,1,1,0,
                    1,0,1,0,1,1,1,0,0,1,
                    1,0,0,0,0,1,0,1,1,0,
                    1,1,0,1,1,1,0,1,0,0,
                    1,1,0,1,1,0,0,1,1,1,
                    1,0,0,1,0,1,0,0,1,1,
                    0,1,1,1,1,0,1,1,1,1,
                    1,0,1,1,1,0,1,1,1,0,
                    1,1,1,0,0,1,1,1,1,1,
                    1,0,0,1,1,1,1,0,1,1,
                    1,0,0,0,1,1,1,1,1,0,
                    1,1,1,1,1,1,0,1,1,1,
                    1,1,1,1,0,1,1,1,1,1};
            currentLevel=1;//当前等级
            currenttimes=0;//当前等级进行进度
            upleveltimes=3;//当前等级进行3次升一级
            currentJumptimes=0;//跳的次数为0表示上一次播放完成后一次没跳
            Random random = new Random();
            currentPosInMap = random.nextInt(mymap.length)/10;//随机一个开始位置
            currentmusic = currentPosInMap;
            Integer tem = mymap[currentPosInMap];
            //Log.i("test","nextpos:"+tem.toString());
            isDead = false;
            points = 0;
            musicplaying = false;
            needplay = true;
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if(msg.what==MUSICDONE){
                        musicplaying = false;
                    }
                }
            };
        }
    }

    /**
     *
     * @param x
     * @param y
     * 加盒子
     */
    public void addBox(float x,float y){
        Box box = new Box(box_img,x,y);
        boxes.add(box);
    }

    /**
     *
     * @param canvas
     * @param paint
     * 画盒子
     */
    public void DrawBoxes(Canvas canvas,Paint paint){
        if(!boxes.isEmpty()){
            for(Box box:boxes){
                box.Draw(canvas,paint);
            }
        }
    }

    /**
     *  盒子移动逻辑
     */
    public void BoxesLogic(){
        if(!boxes.isEmpty()){
            for(Iterator<Box> it = boxes.iterator();it.hasNext();){ //一定要用Iterator来遍历删除
                Box box = (Box) it.next();
                if(!box.isAlive){
                    it.remove();
                }else box.logic();
            }
        }
    }
    /**
     * 盒子列表的触屏逻辑
     */
    public void BoxesonTouchEvent(MotionEvent event){
        if(!boxes.isEmpty()){
            for(Box box:boxes){
                box.onTouchEvent(event);
            }
        }
    }


    public boolean judgeDead(int dir){
        if(dir == mymap[currentPosInMap]) return false;
        return true;
    }
    /**
     *
     * @param event
     * @return  触屏监听
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
       // Log.i("test","onTouchEvent");
        switch (gameState){
            case GAME_MENU:
                gameMenu.onTouchEvent(event);
                break;
            case GAMING:
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(!isDead){
                       // Log.i("test","here");
                        Integer tt = currentJumptimes;
                       // Log.i("test","currentJ:"+ tt.toString()+" currentL:"+currentLevel.toString());
                        if(needplay){
                            musicplaying = true;
                            currentmusic = currentPosInMap;
                           // Log.i("test","musicplaying");
                            //Log.i("test","currentlevel:"+currentLevel.toString());

                           int[] next_temp = new int[currentLevel];
                           for(int i=0;i<currentLevel;i++){
                               next_temp[i] = mymap[currentmusic];
                               currentmusic = (currentmusic+1)%mymap.length;
                           }
                            final int[] next = next_temp;
                            final int musictimes = currentLevel;
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for(int i=0;i<musictimes;i++){
                                        int leftVolum = next[i]==0?1:0;
                                        int rightVolum = next[i]==1?1:0;
                                        soundPool.play(soundId,leftVolum,rightVolum,0,0,1);
                                        try{
                                            Thread.sleep(400);
                                        }catch (InterruptedException e){
                                            Log.e("test","播放出错"+e.toString());
                                        }
                                    }
                                }
                            });
                            thread.start();
                            //Log.i("test","musicplayingdone");
                            currentJumptimes = 0;
                            musicplaying = false;
                            needplay = false;
                        }else{
                            player.onTouchEvent(event);
                            BoxesonTouchEvent(event);
                        }
                    }
                }
                break;
            default:break;
        }
        return true;
    }

    /**
     *  游戏逻辑函数
     */
    public void logic(){
        switch (gameState) {
            case GAMING:
                if(!isDead){
                    if(!musicplaying){//播放音乐时不执行任何逻辑
                        player.logic();
                        BoxesLogic();
                        if(player.currentFrame==4){
                            if(!judgeDead(player.dir)){
                                currentPosInMap = (currentPosInMap + 1)%mymap.length;
                                addBox(screenwidth/2-box_img.getWidth()/2,boxInitY);//没死加一个盒子
                                Integer temp = mymap[currentPosInMap];
                                points++;
                                currentJumptimes++;
                                if(currentJumptimes==currentLevel){
                                    needplay = true;
                                    currenttimes++;
                                    if(currenttimes==upleveltimes){
                                        currenttimes = 0;
                                        currentLevel++;
                                    }
                                }
                                //Integer t = currentJumptimes;
                                //Log.i("test","currentJumptimes:"+t.toString());
                                //Log.i("test:","no dead,next:"+temp.toString());
                            } else{
                                isDead = true;
                                Log.i("test:","dead");
                            }
                        }
                    }
                }else gameState =GAME_LOSE;
                break;
            case GAME_LOSE:
                break;
            default:
                break;
        }
    }
    /**
     *  绘制函数
     */
    public void Draw() {
        //Log.i("test", "Draw");
        try {
            canvas = surfaceHolder.lockCanvas();
            if(canvas!=null){
                canvas.drawBitmap(background, 0, 0, paint); //画背景
                /*Paint paint1 = new Paint();//draw for test
                paint1.setColor(Color.BLACK);
                canvas.drawLine(screenwidth/2, 0, screenwidth/2, screenheight, paint1);
                canvas.drawLine(0, screenheight/2, screenwidth, screenheight/2, paint1);*/
                Paint p_title = new Paint();
                p_title.setColor(Color.BLACK);
                p_title.setTextSize(100);
                switch (gameState) {
                    case GAME_MENU:
                        gameMenu.draw(canvas, paint);
                        break;
                    case GAMING:
                        canvas.drawText("LEVEL: "+currentLevel.toString(),screenwidth/2-150,200,p_title);
                        canvas.drawText("POINTS: "+points.toString(),screenwidth/2-150,370,p_title);
                        if(needplay) canvas.drawBitmap(music_play,screenwidth*1/4,screenheight*1/4,paint);
                        player.draw(canvas, paint);
                        DrawBoxes(canvas,paint);
                        break;
                    case GAME_LOSE:
                        canvas.drawText("YOU DEAD!",screenwidth/2-260,500,p_title);
                        canvas.drawText("YOUR POINTS: "+points.toString(),screenwidth/2-360,screenheight/2,p_title);
                        Paint p = new Paint();
                        p.setColor(Color.BLACK);
                        p.setTextSize(60);
                        canvas.drawText("GOOD LUCK NEXT TIME!",screenwidth/2-330,screenheight/2+200,p);
                        break;
                    default:
                        break;
                }
            }
        }catch (Exception e){

        }finally {
            if(canvas!=null){
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }
    /**
     *  游戏运行
     */
    @Override
    public void run() {
        //Log.i("test", "界面刷新");
        while (talive){
            long start = System.currentTimeMillis();//逻辑执行开始时间
            logic();
            Draw();
            long end = System.currentTimeMillis();//逻辑执行结束
            //Long test =  end - start;
            //Log.i("test","线程执行共: " + test.toString()+"ms");
            try {
                if(end-start<100){ //每秒10帧
                    Thread.sleep(100-(end-start));//休眠以保证每次刷新时间间隔一致
                }
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

}
