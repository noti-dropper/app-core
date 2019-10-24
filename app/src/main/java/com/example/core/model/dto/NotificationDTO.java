package com.example.core.model.dto;

public class NotificationDTO {

    public int id;
    public String title;
    public String msg;
    public int weight;
    public boolean isRead;
    public String pkgName;

    public NotificationDTO(int id, String pkgName, String title, String msg, int weight, boolean isRead) {
        this.id = id;
        this.title = title;
        this.msg = msg;
        this.weight = weight;
        this.isRead = isRead;
        this.pkgName = pkgName;
    }

}
