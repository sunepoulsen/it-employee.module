package dk.sunepoulsen.itemployee.client.rs.utils

import javax.validation.*

class ValidatorUtils {

    static void validate(Object value, Class<?>... groups) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory()
        Validator validator = factory.getValidator()
        Set<ConstraintViolation> violations = validator.validate(value, groups)
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations)
        }
    }

    static ConstraintViolation findFirstViolation(ConstraintViolationException ex) {
        return ex.constraintViolations.find()
    }

}
