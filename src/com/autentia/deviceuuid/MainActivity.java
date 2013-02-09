package com.autentia.deviceuuid;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private TextView[] serialList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		init();
	}

	private void init() {
		serialList = new TextView[6];
		
		serialList[0] = (TextView) findViewById(R.id.textView_device1_value);
		if (serialList[0]!=null) {
			serialList[0].setText( SerialInfo.getGSFId(this.getApplication()) );
		}

		serialList[1] = (TextView) findViewById(R.id.textView_device2_value);
		if (serialList[1]!=null) {
			serialList[1].setText( SerialInfo.getBuildSerialId() );
		}
		
		serialList[2] = (TextView) findViewById(R.id.textView_device3_value);
		if (serialList[2]!=null) {
			serialList[2].setText( SerialInfo.getAndroidId(this.getApplication()) );
		}

		serialList[3] = (TextView) findViewById(R.id.textView_device4_value);
		if (serialList[3]!=null) {
			serialList[3].setText( SerialInfo.getIMEI(this.getApplication()) );
		}		

		serialList[4] = (TextView) findViewById(R.id.textView_device5_value);
		if (serialList[4]!=null) {
			serialList[4].setText( SerialInfo.getDeviceId(this.getApplication()) );
		}
		
		serialList[5] = (TextView) findViewById(R.id.textView_device6_value);
		if (serialList[5]!=null) {
			serialList[5].setText( SerialInfo.getPid() );
		}		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		this.finish();
	}

}
