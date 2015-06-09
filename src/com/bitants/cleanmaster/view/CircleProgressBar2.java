package com.bitants.cleanmaster.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
 
@SuppressLint("NewApi") public class CircleProgressBar2 extends View {
private int maxProgress = 100;
private int progress = 30,cm=1;
private int progressStrokeWidth = 6;
//画圆所在的距形区域
RectF oval;
Paint paint;
public CircleProgressBar2(Context context, AttributeSet attrs) {
super(context, attrs);
// TODO 自动生成的构造函数存根
oval = new RectF();
paint = new Paint();
}
 
@SuppressLint("NewApi") @Override
protected void onDraw(Canvas canvas) {
// TODO 自动生成的方法存根
super.onDraw(canvas);
int width = this.getWidth();
int height = this.getHeight();

int x = (int) this.getX();
int y = (int) this.getY();

if(width!=height)
{
int min=Math.min(width, height);
width=min;
height=min;
}
 
paint.setAntiAlias(true); // 设置画笔为抗锯齿


paint.setColor(Color.TRANSPARENT); // 设置画笔颜色


canvas.drawColor(Color.TRANSPARENT);// 白色背景
paint.setStrokeWidth(progressStrokeWidth); //线宽
paint.setStyle(Style.STROKE);
 
//oval.left = progressStrokeWidth / 2; // 左上角x
//oval.top = progressStrokeWidth / 2; // 左上角y
//oval.right = width - progressStrokeWidth / 2; // 左下角x
//oval.bottom = height - progressStrokeWidth / 2; // 右下角y

oval.left = progressStrokeWidth / 2; // 左上角x
oval.top = progressStrokeWidth / 2; // 左上角y
oval.right = width - progressStrokeWidth / 2; // 左下角x
oval.bottom = height - progressStrokeWidth / 2; // 右下角y
 
canvas.drawArc(oval, 90, 360, false, paint); // 绘制白色圆圈，即进度条背景


switch (cm){
case 1:paint.setColor(Color.GREEN);break;
case 2:paint.setColor(Color.BLUE);break;
case 3:paint.setColor(Color.YELLOW);break;
case 4:paint.setColor(Color.RED);break;
default:paint.setColor(Color.YELLOW);break;	
}


canvas.drawArc(oval, 90, ((float) progress / maxProgress) * 360, false, paint); // 绘制进度圆弧，这里是蓝色
 
paint.setStrokeWidth(5);
String text = progress + "%";
int textHeight = height / 3;
paint.setTextSize(textHeight);
paint.setColor(Color.WHITE);
int textWidth = (int) paint.measureText(text, 0, text.length()+0);
paint.setStyle(Style.FILL);
canvas.drawText(text, width / 2 - textWidth / 2-2, height / 2 +textHeight/2-2, paint);
 
}
 
 
 
public int getMaxProgress() {
return maxProgress;
}
 
public void setMaxProgress(int maxProgress) {
this.maxProgress = maxProgress;
}
 
public void setProgress(int progress) {
this.progress = progress;
this.invalidate();
}
 
/**
* 非ＵＩ线程调用
*/
public void setProgressNotInUiThread(int progress,int cm) {
this.progress = progress;
this.cm = cm;
this.postInvalidate();
}
}