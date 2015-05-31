package com.sokoban.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class SokobanView extends View {
	private int boxSize;
	private int buttonSize;
	private int startX;
	private int startY;
	private int restartX;
	private int restartY;
	private float oldEventX = 0;
	private float oldEventY = 0;
	private float oldEventTime = 0;
	private float distanceX = 0;
	private float distanceY = 0;
	private float speedX = 0;
	private float speedY = 0;
	private Bitmap box;
	private Bitmap boxgoal;
	private Bitmap goal;
	private Bitmap wall;
	private Bitmap restart;
	private Bitmap pusher;
	private Bitmap floor;
	private Bitmap pushergoal;
	private Level level;
	private RectF rect;

	public SokobanView(Context context) {
		super(context);
		
		// LEGEND
		// 	# - Wall
		//	$ - Box
		//	. - Goal
		//	* - Box on goal
		//	@ - Pusher
		// 	+ - Pusher on goal
		//	S - Floor
		
		level = new Level(
				"#################  " 
				+ "#..  #          ###" 
				+ "#..  # $     $    #" 
				+ "#..  # #########  #" 
				+ "#      @ #######  #"
				+ "#    # #     $   ##" 
				+ "###### ##         #" 
				+ "  # $  $          #" 
				+ "  #    #      $   #" 
				+ "  #    #          #"
				+ "  #################", 19, 11);

		// TEST LEVEL
		/*level = new Level(
				"#################  " 
				+ "#..  #          ###"
				+ "#..  #            #" 
				+ "#..  # #########  #"
				+ "# $    @ #######  #" 
				+ "#    # #         ##"
				+ "###### ##         #" 
				+ "  #               #"
				+ "  #    #          #" 
				+ "  #    #          #"
				+ "  #################", 19, 11);*/

		box = BitmapFactory.decodeResource(getResources(), R.drawable.box);
		boxgoal = BitmapFactory.decodeResource(getResources(),
				R.drawable.boxgoal);
		goal = BitmapFactory.decodeResource(getResources(), R.drawable.goal);
		wall = BitmapFactory.decodeResource(getResources(), R.drawable.wall);
		restart = BitmapFactory.decodeResource(getResources(),
				R.drawable.restart);
		pusher = BitmapFactory
				.decodeResource(getResources(), R.drawable.pusher);
		floor = BitmapFactory.decodeResource(getResources(), R.drawable.floor);
		pushergoal = BitmapFactory.decodeResource(getResources(),
				R.drawable.pushergoal);
		rect = new RectF();
	}

	protected void onDraw(Canvas canvas) {
		for (int i = 0; i < level.numberOfRows; i++) {
			for (int j = 0; j < level.numberOfCols; j++) {
				char type = level.readPosition(i, j);

				Bitmap character = null;
				if (type == 's')
					character = floor;
				else if (type == '#')
					character = wall;
				else if (type == '$')
					character = box;
				else if (type == '*')
					character = boxgoal;
				else if (type == '.')
					character = goal;
				else if (type == '@')
					character = pusher;
				else if (type == '+')
					character = pushergoal;

				if (character != null) {
					rect.set(startX + j * boxSize, startY + i * boxSize, startX
							+ (j + 1) * boxSize, startY + (i + 1) * boxSize);
					canvas.drawBitmap(character, null, rect, null);
				}
			}
		}

		rect.set(restartX, restartY, restartX + buttonSize, restartY
				+ buttonSize);
		canvas.drawBitmap(restart, null, rect, null);
		invalidate();
	}

	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_UP:
			distanceX = 0;
			distanceY = 0;
			speedX = 0;
			speedY = 0;
			break;
		case MotionEvent.ACTION_MOVE:
			if (!level.is_won()) {
				int SWIPE_DISTANCE_THRESHOLD = 55;
				int SWIPE_VELOCITY_THRESHOLD = 150;

				distanceX += (event.getX() - oldEventX);
				distanceY += (event.getY() - oldEventY);
				speedX = (event.getX() - oldEventX)
						/ (event.getEventTime() - oldEventTime) * 1000;
				speedY = (event.getY() - oldEventY)
						/ (event.getEventTime() - oldEventTime) * 1000;

				char direction = ' ';

				if (Math.abs(distanceX) > Math.abs(distanceY)
						&& Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD
						&& Math.abs(speedX) > SWIPE_VELOCITY_THRESHOLD) {
					if (distanceX > 0)
						direction = 'r';
					else
						direction = 'l';
					distanceX = 0;
					distanceY = 0;
				} else if (Math.abs(distanceY) > Math.abs(distanceX)
						&& Math.abs(distanceY) > SWIPE_DISTANCE_THRESHOLD
						&& Math.abs(speedY) > SWIPE_VELOCITY_THRESHOLD) {
					if (distanceY > 0)
						direction = 'd';
					else
						direction = 'u';
					distanceX = 0;
					distanceY = 0;
				}

				if (direction != ' ')
					level.move(direction);
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
				builder.setTitle("You Win!")
				    .setMessage("Restart Game?")
				    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				    	public void onClick(DialogInterface dialog, int which) {
				    		level.restart();
				    	}
				    })
				    .setNegativeButton("No", new DialogInterface.OnClickListener() {
				    	public void onClick(DialogInterface dialog, int which) {
				            System.exit(0);
				    	}
				    });

				AlertDialog dialog = builder.create();
				if(dialog.isShowing()){
				    dialog.dismiss();
				}
				else {
					dialog.show();
				}
			}
			break;
		}

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (event.getX() > restartX && event.getX() < restartX + buttonSize
					&& event.getY() > restartY
					&& event.getY() < restartY + buttonSize)
				level.restart();
		}

		oldEventX = event.getX();
		oldEventY = event.getY();
		oldEventTime = event.getEventTime();

		return true;
	}

	public void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
		float x = 0.9f * (float) width / (float) level.numberOfCols;
		float y = 0.9f * (float) height / (float) level.numberOfRows;
		boxSize = (int) Math.min(x, y);
		startX = (width - (int) (boxSize * (float) level.numberOfCols)) / 2;
		startY = (height - (int) (boxSize * (float) level.numberOfRows)) / 2;
		buttonSize = height / 8;
		restartX = width - buttonSize - 30;
		restartY = 30;
	}
}