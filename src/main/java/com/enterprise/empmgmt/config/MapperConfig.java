package com.enterprise.empmgmt.config;

import com.enterprise.empmgmt.mapper.EmployeeMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public EmployeeMapper employeeMapper() {
        return Mappers.getMapper(EmployeeMapper.class);
    }
}