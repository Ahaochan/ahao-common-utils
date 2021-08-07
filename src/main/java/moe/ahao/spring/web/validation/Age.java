package moe.ahao.spring.web.validation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = {Age.AgeValidator.class})
@Documented
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Age {
    String message() default "年龄非法，不能超过{max}岁";
    int max() default 200;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};


    class AgeValidator implements ConstraintValidator<Age, Integer> {
        private Age age;
        private Integer max;

        @Override
        public void initialize(Age age) {
            this.age = age;
            this.max = age.max();
        }

        @Override
        public boolean isValid(Integer value, ConstraintValidatorContext context) {
            return value != null && value < max;
        }

    }
}
