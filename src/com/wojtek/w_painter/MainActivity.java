package com.wojtek.w_painter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		
		//fluent transition to new thread after 4s
		new Handler().postDelayed(new Thread(){
			@Override 
			public void run()
			{
				Intent startActivityIntent = new Intent(MainActivity.this, StartActivity.class);
				MainActivity.this.startActivity(startActivityIntent);
				MainActivity.this.finish();
				overridePendingTransition(R.layout.fadein, R.layout.fadeout);
			}
		}, 4000);
		
	}

	

	
}
