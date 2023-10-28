package com.demo.demo.Dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
@Data
public class StandardResponseDTO {
    private boolean success;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message = null;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data = null;
    private int cout;

    public StandardResponseDTO FullSuccess(Object data){
        this.success = true;
        this.data = data;
        this.cout = 1;
        return this;
    }

    public StandardResponseDTO FailSuccess(String message){
        this.success = false;
        this.message = message;
        this.cout = 1;
        return this;
    }
}
