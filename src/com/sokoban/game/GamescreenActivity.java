package com.sokoban.game;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

public class GamescreenActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View sokobanView = new SokobanView(this);
		setContentView(sokobanView);
		sokobanView.setBackgroundColor(Color.WHITE);
	}
}