package org.heyu.ig.core;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author heyu
 */
@Getter
@Setter
@ToString
public class UipRequestRecord {

    private String code;

    private Integer type;

    private String requestId;

    private boolean success;

    private String url;

    private String method;

    private String param;

    private String body;

    private String response;

    private String ip;

    private String userAgent;

    private String requestTime;

    private String responseTime;

    private Long milli;

}
