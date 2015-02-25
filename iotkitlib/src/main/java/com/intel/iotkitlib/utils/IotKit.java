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
package com.intel.iotkitlib.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;


public class IotKit {
    public static final String HTTP_PROTOCOL = "http://";
    public static final String HTTPS_PROTOCOL = "https://";
    public static final String HEADER_CONTENT_TYPE_NAME = "Content-Type";
    public static final String HEADER_CONTENT_TYPE_JSON = "application/json";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_AUTHORIZATION_BEARER = "Bearer";
    private static final String TAG = "IotKit";
    private static IotKit singletonInstance = null;
    public String base_Url;
    public boolean is_Secure;
    public String protocol;
    //Authorization Url strings
    public String newAuthToken;
    public String authTokenInfo;
    //Account Management Url Strings
    public String createAnAccount;
    public String getAccountInfo;
    public String getActivationCode;
    public String renewActivationCode;
    public String updateAccount;
    public String deleteAccount;
    public String addUserToAccount;
    //Device Management Url Strings
    public String listDevices;
    public String listAllAttributes;
    public String listAllTags;
    public String createDevice;
    public String deleteDevice;
    public String activateDevice;
    public String updateDevice;
    public String addComponent;
    public String deleteComponent;
    public String getOneDeviceInfo;
    public String getMyDeviceInfo;
    //Component type catalog url strings
    public String listAllComponentTypesCatalog;
    public String listAllComponentTypesCatalogDetailed;
    public String componentTypeCatalogDetails;
    public String createCustomComponent;
    public String updateComponent;
    //data related url strings
    public String submitData;
    public String retrieveData;
    //user Management Url Strings
    public String createUser;
    public String getUserInfo;
    public String updateUserAttributes;
    public String acceptTermsAndConditions;
    public String deleteUser;
    public String requestChangePassword;
    public String changePassword;
    //Invitation management URL strings
    public String getInvitationList;
    public String getInvitationListSendToSpecificUser;
    public String createInvitation;
    public String deleteInvitations;
    //Rule Management URL strings
    public String createRule;
    public String updateRule;
    public String deleteDraftRule;
    public String createRuleAsDraft;
    public String updateStatusOfRule;
    public String getListOfRules;
    public String getInfoOfRule;
    //Alert Management Url Strings
    public String createNewAlert;
    public String getListOfAlerts;
    public String getAlertInformation;
    public String resetAlert;
    public String updateAlertStatus;
    public String addCommentToAlert;
    //Advanced data enquiry
    public String advancedEnquiryOfData;
    //Aggregated Report Interface
    public String aggregatedReportInterface;
    //public String authorizaionKey;
    //public String accountName;
    //public List<String> accountIds;
    public int port;

    /* private Constructor prevents any other
    * class from instantiating.
    */
    private IotKit() {
    }

