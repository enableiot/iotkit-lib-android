/*
 * Copyright (c) 2014 Intel Corporation.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.intel.iotkit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.intel.iotkitlib.AuthorizationManagement;
import com.intel.iotkitlib.RequestStatusHandler;
import com.intel.iotkitlib.http.CloudResponse;
import com.intel.iotkitlib.utils.Utilities;

import java.lang.ref.WeakReference;


public class MyActivity extends Activity {
    private static final String TAG = "MyActivity";

    //method to show response
    public void setResponse(int responseCode, String response) {
        final TextView responseCodeView = (TextView) findViewById(R.id.response_code);
        final TextView responseContentView = (TextView) findViewById(R.id.response_data);
        Log.d(TAG, String.valueOf(responseCode));
        Log.d(TAG, response);
        Toast.makeText(MyActivity.this, "Response code :" + responseCode, Toast.LENGTH_LONG).show();
        responseCodeView.setText("Response code :" + responseCode);
        responseContentView.setText("Response code :" + response);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Button openCloud = (Button) findViewById(R.id.opencloud);
        openCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//on click of getAuthToken button, expected to see Token-value with response code as Toast

                //This call needed to create shared Prefs for the TestApp and also assigns passed context in utilities for further usage
                Utilities.createSharedPreferences(new WeakReference<Context>(MyActivity.this));

                /****written test code to call Authorization module Asynchronously & Synchronously****/

                //######Asynchronous Http call##########
                //Passing valid anonymous callback creates AsyncTask to handle HTTP requests
                AuthorizationManagement getTokenAsync = new AuthorizationManagement(new RequestStatusHandler() {
                    //anonymous call back
                    @Override
                    public void readResponse(CloudResponse response) {
                        setResponse(response.getCode(), response.getResponse());
                    }
                });
                CloudResponse cloudResponse = getTokenAsync.getNewAuthorizationToken("xxxx@gmail.com", "xxxx");
                setResponse(cloudResponse.getCode(), cloudResponse.getResponse());

                //######Synchronous Http call##########
                //Sync call doing on background thread, doing on UI thread is not valid(creates NetworkOnMainThreadException)
                final AuthorizationManagement getTokenSync = new AuthorizationManagement();//passing null handler creates Synchronous Http call
                //for getting token
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final CloudResponse cloudResponse = getTokenSync.getNewAuthorizationToken("xxxx@gmail.com", "xxxx");
                        runOnUiThread(new Runnable() {//updating cloud response to activity over UI thread
                            @Override
                            public void run() {
                                setResponse(cloudResponse.getCode(), cloudResponse.getResponse());
                            }
                        });
                    }
                }).start();
            }
        });
    }
}
