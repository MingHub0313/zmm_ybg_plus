package com.ybg.base.util;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import com.ybg.base.util.webexception.ResultException;
import java.util.Set;

/**
 * hibernate-validator校验工具类 主要校验bean
 *
 * 参考文档：http://docs.jboss.org/hibernate/validator/5.4/reference/en-US/html_single/
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-15 10:50
 */
public class ValidatorUtils {
    
    private static Validator validator;
    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
    
    /**
     * 校验对象
     * 
     * @param object
     *            待校验对象
     * @param groups
     *            待校验的组
     * @throws ResultException
     *             校验不通过，则报RRException异常
     */
    public static void validateEntity(Object object,Class<?>... groups){
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            ConstraintViolation<Object> constraint = (ConstraintViolation<Object>) constraintViolations.iterator().next();
            throw new ResultException(constraint.getMessage());
        }
    }
}
