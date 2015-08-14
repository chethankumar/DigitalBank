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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.security.Key;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

    public class LoginCustomLoginModule extends Activity {
	private EditText userNameEditText, passwordEditText;
	private Button loginBtn, backBtn,Regwearable;
	private TextView login_textView2, login_textView3,usertext,passtext;
	private Intent result;
	private static final long CONNECTION_TIME_OUT_MS = 100;
	private static LoginCustomLoginModule MiscThis;
    public static final String Back = "back";
	public static final String UserNameExtra = "username";
	public static final String PasswordExtra = "password";
	public static final String key = "Bar12345Bar12345";
	public static final Key aesKey = new SecretKeySpec(key.getBytes(), "AES");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		MiscThis = this;
		userNameEditText = (EditText) findViewById(R.id.enterName);
		passwordEditText = (EditText) findViewById(R.id.enterPass);
		usertext=(TextView) findViewById(R.id.username);
		passtext=(TextView) findViewById(R.id.pass);
		loginBtn = (Button) findViewById(R.id.login);
		backBtn = (Button) findViewById(R.id.back);
		Regwearable = (Button) findViewById(R.id.wear);
		login_textView2 = (TextView)findViewById(R.id.login_textView2);
		login_textView3 = (TextView)findViewById(R.id.login_textView3);
		login_textView2.setTypeface(Utils.getBoldTypeface(this));
		login_textView3.setTypeface(Utils.getLightTypeface(this));
		result = new Intent();
		if(decryptdata("trusted")=="error"){
			Regwearable.setVisibility(View.VISIBLE);
			loginBtn.setVisibility(View.INVISIBLE);
			backBtn.setVisibility(View.INVISIBLE);
			userNameEditText.setVisibility(View.INVISIBLE);
			passwordEditText.setVisibility(View.INVISIBLE);
			usertext.setVisibility(View.INVISIBLE);
			passtext.setVisibility(View.INVISIBLE);
		}
		else
		{
		Regwearable.setVisibility(View.INVISIBLE);
		encryptdata("activity", "window");
			checkIfWearableConnected();

		}

		Regwearable.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checkIfWearableConnected();
			}
		});

		//ClickListener
		loginBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String userName = userNameEditText.getText().toString();
				String password = passwordEditText.getText().toString();
				encryptdata(userName,"TempName");
				encryptdata(password, "TempPass");
				result.putExtra(UserNameExtra, userName);
				result.putExtra(PasswordExtra, password);
				result.putExtra(Back, false);
				setResult(RESULT_OK, result);
				finish();
			}
		});

        backBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onCreate(null);
			}
		});
	}

		public void encryptdata(String message,String tag)
		{
			try
			{
				Cipher cipher = Cipher.getInstance("AES");
				cipher.init(Cipher.ENCRYPT_MODE, aesKey);
				byte[] encrypted = cipher.doFinal(message.getBytes());
				System.err.println(new String(encrypted));
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MiscThis);
				SharedPreferences.Editor editor = preferences.edit();
                editor.putString(tag, Base64.encodeToString(encrypted, Base64.DEFAULT));
				editor.apply();
            }
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		public String decryptdata(String tag)
		{
			final String decrypted;
			try {
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MiscThis);
                byte[] encodedBytes =  Base64.decode(preferences.getString(tag, ""), Base64.DEFAULT);
				Cipher cipher = Cipher.getInstance("AES");
				cipher.init(Cipher.DECRYPT_MODE, aesKey);
				decrypted = new String(cipher.doFinal(encodedBytes));
				System.err.println("<------Decryption Success---->");
				return(decrypted);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return("error");
		}


	@Override
	public void onBackPressed() {
		result.putExtra(Back, true);
		setResult(RESULT_OK, result);
		finish();
	}

	public void checkIfWearableConnected() {
		retrieveDeviceNode(new Callback() {
			@Override
			public void success(String nodeId) {
				if (decryptdata("window").equals("activity")) {
					encryptdata("false", "window");
					final String userName = decryptdata("Name");
					final String password = decryptdata("Pass");
					final String wearableID = decryptdata("trusted");
					if (userName != "error" && userName != "error" && password != null && nodeId.equals(wearableID)) {
						Runnable run = new Runnable() {
							public void run() {
								userNameEditText.setText(userName);
								passwordEditText.setText(password);
							}
						};
						MiscThis.runOnUiThread(run);
						try {
							Thread.sleep(750);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						result.putExtra(UserNameExtra, userName);
						result.putExtra(PasswordExtra, password);
						result.putExtra(Back, false);
						setResult(RESULT_OK, result);
						finish();
					}
				} else {
					encryptdata(nodeId, "trusted");
					Runnable run = new Runnable() {
						public void run() {
							new AlertDialog.Builder(MiscThis)
									.setTitle("Digital Bank")
									.setMessage("Trusted Wearable Registered")
									.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which) {
											onCreate(null);

										}
									})
									.setIcon(android.R.drawable.ic_dialog_alert)
									.show();


						}
					};
					MiscThis.runOnUiThread(run);
				}

			}


			@Override
			public void failed(String message) {
				if (decryptdata("window").equals("activity")){
					encryptdata("false", "window");
				}
				else
				{
					Runnable run = new Runnable() {
						public void run() {
							new AlertDialog.Builder(MiscThis)
									.setTitle("Digital Bank")
									.setMessage("No Trusted Wearable Found")
									.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which) {
											dialog.cancel();
										}
									})
									.setIcon(android.R.drawable.ic_dialog_alert)
									.show();
						}
					};
					MiscThis.runOnUiThread(run);
				}

			}
		});

	}

	private interface Callback {
		public void success(final String nodeId);
		public void failed(final String message);
	}

	private void retrieveDeviceNode(final Callback callback) {
		final GoogleApiClient client = new GoogleApiClient.Builder(this).addApi(Wearable.API).build();
		new Thread(new Runnable() {
			@Override
			public void run() {
				client.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
				NodeApi.GetConnectedNodesResult result = Wearable.NodeApi.getConnectedNodes(client).await();
				List<Node> nodes = result.getNodes();
				if (nodes.size() > 0) {
					String nodeId = nodes.get(0).getId();
					callback.success(nodeId);
					System.err.println("wearable found");
				} else {
					callback.failed("No paired wearables found");
				}
				client.disconnect();
			}
		}).start();
	}

}
