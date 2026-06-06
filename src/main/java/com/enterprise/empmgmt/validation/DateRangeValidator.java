package com.enterprise.empmgmt.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

import java.time.LocalDate;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {

    private String startField;
    private String endField;

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
        this.startField = constraintAnnotation.start();
        this.endField = constraintAnnotation.end();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        var wrapper = new BeanWrapperImpl(value);
        Object start = wrapper.getPropertyValue(startField);
        Object end = wrapper.getPropertyValue(endField);
        if (!(start instanceof LocalDate startDate) || !(end instanceof LocalDate endDate)) {
            return true;
        }
        return !endDate.isBefore(startDate);
    }
}
