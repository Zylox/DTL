package com.deeper.than.android;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.deeper.than.DTL;

public class AndroidLauncher extends AndroidApplication {
	
	public static int androidFPSTarget = 60;
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new DTL(androidFPSTarget), config);
	}
}
