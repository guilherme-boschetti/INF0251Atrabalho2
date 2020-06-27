package com.example.inf0251atrabalho2.maze;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;

import com.example.inf0251atrabalho2.R;

public class MazeView extends View {

    private MazeActivity mazeActivity;
    private boolean isInitialized;
    private boolean finish;
    private int mazeColor = Color.GRAY;
    private int ballColor = Color.RED;
    private int backGroundColor = Color.BLACK;
    private int startEndColor = Color.BLUE;
    private int width;
    private int height;
    private int rows;
    private int columns;
    private int ballrow;
    private int ballcol;
    private int crashes;
    private int[][] maze;
    private long startTime;
    private Canvas canvas;
    private Paint paint;
    private Rect[][] rectangles;
    //private String entrance;
    //private String exit;
    private Vibrator vibrator;

    public MazeView(MazeActivity mazeActivity, int level) {
        super(mazeActivity);
        this.mazeActivity = mazeActivity;

        switch (level) {
            case 3:
                maze = Mazes.MAZE_DIFFICULT;
                break;
            case 2:
                maze = Mazes.MAZE_MEDIUM;
                break;
            case 1:
            default:
                maze = Mazes.MAZE_EASY;
                break;
        }

        rows = maze.length;
        columns = maze[0].length;
        //entrance = mazeActivity.getString(R.string.entrance);
        //exit = mazeActivity.getString(R.string.exit);
        vibrator = (Vibrator) mazeActivity.getSystemService(Context.VIBRATOR_SERVICE);

        startTime = System.currentTimeMillis();

        MediaPlayer mp = MediaPlayer.create(mazeActivity, R.raw.maze_start_sound);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mp.start();
    }

    protected void onDraw(Canvas c) {
        super.onDraw(c);
        canvas = c;

        if (!isInitialized) {
            init();
            isInitialized = true;
        } else {

            paint.setColor(backGroundColor);
            canvas.drawRect(0, 0, width, height, paint);
            paint.setColor(mazeColor);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if (maze[i][j] == 1) {
                        canvas.drawRect(rectangles[i][j], paint);
                    }
                }
            }
            drawSrartEndCircle();
        }

    }

    private void init() {

        ballrow = 0;
        ballcol = 0;
        paint = new Paint();
        width = getWidth();
        height = getHeight();
        paint.setColor(backGroundColor);
        canvas.drawRect(0, 0, width, height, paint);
        paint.setColor(mazeColor);

        rectangles = new Rect[rows][columns];

        int lastBottom = 0;
        int newBottom;
        int lastRight;
        int newRight;

        for (int i = 0; i <rows; i++) {
            lastRight = 0;
            newBottom = (height) * (i + 1) / rows;
            for (int j = 0; j < columns; j++) {
                newRight = (width) * (j + 1) / columns;
                rectangles[i][j] = new Rect();
                rectangles[i][j].left = lastRight ;
                rectangles[i][j].top = lastBottom ;
                rectangles[i][j].right = newRight;
                rectangles[i][j].bottom = newBottom;
                if (maze[i][j] == 1)
                    canvas.drawRect(rectangles[i][j], paint);
                lastRight = newRight;
            }
            lastBottom = newBottom;

        }
        drawSrartEndCircle();
    }

    private void drawSrartEndCircle() {

        paint.setColor(startEndColor);
        canvas.drawRect(rectangles[0][0], paint);
        canvas.drawRect(rectangles[rows - 1][columns - 1], paint);
        /*paint.setColor(backGroundColor);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(12);
        canvas.drawText(
                entrance,
                (float) (rectangles[0][0].left + rectangles[0][0].right) / 2,
                (float) (rectangles[0][0].top + rectangles[0][0].bottom) / 2,
                paint);
        canvas.drawText(
                exit,
                (float) (rectangles[rows - 1][columns - 1].left + rectangles[rows - 1][columns - 1].right) / 2,
                (float) (rectangles[rows - 1][columns - 1].top + rectangles[rows - 1][columns - 1].bottom) / 2,
                paint);*/
        paint.setColor(ballColor);
        drawCircle(ballrow, ballcol, ballColor);
    }

    private void drawCircle(int x, int y, int color) {
        paint.setColor(color);
        canvas.drawCircle((float) (rectangles[x][y].left + rectangles[x][y].right) / 2,
                (float) (rectangles[x][y].top + rectangles[x][y].bottom) / 2,
                (float) (rectangles[x][y].right - rectangles[x][y].left) / 2,
                paint);
    }

    public void left() {
        if (finish) {
            return;
        }
        if(ballcol > 0 && maze[ballrow][ballcol - 1] == 0) {
            ballcol--;
            checkFinish();
        } else {
            vibrate();
        }
    }

    public void right() {
        if (finish) {
            return;
        }
        if(ballcol < columns - 1 && maze[ballrow][ballcol + 1] == 0) {
            ballcol++;
            checkFinish();
        } else {
            vibrate();
        }
    }

    public void up() {
        if (finish) {
            return;
        }
        if(ballrow > 0 && maze[ballrow - 1][ballcol] == 0) {
            ballrow--;
            checkFinish();
        } else {
            vibrate();
        }
    }

    public void down() {
        if (finish) {
            return;
        }
        if(ballrow < rows - 1 && maze[ballrow + 1][ballcol] == 0) {
            ballrow++;
            checkFinish();
        } else {
            vibrate();
        }
    }

    private void vibrate() {
        // Vibrate for 50 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vibrator.vibrate(50);
        }
        crashes++;
    }

    private void checkFinish() {
        if(ballrow == rows-1 && ballcol == columns-1) {
            finish = true;

            MediaPlayer mp = MediaPlayer.create(mazeActivity, R.raw.maze_win_sound);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
            mp.start();

            long endTime = System.currentTimeMillis();
            long interval = endTime - startTime;
            double ellapsed = interval / 1000.0;
            mazeActivity.showAlert(mazeActivity.getString(R.string.congratulations), mazeActivity.getString(R.string.crashes_time, crashes, ellapsed));
        }
    }
    
}
