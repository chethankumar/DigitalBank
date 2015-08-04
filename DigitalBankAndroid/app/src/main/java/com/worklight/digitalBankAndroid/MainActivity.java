/*
 *
    COPYRIGHT LICENSE: This information contains sample code provided in source code form. You may copy, modify, and distribute
    these sample programs in any form without payment to IBM® for the purposes of developing, using, marketing or distributing
    application programs conforming to the application programming interface for the operating platform for which the sample code is written.
    Notwithstanding anything to the contrary, IBM PROVIDES THE SAMPLE SOURCE CODE ON AN "AS IS" BASIS AND IBM DISCLAIMS ALL WARRANTIES,
    EXPRESS OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, ANY IMPLIED WARRANTIES OR CONDITIONS OF MERCHANTABILITY, SATISFACTORY QUALITY,
    FITNESS FOR A PARTICULAR PURPOSE, TITLE, AND ANY WARRANTY OR CONDITION OF NON-INFRINGEMENT. IBM SHALL NOT BE LIABLE FOR ANY DIRECT,
    INDIRECT, INCIDENTAL, SPECIAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE USE OR OPERATION OF THE SAMPLE SOURCE CODE.
    IBM HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS OR MODIFICATIONS TO THE SAMPLE SOURCE CODE.

 */
package com.worklight.digitalBankAndroid;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.worklight.jsonstore.util.JSONStoreLogger;
import com.worklight.wlclient.api.WLClient;
import com.worklight.wlclient.api.WLFailResponse;
import com.worklight.wlclient.api.WLProcedureInvocationData;
import com.worklight.wlclient.api.WLRequestOptions;
import com.worklight.wlclient.api.WLResponse;
import com.worklight.wlclient.api.WLResponseListener;
import com.worklight.wlclient.challengehandler.WearableChallengeHandler;

public class MainActivity extends Activity {

	private static TextView mainText = null;
	private TextView textView2,textView3;
	WLClient client;
	private Button invokeBtn, logoutBtn;
	private static MainActivity otherThis;
	private AndroidChallengeHandler challengeHandler;
	private WearableChallengeHandler wc;
	private String realm = "CustomAuthenticatorRealm";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mainText = (TextView) findViewById(R.id.result);
		textView2=(TextView)findViewById(R.id.textView2);
		textView3=(TextView)findViewById(R.id.textView3);
		textView2.setTypeface(Utils.getBoldTypeface(this));
		textView3.setTypeface(Utils.getLightTypeface(this));
		otherThis = this;

		client = WLClient.createInstance(this);
		challengeHandler = new AndroidChallengeHandler(this, realm,this);
		client.registerChallengeHandler(challengeHandler);
		connectToServer();


		invokeBtn = (Button) findViewById(R.id.invoke);
		invokeBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				WLProcedureInvocationData invocationData = new WLProcedureInvocationData("DigitalBankAdapter", "getBalance");
				WLRequestOptions options = new WLRequestOptions();
				options.setTimeout(30000);
				client.invokeProcedure(invocationData, new MyResponseListener(), options);
			}

		});

		logoutBtn = (Button) findViewById(R.id.logout);
		logoutBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				client.logout(realm, new MyRequestListener());
				connectToServer();
			}
		});

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		boolean back = data.getBooleanExtra(LoginCustomLoginModule.Back, true);
		String username = data.getStringExtra(LoginCustomLoginModule.UserNameExtra);
		String password = data.getStringExtra(LoginCustomLoginModule.PasswordExtra);
		challengeHandler.submitLogin(resultCode, username, password, back);
	}

	public static void setMainText(final String txt){
		Runnable run = new Runnable() {			
			public void run() {
				mainText.setText(txt);				
			}
		};
		otherThis.runOnUiThread(run);
	}

	private void displayToast(final String msg){
		MainActivity.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void updateUI(){
		MainActivity.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				invokeBtn.setVisibility(View.VISIBLE);
				logoutBtn.setVisibility(View.VISIBLE);
			}
		});
	}

	private void connectToServer(){
		client.connect(new WLResponseListener() {
			@Override
			public void onSuccess(WLResponse wlResponse) {
				Log.d("MainActivity","Successfully connected to MFP server");
				updateUI();
			}

			@Override
			public void onFailure(WLFailResponse wlFailResponse) {
				Log.d("MainActivity", "Error while connecting to MFP server");
				displayToast("Error while connecting to MFP server");
			}
		});
	}
}
