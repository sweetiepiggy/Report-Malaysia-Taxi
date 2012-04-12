package com.sweetiepiggy.reportmalaysiataxi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TextViewActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.textview_layout);
		Bundle b = getIntent().getExtras();
		String text = (b == null) ? "" : b.getString("text");
		((TextView) findViewById(R.id.text)).setText(text);
	}

}
