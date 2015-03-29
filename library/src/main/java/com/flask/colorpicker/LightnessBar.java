package com.flask.colorpicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class LightnessBar extends AbsCustomBar {
	private int color;
	private Paint barPaint = PaintBuilder.newPaint().build();
	private Paint solid = PaintBuilder.newPaint().build();
	private Paint stroke1 = PaintBuilder.newPaint().color(0xffffffff).build();
	private Paint stroke2 = PaintBuilder.newPaint().color(0xff000000).build();

	private ColorPickerView colorPicker;

	public LightnessBar(Context context) {
		super(context);
	}

	public LightnessBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LightnessBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void drawBar(Canvas barCanvas) {
		int width = barCanvas.getWidth();
		int height = barCanvas.getHeight();

		float[] hsv = new float[3];
		Color.colorToHSV(color, hsv);
		int l = Math.max(2, width / 256);
		for (int x = 0; x <= width; x += l) {
			hsv[2] = (float) x / (width - 1);
			barPaint.setColor(Color.HSVToColor(hsv));
			barCanvas.drawRect(x, 0, x + l, height, barPaint);
		}
	}

	@Override
	protected void onValueChanged(float value) {
		colorPicker.setLightness(value);
	}

	@Override
	protected void drawHandle(Canvas canvas, float x, float y) {
		solid.setColor(colorAtLightness(color, value));
		canvas.drawCircle(x, y, handleRadius, stroke1);
		canvas.drawCircle(x, y, handleRadius * 0.8f, stroke2);
		canvas.drawCircle(x, y, handleRadius * 0.6f, solid);
	}

	public void setColorPicker(ColorPickerView colorPicker) {
		this.colorPicker = colorPicker;
	}

	public void setColor(int color) {
		this.color = color;
		this.value = lightnessOfColor(color);
		if (bar != null) {
			updateBar();
			invalidate();
		}
	}

	private int colorAtLightness(int color, float lightness) {
		float[] hsv = new float[3];
		Color.colorToHSV(color, hsv);
		hsv[2] = lightness;
		return Color.HSVToColor(hsv);
	}

	private float lightnessOfColor(int color) {
		float[] hsv = new float[3];
		Color.colorToHSV(color, hsv);
		return hsv[2];
	}
}