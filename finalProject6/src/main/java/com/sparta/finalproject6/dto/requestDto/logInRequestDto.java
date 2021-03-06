package com.sparta.finalproject6.dto.requestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class logInRequestDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}

