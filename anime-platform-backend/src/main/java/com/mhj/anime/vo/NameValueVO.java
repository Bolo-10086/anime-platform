package com.mhj.anime.vo;

public class NameValueVO {

    private String name;
    private Integer value;

    public NameValueVO() {
    }

    public NameValueVO(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
