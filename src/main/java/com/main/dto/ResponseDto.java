package com.main.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@ToString
@AllArgsConstructor
@Getter
public class ResponseDto {
    String data;
    String code;

    public static ResponseDto OK(String data){
        return new ResponseDto(data, HttpStatus.OK.value()+"");
    }

    public static ResponseDto ERROR(String data){
        return new ResponseDto(data, HttpStatus.INTERNAL_SERVER_ERROR.value()+"");
    }
}
