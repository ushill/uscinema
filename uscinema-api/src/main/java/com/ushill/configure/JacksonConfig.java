package com.ushill.configure;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;

import java.text.SimpleDateFormat;

/**
 * @author ：五羊
 * @description：TODO
 * @date ：2020/4/2 下午5:31
 */
public class JacksonConfig {

    @Bean
    public ObjectMapper getObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-mm-dd HH:mm:ss"));
        return objectMapper;
    }
}
