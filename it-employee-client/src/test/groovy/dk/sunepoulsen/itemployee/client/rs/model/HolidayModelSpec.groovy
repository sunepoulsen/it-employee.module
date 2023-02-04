package dk.sunepoulsen.itemployee.client.rs.model

import dk.sunepoulsen.itemployee.client.rs.utils.ValidatorUtils
import dk.sunepoulsen.tes.validation.model.OnCrudCreate
import dk.sunepoulsen.tes.validation.model.OnCrudRead
import dk.sunepoulsen.tes.validation.model.OnCrudUpdate
import spock.lang.Specification
import spock.lang.Unroll

import javax.validation.ConstraintViolationException
import java.time.LocalDate

class HolidayModelSpec extends Specification {

    @Unroll
    void "Validate with group #_testcase is valid"() {
        given:
            HolidayModel model = new HolidayModel(
                id: _id,
                name: 'name',
                date: LocalDate.now()
            )

        expect:
            if( _groups == null ) {
                ValidatorUtils.validate(model)
            }
            else {
                ValidatorUtils.validate(model, _groups)
            }


        where:
            _testcase      | _id  | _groups
            'OnCrudCreate' | null | OnCrudCreate
            'OnCrudRead'   | 1L   | OnCrudRead
            'OnCrudUpdate' | null | OnCrudUpdate
    }

    @Unroll
    void "Validate with group OnCrudCreate is invalid: #_testcase"() {
        given:
            HolidayModel model = new HolidayModel(
                id: _id,
                name: _name,
                date: _date
            )

        when:
            ValidatorUtils.validate(model, OnCrudCreate)

        then:
            ConstraintViolationException exception = thrown(ConstraintViolationException)
            ValidatorUtils.findFirstViolation(exception).propertyPath.toString() == _errorParam
            ValidatorUtils.findFirstViolation(exception).message == _errorMessage

        where:
            _testcase        | _id  | _name  | _date           | _errorCode | _errorParam | _errorMessage
            'id is not null' | 10   | 'name' | LocalDate.now() | null       | 'id'        | 'must be null'
            'name is null'   | null | null   | LocalDate.now() | null       | 'name'      | 'must not be null'
            'date is null'   | null | 'name' | null            | null       | 'date'      | 'must not be null'
    }

    @Unroll
    void "Validate with group OnCrudRead is invalid: #_testcase"() {
        given:
            HolidayModel model = new HolidayModel(
                id: _id,
                name: _name,
                date: _date
            )

        when:
            ValidatorUtils.validate(model, OnCrudRead)

        then:
            ConstraintViolationException exception = thrown(ConstraintViolationException)
            ValidatorUtils.findFirstViolation(exception).propertyPath.toString() == _errorParam
            ValidatorUtils.findFirstViolation(exception).message == _errorMessage

        where:
            _testcase      | _id  | _name  | _date           | _errorCode | _errorParam | _errorMessage
            'id is null'   | null | 'name' | LocalDate.now() | null       | 'id'        | 'must not be null'
            'name is null' | 10L  | null   | LocalDate.now() | null       | 'name'      | 'must not be null'
            'date is null' | 10L  | 'name' | null            | null       | 'date'      | 'must not be null'
    }

    @Unroll
    void "Validate with group OnCrudUpdate is valid: #_testcase"() {
        given:
            HolidayModel model = new HolidayModel(
                id: _id,
                name: _name,
                date: _date
            )

        expect:
            ValidatorUtils.validate(model, OnCrudUpdate)

        where:
            _testcase             | _id  | _name  | _date
            'All values are null' | null | null   | null
            'name is null'        | null | null   | LocalDate.now()
            'date is null'        | null | 'name' | null
    }

    @Unroll
    void "Validate with group OnCrudUpdate is invalid: #_testcase"() {
        given:
            HolidayModel model = new HolidayModel(
                id: _id,
                name: _name,
                date: _date
            )

        when:
            ValidatorUtils.validate(model, OnCrudUpdate)

        then:
            ConstraintViolationException exception = thrown(ConstraintViolationException)
            ValidatorUtils.findFirstViolation(exception).propertyPath.toString() == _errorParam
            ValidatorUtils.findFirstViolation(exception).message == _errorMessage

        where:
            _testcase        | _id  | _name  | _date           | _errorCode | _errorParam | _errorMessage
            'id is not null' | 10L  | 'name' | LocalDate.now() | null       | 'id'        | 'must be null'
    }

}
