package com.example.springbootrabbitmq.dto;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User implements Serializable {

  private String userName;
  private Integer userAge;
  private String userSchool;
}