    public static IotKit getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new IotKit();
            try {
                singletonInstance.parseJsonObjectFromFile("res/raw/config.json");
            } catch (IOException ie) {
                ie.printStackTrace();
            } catch (JSONException je) {
                je.printStackTrace();
            }
        }
        return singletonInstance;
    }

    private JSONObject createJsonObjectFromFile(String filePath) throws IOException, JSONException {
        //loading json files from res/raw folder using class loader
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(filePath);
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        StringBuilder responseStrBuilder = new StringBuilder();
        String inputString;
        while ((inputString = streamReader.readLine()) != null)
            responseStrBuilder.append(inputString);
        JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());
        //Toast.makeText(jsonContext, "Response code :" + jsonObject, Toast.LENGTH_LONG).show();
        return jsonObject;
    }

    private void parseJsonObjectFromFile(String filePath) throws IOException, JSONException {
        this.parseConfigurationJsonObject(this.createJsonObjectFromFile(filePath));
    }

    private void parseConfigurationJsonObject(JSONObject jsonObject) throws JSONException {
        //base url parsing
        base_Url = jsonObject.getString("host");
        port = jsonObject.getInt("port");
        if (jsonObject.getBoolean("isSecure")) {
            is_Secure = true;
        }
        JSONObject apiJson = jsonObject.getJSONObject("apipath");
        //extracting authorization Json containing related URL's
        JSONObject authorizationApiJson = apiJson.getJSONObject("authorization");
        newAuthToken = authorizationApiJson.getString("new_auth_token");
        authTokenInfo = authorizationApiJson.getString("auth_token_info");
        //extracting account_management Json containing related URL's
        JSONObject accountManagementApiJson = apiJson.getJSONObject("account_management");
        createAnAccount = accountManagementApiJson.getString("create_an_account");
        getAccountInfo = accountManagementApiJson.getString("get_account_information");
        getActivationCode = accountManagementApiJson.getString("get_account_activation_code");
        renewActivationCode = accountManagementApiJson.getString("renew_account_activation");
        updateAccount = accountManagementApiJson.getString("update_an_account_name");
        deleteAccount = accountManagementApiJson.getString("delete_an_account_name");
        addUserToAccount = accountManagementApiJson.getString("add_an_user_to_account");
        //extracting device management Json containing related URL's
        JSONObject deviceManagementApiJson = apiJson.getJSONObject("device_management");
        listDevices = deviceManagementApiJson.getString("list_all_devices");
        createDevice = deviceManagementApiJson.getString("create_a_device");
        getOneDeviceInfo = deviceManagementApiJson.getString("get_device_info");
        getMyDeviceInfo = deviceManagementApiJson.getString("get_my_device_info");
        updateDevice = deviceManagementApiJson.getString("update_a_device");
        deleteDevice = deviceManagementApiJson.getString("delete_a_device");
        activateDevice = deviceManagementApiJson.getString("activate_a_device");
        addComponent = deviceManagementApiJson.getString("add_a_component");
        deleteComponent = deviceManagementApiJson.getString("delete_a_component");
        listAllTags = deviceManagementApiJson.getString("list_all_tags");
        listAllAttributes = deviceManagementApiJson.getString("list_all_attributes");
        //extracting component types catalog Json containing related URL's
        JSONObject componentCatalogApiJson = apiJson.getJSONObject("cmpcatalog");
        listAllComponentTypesCatalog = componentCatalogApiJson.getString("list_components");
        listAllComponentTypesCatalogDetailed = componentCatalogApiJson.getString("list_components_detailed");
        componentTypeCatalogDetails = componentCatalogApiJson.getString("get_component_details");
        createCustomComponent = componentCatalogApiJson.getString("create_an_cmp_catalog");
        updateComponent = componentCatalogApiJson.getString("update_an_cmp_catalog");
        //extracting  data Json containing related URL's
        JSONObject dataApiJson = apiJson.getJSONObject("data");
        submitData = dataApiJson.getString("submit_data");
        retrieveData = dataApiJson.getString("retrieve_data");
        //extracting  userManagement Json containing related URL's
        JSONObject userManagementApiJson = apiJson.getJSONObject("user_management");
        createUser = userManagementApiJson.getString("create_a_user");
        getUserInfo = userManagementApiJson.getString("get_user_information");
        updateUserAttributes = userManagementApiJson.getString("update_user_attributes");
        acceptTermsAndConditions = userManagementApiJson.getString("accept_terms_and_conditions");
        deleteUser = userManagementApiJson.getString("delete_a_user");
        requestChangePassword = userManagementApiJson.getString("request_change_password");
        changePassword = userManagementApiJson.getString("change_password");
        //extracting  Invitation Management Json containing related URL's
        JSONObject invitationManagementApiJson = apiJson.getJSONObject("invitation_management");
        getInvitationList = invitationManagementApiJson.getString("get_list_of_invitation");
        getInvitationListSendToSpecificUser = invitationManagementApiJson.getString("get_invitation_list_send_to_specific_user");
        createInvitation = invitationManagementApiJson.getString("create_invitation");
        deleteInvitations = invitationManagementApiJson.getString("delete_invitations");
        //extracting  Rule Management Json containing related URL's
        JSONObject ruleManagementApiJson = apiJson.getJSONObject("rule_management");
        createRule = ruleManagementApiJson.getString("create_a_rule");
        updateRule = ruleManagementApiJson.getString("update_a_rule");
        getListOfRules = ruleManagementApiJson.getString("get_list_of_rules");
        getInfoOfRule = ruleManagementApiJson.getString("get_one_rule_info");
        createRuleAsDraft = ruleManagementApiJson.getString("create_a_rule_as_draft");
        updateStatusOfRule = ruleManagementApiJson.getString("update_status_of_a_rule");
        deleteDraftRule = ruleManagementApiJson.getString("delete_a_draft_rule");
        //extracting  Alert Management Json containing related URL's
        JSONObject alertManagementApiJson = apiJson.getJSONObject("alert_management");
        createNewAlert = alertManagementApiJson.getString("create_new_alert");
        getListOfAlerts = alertManagementApiJson.getString("get_list_of_alerts");
        getAlertInformation = alertManagementApiJson.getString("get_alert_information");
        resetAlert = alertManagementApiJson.getString("reset_alert");
        updateAlertStatus = alertManagementApiJson.getString("update_alert_status");
        addCommentToAlert = alertManagementApiJson.getString("add_comment_to_alert");
        //advanced data enquiry
        advancedEnquiryOfData = apiJson.getString("advanced_data_inquiry");
        //aggregated report interface
        aggregatedReportInterface = apiJson.getString("aggregated_report_interface");
        this.createBaseUrl();
    }

    private void createBaseUrl() {
        base_Url = ((is_Secure ? HTTPS_PROTOCOL : HTTP_PROTOCOL) + base_Url + ":" + String.valueOf(port));
        //Toast.makeText(jsonContext, "Response code :" + base_Url, Toast.LENGTH_LONG).show();
    }

    public String prepareUrl(String urlToAppend, LinkedHashMap urlSlugNameValues) {
        String urlPrepared = null;
        if (urlToAppend != null) {
            if (Utilities.sharedPreferences == null) {
                Log.w(TAG, "cannot find shared preferences object,not able to create URL");
                return null;
            }
            //Handling Slug param replacement in URL
            if (urlToAppend.contains("data_account_id")) {
                if (urlSlugNameValues != null && urlSlugNameValues.containsKey("account_id")) {
                    urlToAppend = urlToAppend.replace("data_account_id", urlSlugNameValues.get("account_id").toString());
                } else {
                    urlToAppend = urlToAppend.replace("data_account_id", Utilities.sharedPreferences.getString("account_id", ""));
                }
                //updating device id url-value
                if (urlToAppend.contains("other_device_id")) {
                    urlToAppend = urlToAppend.replace("other_device_id", urlSlugNameValues.get("other_device_id").toString());
                }
                if (urlToAppend.contains("device_id")) {
                    urlToAppend = urlToAppend.replace("device_id", Utilities.sharedPreferences.getString("deviceId", ""));
                }
                if (urlToAppend.contains("cmp_catalog_id")) {
                    urlToAppend = urlToAppend.replace("cmp_catalog_id", urlSlugNameValues.get("cmp_catalog_id").toString());
                }
                if (urlToAppend.contains("cid")) {
                    String sensorId = null;
                    if ((sensorId = Utilities.getSensorId(urlSlugNameValues.get("cname").toString())) != null) {
                        urlToAppend = urlToAppend.replace("cid", sensorId);
                    } else {
                        Log.d(TAG, "No component found with this name-To Delete");
                    }
                    /*if (Utilities.sharedPreferences.getString("cname", "").
                            contentEquals(urlSlugNameValues.get("cname").toString())) {
                        urlToAppend = urlToAppend.replace("cid", Utilities.sharedPreferences.getString("cid", ""));
                    } */
                }
                if (urlToAppend.contains("email")) {
                    urlToAppend = urlToAppend.replace("email", urlSlugNameValues.get("email").toString());
                }
                if (urlToAppend.contains("rule_id")) {
                    urlToAppend = urlToAppend.replace("rule_id", urlSlugNameValues.get("rule_id").toString());
                }
                if (urlToAppend.contains("alert_id")) {
                    urlToAppend = urlToAppend.replace("alert_id", urlSlugNameValues.get("alert_id").toString());
                }
                if (urlToAppend.contains("invitee_user_id")) {
                    urlToAppend = urlToAppend.replace("invitee_user_id", urlSlugNameValues.get("invitee_user_id").toString());
                }

            } else if (urlToAppend.contains("device_id")) {
                urlToAppend = urlToAppend.replace("device_id", Utilities.sharedPreferences.getString("deviceId", ""));
            } else if (urlToAppend.contains("user_id")) {
                urlToAppend = urlToAppend.replace("user_id", urlSlugNameValues.get("user_id").toString());
            } else if (urlToAppend.contains("email")) {
                urlToAppend = urlToAppend.replace("email", urlSlugNameValues.get("email").toString());
            } else {
                Log.d(TAG, "URL with out slugs");
            }
        }
        urlToAppend = urlToAppend.replaceAll("\\{", "");
        urlToAppend = urlToAppend.replaceAll("\\}", "");
        //appending the module url to base url
        urlPrepared = base_Url + urlToAppend;
        return urlPrepared;
    }

}

