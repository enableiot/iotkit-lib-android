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

import com.intel.iotkitlib.LibModules.RequestStatusHandler;
import com.intel.iotkitlib.LibModules.UserManagement;

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
            public void readResponse(int responseCode, String response) {
                assertEquals(201, responseCode);
                serverResponse = true;
            }
        });
        assertEquals(true, userManagement.createNewUser("intel.aricent.iot3@gmail.com", "Password2529"));
        waitForServerResponse(userManagement);
    }

    public void testGetUserInfo() throws JSONException {
        UserManagement userManagement = new UserManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(200, responseCode);
                serverResponse = true;
            }
        });
        assertEquals(true, userManagement.getUserInfo("5465ef05cd7df9c1113aafa3"));
        waitForServerResponse(userManagement);
    }

    public void testAcceptTermsAndConditions() throws JSONException {
        UserManagement userManagement = new UserManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(200, responseCode);
                serverResponse = true;
            }
        });
        assertEquals(true, userManagement.acceptTermsAndConditions("5465ef05cd7df9c1113aafa3", true));
        waitForServerResponse(userManagement);
    }

    public void testUpdateUserAttributes() throws JSONException {
        UserManagement userManagement = new UserManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(200, responseCode);
                serverResponse = true;
            }
        });
        //update attributes on user
        List<NameValuePair> attributes = new LinkedList<NameValuePair>();
        attributes.add(new BasicNameValuePair("phone", "123456789"));
        attributes.add(new BasicNameValuePair("new", "next_string_value"));
        attributes.add(new BasicNameValuePair("another_attribute", "another_value"));
        assertEquals(true, userManagement.updateUserAttributes("5465ef05cd7df9c1113aafa3", attributes));
        waitForServerResponse(userManagement);
    }

    public void testRequestChangePassword() throws JSONException {
        UserManagement userManagement = new UserManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(200, responseCode);
                serverResponse = true;
            }
        });
        assertEquals(true, userManagement.requestChangePassword("intel.aricent.iot3@gmail.com"));
        waitForServerResponse(userManagement);
    }

    public void testUpdateForgotPassword() throws JSONException {
        UserManagement userManagement = new UserManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(200, responseCode);
                serverResponse = true;
            }
        });
        assertEquals(true, userManagement.updateForgotPassword("3jxo3jP3d1I1Toq7", "Password2529"));
        waitForServerResponse(userManagement);
    }

    public void testChangePassword() throws JSONException {
        UserManagement userManagement = new UserManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(200, responseCode);
                serverResponse = true;
            }
        });
        assertEquals(true, userManagement.changePassword("intel.aricent.iot3@gmail.com", "Password2529", "Password25292"));
        waitForServerResponse(userManagement);
    }

    public void testDeleteAUser() throws JSONException {
        UserManagement userManagement = new UserManagement(new RequestStatusHandler() {
            @Override
            public void readResponse(int responseCode, String response) {
                assertEquals(204, responseCode);
                serverResponse = true;
            }
        });
        assertEquals(true, userManagement.deleteAUser("54533fbbf7e1110732274b51"));
        waitForServerResponse(userManagement);
    }

}
