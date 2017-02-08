package com.aca.binding;

/**
 * Created by Marsel on 10/5/2016.
 */
public class SpinnerItem {
    String key;
    String value;
    String description;

    public SpinnerItem(String key, String value) {
        this.key = key;
        this.value = value;
    }
    public SpinnerItem(String key, String value, String description) {
        this.key = key;
        this.value = value;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return value;
    }


}
