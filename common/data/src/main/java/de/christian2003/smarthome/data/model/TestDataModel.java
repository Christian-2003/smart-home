package de.christian2003.smarthome.data.model;

import android.content.Context;


public class TestDataModel {

    private final String value;


    public TestDataModel(Context context) {
        value = "TestValue";
    }


    public String getValue() {
        return value;
    }

}
