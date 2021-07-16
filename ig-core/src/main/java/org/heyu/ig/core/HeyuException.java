/*
 ***********************************************************************************
 * Copyright www.ebidding.net.cn 2017年09月27日 Authors: 闫广坤 <ygk@ebidding.com.cn>*
 ***********************************************************************************
 */
package org.heyu.ig.core;

/**
 * @author heyu
 */
public class HeyuException extends RuntimeException {

    private String code;


    public HeyuException(String message) {
        super(message);
    }

    public HeyuException(Exception e) {
        super(e);
    }

    public HeyuException(String message, Exception e) {
        super(message, e);
    }

    public HeyuException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


}
