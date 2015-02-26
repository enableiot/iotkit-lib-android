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
package com.intel.iotkitlib;

import android.util.Log;

import com.intel.iotkitlib.http.CloudResponse;
import com.intel.iotkitlib.http.HttpDeleteTask;
import com.intel.iotkitlib.http.HttpGetTask;
import com.intel.iotkitlib.http.HttpPostTask;
import com.intel.iotkitlib.http.HttpTaskHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

/**
 * Module that handles invitations
 */
public class InvitationManagement extends ParentModule {
    private final static String TAG = "InvitationManagement";

    // Errors
    public final static String ERR_INVALID_EMAIL = "emailId cannot be null";
    public final static String ERR_INVALID_BODY = "Invalid invitation body";

    /**
     * Module that handles invitations. Invitation to the account can be created by the user with
     * administrator permission. After created, invitation notification will be send as an email
     * message to the specific email account. Invited user has to login into IoT dashboard and
     * the accept/decline received invitation.
     *
     * For more information, please refer to @link{https://github.com/enableiot/iotkit-api/wiki/Invitation-Management}
     *
     * @param requestStatusHandler The handler for asynchronously request to return data and status
     *                             from the cloud
     */
    public InvitationManagement(RequestStatusHandler requestStatusHandler) {
        super(requestStatusHandler);
    }

    /**
     * Get a list of invitations send to specific user.
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     */
    public CloudResponse getListOfInvitation() {
        //initiating get for invitation list
        HttpGetTask listInvitation = new HttpGetTask();
        listInvitation.setHeaders(basicHeaderList);
        String url = objIotKit.prepareUrl(objIotKit.getInvitationList, null);
        return super.invokeHttpExecuteOnURL(url, listInvitation);
    }

    /**
     * Get the details about invitations send to specific user.
     * @param emailId The email that identifies the user
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     */
    public CloudResponse getInvitationListSendToSpecificUser(String emailId) {
        if (emailId == null) {
            Log.d(TAG, ERR_INVALID_EMAIL);
            return new CloudResponse(false, ERR_INVALID_EMAIL);
        }
        //initiating get for invitation list send to specific user
        HttpGetTask listInvitationToSpecificUser = new HttpGetTask();
        listInvitationToSpecificUser.setHeaders(basicHeaderList);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("email", emailId);
        String url = objIotKit.prepareUrl(objIotKit.getInvitationListSendToSpecificUser, linkedHashMap);
        return super.invokeHttpExecuteOnURL(url, listInvitationToSpecificUser);
    }

    /**
     * Delete invitations to an account for a specific user
     * @param emailId identifier for the user
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     * @throws JSONException
     */
    public CloudResponse deleteInvitations(String emailId) {
        if (emailId == null) {
            Log.d(TAG, ERR_INVALID_EMAIL);
            return new CloudResponse(false, ERR_INVALID_EMAIL);
        }
        //initiating delete for invitation deletion
        HttpDeleteTask deleteTheInvitations = new HttpDeleteTask();
        deleteTheInvitations.setHeaders(basicHeaderList);
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        linkedHashMap.put("email", emailId);
        String url = objIotKit.prepareUrl(objIotKit.deleteInvitations, linkedHashMap);
        return super.invokeHttpExecuteOnURL(url, deleteTheInvitations);
    }

    /**
     * Create an invitation to send out to the user
     * @param emailId The email of the user to be invited
     * @return For async model, return CloudResponse which wraps true if the request of REST
     * call is valid; otherwise false. The actual result from
     * the REST call is return asynchronously as part {@link RequestStatusHandler#readResponse}.
     * For synch model, return CloudResponse which wraps HTTP return code and response.
     * @throws JSONException
     */
    public CloudResponse createInvitation(String emailId) throws JSONException {
        if (emailId == null) {
            Log.d(TAG, ERR_INVALID_EMAIL);
            return new CloudResponse(false, ERR_INVALID_EMAIL);
        }
        String body;
        if ((body = createBodyForInvitation(emailId)) == null) {
            return new CloudResponse(false, ERR_INVALID_BODY);
        }
        //initiating post for  invitation creation
        HttpPostTask createInvitation = new HttpPostTask();
        createInvitation.setHeaders(basicHeaderList);
        createInvitation.setRequestBody(body);
        String url = objIotKit.prepareUrl(objIotKit.createInvitation, null);
        return super.invokeHttpExecuteOnURL(url, createInvitation);
    }

    private String createBodyForInvitation(String emailId) throws JSONException {
        JSONObject invitationJson = new JSONObject();
        invitationJson.put("email", emailId);
        return invitationJson.toString();
    }
}
