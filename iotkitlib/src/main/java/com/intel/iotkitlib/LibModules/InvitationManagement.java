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
package com.intel.iotkitlib.LibModules;

import android.util.Log;

import com.intel.iotkitlib.LibHttp.HttpDeleteTask;
import com.intel.iotkitlib.LibHttp.HttpGetTask;
import com.intel.iotkitlib.LibHttp.HttpPostTask;
import com.intel.iotkitlib.LibHttp.HttpTaskHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;


public class InvitationManagement extends ParentModule {
    private final static String TAG = "InvitationManagement";

    public InvitationManagement(RequestStatusHandler requestStatusHandler) {
        super(requestStatusHandler);
    }

    public boolean getListOfInvitation() {
        //initiating get for invitation list
        HttpGetTask listInvitation = new HttpGetTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        listInvitation.setHeaders(basicHeaderList);
        String url = objIotKit.prepareUrl(objIotKit.getInvitationList, null);
        return super.invokeHttpExecuteOnURL(url, listInvitation, "list invitation");
    }

    public boolean getInvitationListSendToSpecificUser(String emailId) {
        if (emailId == null) {
            Log.d(TAG, "emailId cannot be null");
            return false;
        }
        //initiating get for invitation list send to specific user
        HttpGetTask listInvitationToSpecificUser = new HttpGetTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        listInvitationToSpecificUser.setHeaders(basicHeaderList);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("email", emailId);
        String url = objIotKit.prepareUrl(objIotKit.getInvitationListSendToSpecificUser, linkedHashMap);
        return super.invokeHttpExecuteOnURL(url, listInvitationToSpecificUser, "list invitation to specific user");
    }

    public boolean deleteInvitations(String emailId) {
        if (emailId == null) {
            Log.d(TAG, "emailId cannot be null");
            return false;
        }
        //initiating delete for invitation deletion
        HttpDeleteTask deleteTheInvitations = new HttpDeleteTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        deleteTheInvitations.setHeaders(basicHeaderList);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("email", emailId);
        String url = objIotKit.prepareUrl(objIotKit.deleteInvitations, linkedHashMap);
        return super.invokeHttpExecuteOnURL(url, deleteTheInvitations, "delete invitations");
    }

    public boolean createInvitation(String emailId) throws JSONException {
        if (emailId == null) {
            Log.d(TAG, "emailId cannot be null");
            return false;
        }
        String body;
        if ((body = createBodyForInvitation(emailId)) == null) {
            return false;
        }
        //initiating post for  invitation creation
        HttpPostTask createInvitation = new HttpPostTask(new HttpTaskHandler() {
            @Override
            public void taskResponse(int responseCode, String response) {
                Log.d(TAG, String.valueOf(responseCode));
                Log.d(TAG, response);
                statusHandler.readResponse(responseCode, response);
            }
        });
        createInvitation.setHeaders(basicHeaderList);
        createInvitation.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.createInvitation, null);
        return super.invokeHttpExecuteOnURL(url, createInvitation, "create invitation");
    }

    private String createBodyForInvitation(String emailId) throws JSONException {
        JSONObject invitationJson = new JSONObject();
        invitationJson.put("email", emailId);
        return invitationJson.toString();
    }
}
