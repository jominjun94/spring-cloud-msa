package com.example.userservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseUser implements Serializable {

    private String email;
    private String name;
    private String userId;
    private List<ResponseOrder> orders;
}
