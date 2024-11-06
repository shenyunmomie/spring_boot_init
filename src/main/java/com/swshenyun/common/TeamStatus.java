package com.swshenyun.common;

/**
 * 队伍状态枚举类
 */
public enum TeamStatus {
    PUBLIC(0, "公开"),
    PRIVATE(1, "私有"),
    SECRET(2, "加密");

    private int value;

    private String text;

    public static TeamStatus getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }
        TeamStatus[] values = TeamStatus.values();
        for (TeamStatus teamStatus : values) {
            if (teamStatus.getValue() == value) {
                return teamStatus;
            }
        }
        return null;
    }

    TeamStatus(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
