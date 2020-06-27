package com.example.inf0251atrabalho2.testmovecircle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawCircleView extends View {

    // Record current ball horizontal ordinate.
    private float currX = 100;

    // Record current ball vertical ordinate
    private float currY = 100;

    // This is the circle color.
    private int circleColor = Color.GREEN;

    public int getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
    }

    // getter and setter method for currX and currY.
    public float getCurrX() {
        return currX;
    }

    public float getCurrY() {
        return currY;
    }

    public void setCurrX(float currX) {
        this.currX = currX;
    }

    public void setCurrY(float currY) {
        this.currY = currY;
    }

    // DrawBallView constructor.
    public DrawCircleView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Create a new Paint object.
        Paint paint = new Paint();

        // Set paint color.
        paint.setColor(this.getCircleColor());

        // Draw a circle in the canvas.
        canvas.drawCircle(currX, currY, 35, paint);
    }
}
