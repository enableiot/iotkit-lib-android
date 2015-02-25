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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;
import java.util.List;


public class CreateRuleConditionValues {
    private List<NameValuePair> components;
    private String ruleConditionType;
    private List<String> values;
    private String ruleConditionValuesOperatorName;

    public void addConditionComponent(String keyName, String keyValue) {
        if (this.components == null) {
            this.components = new LinkedList<NameValuePair>();
        }
        this.components.add(new BasicNameValuePair(keyName, keyValue));
    }

    public void setConditionType(String ruleConditionType) {
        this.ruleConditionType = ruleConditionType;
    }

    public void addConditionValues(String values) {
        if (this.values == null) {
            this.values = new LinkedList<String>();
        }
        this.values.add(values);
    }

    public void setConditionOperator(String ruleConditionValuesOperatorName) {
        this.ruleConditionValuesOperatorName = ruleConditionValuesOperatorName;
    }

    public List<NameValuePair> getComponents() { return components; }

    public String getRuleConditionType() { return ruleConditionType; }

    public List<String> getValues() { return values; }

    public String getRuleConditionValuesOperatorName() { return ruleConditionValuesOperatorName; }
}
