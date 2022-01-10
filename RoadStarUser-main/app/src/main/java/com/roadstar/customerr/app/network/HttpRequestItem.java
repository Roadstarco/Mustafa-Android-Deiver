package com.roadstar.customerr.app.network;

import com.roadstar.customerr.common.utils.ApiConstants;
import com.roadstar.customerr.common.utils.AppUtils;
import com.roadstar.customerr.common.utils.NetworkUtils;

import java.util.HashMap;
import java.util.Map;

public class    HttpRequestItem {

    /**
     * Name-value pair of http prams like name=john doe
     */
    private Map<String, Object> params = null;
    private Map<String, String> params1 = null;

    /**
     * Name-value pair of http header prams like authorization: basic xxxxxxx
     */
    private Map<String, String> headerParams = null;
    /**
     * URL of given request
     */
    private String httpRequestUrl;
    /**
     * URL endPoint of given request
     */
    private String httpRequestEndPoint = "";
    /**
     * HTTP request type either {@link NetworkUtils#HTTP_GET} or {@link NetworkUtils#HTTP_POST}
     */
    private String httpRequestType = NetworkUtils.HTTP_GET;
    /**
     *  Http request timeout value
     */
    private long httpRequestTimeout = 0;

    public HttpRequestItem(String endPoint, String urlParams) {
        this.httpRequestEndPoint = endPoint;
        this.httpRequestUrl = ApiConstants.getServerUrl(endPoint)+urlParams;

    }

    public HttpRequestItem(String httpRequestUrl) {
        if (!AppUtils.ifNotNullEmpty(httpRequestUrl))
            throw new NullPointerException("Http request url can not be null");
        this.httpRequestUrl = httpRequestUrl;
    }

    public HttpRequestItem(String httpRequestUrl, Map<String, Object> params, String httpRequestType){
        this(httpRequestUrl);
        this.params = params;
        this.httpRequestType = httpRequestType;
    }

    public String getHttpRequestType() {
        return httpRequestType;
    }

    public void setHttpRequestType(String httpRequestType) {
        this.httpRequestType = httpRequestType;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
    public void setParams1(Map<String, String> params) {
        this.params1 = params;
    }
    /**
     * Add single name-value pair for http param into {@link Map}
     *
     * @param name  name
     * @param value value
     */
    public void addParams(String name, String value) {
        if (params == null)
            params = new HashMap<>();
        params.put(name, value);
    }

    public String getHttpRequestUrl() {
        return httpRequestUrl;
    }

    public void setHttpRequestUrl(String httpRequestUrl) {
        this.httpRequestUrl = httpRequestUrl;
    }

    public String getHttpRequestEndPoint() {
        return httpRequestEndPoint;
    }

    public void setHttpRequestEndPoint(String httpRequestEndPoint) {
        this.httpRequestEndPoint = httpRequestEndPoint;
    }

    public void setHeaderParams(Map<String, String> headerParams) {
        this.headerParams = headerParams;
    }

    /**
     * name-value pair for http header
     *
     * @param name  name
     * @param value value
     */
    public void addHeaderParams(String name, String value) {
        if (headerParams == null)
            headerParams = new HashMap<>();
        headerParams.put(name, value);
    }

    public Map<String, String> getHeaderParams() {
        return headerParams;
    }

    public long getHttpRequestTimeout() {
        return httpRequestTimeout;
    }

    public void setHttpRequestTimeout(long httpRequestTimeout) {
        this.httpRequestTimeout = httpRequestTimeout;
    }
}
