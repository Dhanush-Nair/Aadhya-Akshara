package com.leapsoftware.leap.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by vincentrickey on 9/11/15.
 */
public class SimpleDrawingView extends View {

    private final int paintColor = Color.BLACK; //setup initial color
    public Paint drawPaint; //defines paint and canvas

    private Path path = new Path();// User will draw paths. A path can contain many lines, contours, and shapes

    public SimpleDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true); // boolean value that determines whether a view can take a focus. The framework handles moving focus in response to user input. Set to true to allow focus.
        setFocusableInTouchMode(true); // boolean value that allows views to focus
        setupPaint();
    }

    // Get X and Y and append them to the path. A line will be drawn between the two points.
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float pointX = event.getX(); // gets x coordinate of touch event
        float pointY = event.getY(); // gets y coordinate of touch event
        switch (event.getAction()) { // Checks for the event that occurs
            case MotionEvent.ACTION_DOWN:
                path.moveTo(pointX, pointY); // starts a new line in the path
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(pointX, pointY); // draws line between last point and this point
                break;
            default:
                return false;
        }
        postInvalidate(); // indicate view should be redrawn
        return true; // indicate that we've consumed the touch
    }

    // Draws the path created during the touch event
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, drawPaint);
    }

    // Setup paint with color and stroke styles
    private void setupPaint() {
        drawPaint = new Paint(); // The paint class holds the style and color information about how to draw geometries, text, and bitmaps
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(30);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    // Suggested stackoverflow solution to erase a canvas implemented in the method below.
    // http://stackoverflow.com/questions/5729377/android-canvas-how-do-i-clear-delete-contents-of-a-canvas-bitmaps-livin

    // Vince: I don't like that StackOverflow solution.  Just reset the path that you have been tracing to delete the drawing
    public void onErase() {
        path.reset();
    }
}