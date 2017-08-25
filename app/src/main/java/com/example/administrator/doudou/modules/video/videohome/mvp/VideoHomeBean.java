package com.example.administrator.doudou.modules.video.videohome.mvp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/5/12.
 */

public class VideoHomeBean<T> {
    @SerializedName("msg")
    private String msg;

    @SerializedName("ret")
    private T data;

    @SerializedName("code")
    private String code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
