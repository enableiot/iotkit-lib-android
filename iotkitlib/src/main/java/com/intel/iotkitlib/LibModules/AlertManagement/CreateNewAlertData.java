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
package com.intel.iotkitlib.LibModules.AlertManagement;

import java.util.LinkedList;
import java.util.List;


public class CreateNewAlertData {
    //data
    String accountId;
    Integer alertId;
    Integer ruleId;
    String deviceId;
    String alertStatus;
    Long timestamp;
    Long resetTimestamp;
    String resetType;
    Long lastUpdateDate;
    String ruleName;
    String rulePriority;
    String naturalLangAlert;
    Long ruleExecutionTimestamp;
    //CreateNewAlertDataConditionsList conditions;
    List<CreateNewAlertDataConditions> alertDataConditionsList;

    public CreateNewAlertData() {
        this.alertId = -1; // IDs are never expected to be -1
        this.ruleId = -1; // IDs are never expected to be -1
        this.timestamp = 0L;
        this.resetTimestamp = 0L;
        this.lastUpdateDate = 0L;
        this.ruleExecutionTimestamp = -1L; // timestamps are never -1
    }

    public void alertSetAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void alertSetAlertId(Integer alertId) {
        this.alertId = alertId;
    }

    public void alertSetRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public void alertSetDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void alertSetAlertStatus(String alertStatus) {
        this.alertStatus = alertStatus;
    }

    public void alertSetTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public void alertSetResetTimestamp(Long resetTimestamp) {
        this.resetTimestamp = resetTimestamp;
    }

    public void alertSetResetType(String resetType) {
        this.resetType = resetType;
    }

    public void alertSetLastUpdateDate(Long lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public void alertSetRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public void alertSetRulePriority(String rulePriority) {
        this.rulePriority = rulePriority;
    }

    public void alertSetNaturalLangAlert(String naturalLangAlert) {
        this.naturalLangAlert = naturalLangAlert;
    }

    public void alertSetRuleExecutionTimestamp(Long ruleExecutionTimestamp) {
        this.ruleExecutionTimestamp = ruleExecutionTimestamp;
    }

    public void alertAddNewAlertConditions(CreateNewAlertDataConditions conditionsObj) {
        if (this.alertDataConditionsList == null) {
            this.alertDataConditionsList = new LinkedList<CreateNewAlertDataConditions>();
        }
        this.alertDataConditionsList.add(conditionsObj);
    }


}
