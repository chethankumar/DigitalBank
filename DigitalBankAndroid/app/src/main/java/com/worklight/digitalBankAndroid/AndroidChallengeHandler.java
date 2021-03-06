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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.worklight.wlclient.api.WLFailResponse;
import com.worklight.wlclient.api.WLResponse;
import com.worklight.wlclient.api.challengehandler.ChallengeHandler;

import org.json.JSONException;

import java.util.HashMap;

public class AndroidChallengeHandler extends ChallengeHandler{
	private Activity parentActivity;
	private WLResponse cachedResponse;
	private Context mcontext;
	static final String TAG = "SymmetricAlgorithmAES";


	public AndroidChallengeHandler(Activity activity, String realm,Context c) {
		super(realm);
		parentActivity = activity;
		mcontext=c;
	}

	public AndroidChallengeHandler(String realm) {
		super(realm);
	}

	@Override
	public void onFailure(WLFailResponse response) {
		submitFailure(response);
	}

	@Override
	public void onSuccess(WLResponse response) {
		submitSuccess(response);
	}

	@Override
	public boolean isCustomResponse(WLResponse response) {
		if (response == null || response.getResponseJSON() == null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mcontext);
			String username=preferences.getString("Name", "");
			String password=preferences.getString("Pass","");
			if(username!="" && password!="")
			{
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("Name1",username);
				editor.putString("Pass1",password);
				editor.apply();
			}
			return false;

	}
	if(response.toString().indexOf("authStatus") > -1){
		return true;
		}
		else{
			return false;
		}		 
	}

	@Override
	public void handleChallenge(WLResponse response){
		try {
			if(response.getResponseJSON().getString("authStatus") == "complete"){

				submitSuccess(response);

			}
			else {
				cachedResponse = response;
				Intent login = new Intent(parentActivity, LoginCustomLoginModule.class);
				parentActivity.startActivityForResult(login, 1);	
			}		
			
		} catch (JSONException e) {
			e.printStackTrace();
		}			
	}
	
	
	public void submitLogin(int resultCode, String userName, String password, boolean back){
		if (resultCode != Activity.RESULT_OK || back) {
			submitFailure(cachedResponse);
		} else {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("username", userName);
			params.put("password", password);
			submitLoginForm("/my_custom_auth_request_url", params, null, 0, "post");
		}
	}
	



}
