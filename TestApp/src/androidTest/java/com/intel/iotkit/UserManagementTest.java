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

import com.intel.iotkitlib.RequestStatusHandler;
import com.intel.iotkitlib.UserManagement;
import com.intel.iotkitlib.http.CloudResponse;
import com.intel.iotkitlib.utils.Utilities;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.util.LinkedList;
import java.util.List;


public class UserManagementTest extends ApplicationTest {
    private boolean serverResponse = false;

    private void waitForServerResponse(Object object) {
        synchronized (object) {
            while (!serverResponse) {
            }
            serverResponse = false;
        }
    }

    public void testCreateNewUser() throws JSONException {
        UserManagement userManagement = new UserManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(CloudResponse response) {
                assertEquals(201, response.getCode());
                serverResponse = true;
            }
        });
        CloudResponse response = userManagement.createNewUser("xxxxx@gmail.com", "xxxx");
        assertEquals(true, response.getStatus());
        waitForServerResponse(userManagement);
    }

    public void testGetUserInfo() throws JSONException {
        UserManagement userManagement = new UserManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(CloudResponse response) {
                assertEquals(200, response.getCode());
                serverResponse = true;
            }
        });
        CloudResponse response = userManagement.getUserInfo(Utilities.sharedPreferences.getString("user_id", ""));
        assertEquals(true, response.getStatus());
        waitForServerResponse(userManagement);
    }

    public void testUpdateUserAttributes() throws JSONException {
        UserManagement userManagement = new UserManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(CloudResponse response) {
                assertEquals(200, response.getCode());
                serverResponse = true;
            }
        });
        //update attributes on user
        List<NameValuePair> attributes = new LinkedList<NameValuePair>();
        attributes.add(new BasicNameValuePair("phone", "123456789"));
        attributes.add(new BasicNameValuePair("new", "next_string_value"));
        attributes.add(new BasicNameValuePair("another_attribute", "another_value"));
        CloudResponse response = userManagement.updateUserAttributes(Utilities.sharedPreferences.getString("user_id", ""), attributes);
        assertEquals(true, response.getStatus());
        waitForServerResponse(userManagement);
    }

    public void testRequestChangePassword() throws JSONException {
        UserManagement userManagement = new UserManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(CloudResponse response) {
                assertEquals(200, response.getCode());
                serverResponse = true;
            }
        });
        CloudResponse response = userManagement.requestChangePassword(Utilities.sharedPreferences.getString("email", ""));
        assertEquals(true, response.getStatus());
        waitForServerResponse(userManagement);
    }

    public void testUpdateForgotPassword() throws JSONException {
        UserManagement userManagement = new UserManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(CloudResponse response) {
                assertEquals(200, response.getCode());
                serverResponse = true;
            }
        });
        CloudResponse response = userManagement.updateForgotPassword("mFxklxSmmfWCyei8", "Password2529");
        assertEquals(true, response.getStatus());
        waitForServerResponse(userManagement);
    }

    public void testChangePassword() throws JSONException {
        UserManagement userManagement = new UserManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(CloudResponse response) {
                assertEquals(200, response.getCode());
                serverResponse = true;
            }
        });
        CloudResponse response = userManagement.changePassword(Utilities.sharedPreferences.getString("email", "")
                , "Password2529", "Password25292");
        assertEquals(true, response.getStatus());
        waitForServerResponse(userManagement);
    }

    public void testDeleteAUser() throws JSONException {
        UserManagement userManagement = new UserManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(CloudResponse response) {
                assertEquals(204, response.getCode());
                serverResponse = true;
            }
        });
        CloudResponse response = userManagement.deleteAUser(Utilities.sharedPreferences.getString("user_id", ""));
        assertEquals(true, response.getStatus());
        waitForServerResponse(userManagement);
    }

}
