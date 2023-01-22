package com.example.userservice.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class RequestUser {
    @NotNull(message = "Not null")
    @Size(min = 2,message = "not be less than 2")
    private String name;
    @NotNull(message = "Not null")
    @Size(min = 2,message = "not be less than 2")
    private String email;
    @NotNull(message = "Not null")
    @Size(min = 8,message = "not be less than 8")
    private String password;
    private String userId;
    private Date createAt;

    private String encryptedPwd;

}
