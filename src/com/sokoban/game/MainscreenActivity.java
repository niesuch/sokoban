package com.sokoban.game;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.content.Intent;
import android.net.Uri;

public class MainscreenActivity extends Activity {
	Button button_newgame;
	Button button_rules;
	Button button_exit;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mainscreen);

		button_newgame = (Button) findViewById(R.id.button_newgame);
		button_newgame.setOnClickListener(newgame);
		button_rules = (Button) findViewById(R.id.button_rules);
		button_rules.setOnClickListener(rules);
		button_exit = (Button) findViewById(R.id.button_exit);
		button_exit.setOnClickListener(exit);
	}

	View.OnClickListener newgame = new View.OnClickListener() {
		public void onClick(View v) {
			Intent play = new Intent(MainscreenActivity.this, GamescreenActivity.class);
	        startActivity(play);
		}
	};
	
	View.OnClickListener rules = new View.OnClickListener() {
		public void onClick(View v) {
			Intent redirect = new Intent(Intent.ACTION_VIEW, Uri.parse("http://en.wikipedia.org/wiki/Sokoban"));
			startActivity(redirect);
		}
	};
	
	View.OnClickListener exit = new View.OnClickListener() {
		public void onClick(View v) {
			finish();
            System.exit(0);
		}
	};
}