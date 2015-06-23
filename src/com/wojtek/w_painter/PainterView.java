package com.wojtek.w_painter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class PainterView extends View {

	// drawing path
	private Path drawPath;
	// drawing and canvas paint
	private Paint drawPaint, canvasPaint;
	// initial color
	private int paintColor = 0xFF000000;
	// canvas
	private Canvas drawCanvas;
	// canvas bitmap
	private Bitmap canvasBitmap;

	public PainterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupPainting();

	}

	private void setupPainting() {
		drawPath = new Path();

		drawPaint = new Paint();
		drawPaint.setColor(paintColor);
		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(getResources()
				.getInteger(R.integer.small_size));
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);

		canvasPaint = new Paint(Paint.DITHER_FLAG);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// view given size
		super.onSizeChanged(w, h, oldw, oldh);

		canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		drawCanvas = new Canvas(canvasBitmap);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// draw view

		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
		canvas.drawPath(drawPath, drawPaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float touchX = event.getX();
		float touchY = event.getY();
		// respond to down, move and up events
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			drawPath.moveTo(touchX, touchY);
			break;
		case MotionEvent.ACTION_MOVE:
			drawPath.lineTo(touchX, touchY);
			break;
		case MotionEvent.ACTION_UP:
			drawPath.lineTo(touchX, touchY);
			drawCanvas.drawPath(drawPath, drawPaint);
			drawPath.reset();
			break;
		default:
			return false;
		}
		// redraw
		invalidate();
		return true;

	}

	public void setColor(String newColor) {
		// set color

		invalidate();
		paintColor = Color.parseColor(newColor);
		drawPaint.setColor(paintColor);
		// Log.d("kolor", "" + paintColor);
	}

	public void setColor(int intColor) {
		drawPaint.setColor(intColor);
	}

	public void setBrushSize(int size) {
		drawPaint.setStrokeWidth(size);
	}

	public void setEraserColor(int backgroundColor) {
		this.paintColor = backgroundColor;
		drawPaint.setColor(backgroundColor);
	}

	public void setStyle(Style style) {
		drawPaint.setStyle(style);
	}
	
	public void setShader(Shader shader){
		drawPaint.setShader(shader);
	}
	
	public void startNew(){
	    drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
	    invalidate();
	    
	}

}
