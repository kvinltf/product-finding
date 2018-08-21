package com.example.productfinding.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseObject<T> {
    private static final String STATUS_SUCCESS = "Success";
    private static final String STATUS_FAIL = "Fail";
    private String status;
    private String message;
    private String error_message;
    private T query_result;

    public ResponseObject() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public T getQuery_result() {
        return query_result;
    }

    public void setQuery_result(T query_result) {
        this.query_result = query_result;
    }

    public boolean isStatusSuccess(){
        return this.status.equalsIgnoreCase(this.STATUS_SUCCESS);
    }

    @Override
    public String toString() {
        return "ResponseObject{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", error_message='" + error_message + '\'' +
                ", query_result=" + query_result +
                '}';
    }
}
