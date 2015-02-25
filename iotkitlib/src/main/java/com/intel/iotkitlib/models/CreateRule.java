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
package com.intel.iotkitlib.models;

import java.util.LinkedList;
import java.util.List;


public class CreateRule {
    private String name;
    private String description;
    private String priority;
    private String ruleType;
    private String status;
    private String resetType;
    private List<CreateRuleActions> ruleActionsList;
    // population
    private List<String> populationIds;
    private String populationAttributes;
    //conditions
    private String operatorName; // operator
    private List<CreateRuleConditionValues> ruleConditionValuesList;

    public void setRuleName(String name) {
        this.name = name;
    }

    public void setRuleDescription(String description) {
        this.description = description;
    }

    public void setRulePriority(String priority) {
        this.priority = priority;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public void setRuleStatus(String status) {
        this.status = status;
    }

    public void setRuleResetType(String resetType) {
        this.resetType = resetType;
    }

    public void setRulePopulationAttributes(String attributes) {
        this.populationAttributes = attributes;
    }

    public void setRuleOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public void setRuleActions(CreateRuleActions createRuleActionsObj) {
        if (this.ruleActionsList == null) {
            this.ruleActionsList = new LinkedList<CreateRuleActions>();
        }
        this.ruleActionsList.add(createRuleActionsObj);
    }

    public void addRulePopulationId(String populationId) {
        if (this.populationIds == null) {
            this.populationIds = new LinkedList<String>();
        }
        this.populationIds.add(populationId);
    }

    public void addRuleConditionValues(CreateRuleConditionValues createRuleConditionValuesObj) {
        if (this.ruleConditionValuesList == null) {
            this.ruleConditionValuesList = new LinkedList<CreateRuleConditionValues>();
        }
        this.ruleConditionValuesList.add(createRuleConditionValuesObj);
    }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public String getPriority() { return priority; }

    public String getRuleType() { return ruleType; }

    public String getStatus() { return status; }

    public String getResetType() { return resetType; }

    public String getPopulationAttributes() { return populationAttributes; }

    public String getOperatorName() { return operatorName; }

    public List<CreateRuleActions> getRuleActionsList() { return ruleActionsList; }

    public List<String> getPopulationIds() { return populationIds; }

    public List<CreateRuleConditionValues> getRuleConditionValuesList() { return ruleConditionValuesList; }
}
