package dk.sunepoulsen.itemployee.module.domain.holidays

import dk.sunepoulsen.itemployee.module.domain.persistence.model.HolidayEntity
import dk.sunepoulsen.tes.rest.models.PaginationModel
import dk.sunepoulsen.tes.rest.models.ServiceErrorModel
import dk.sunepoulsen.tes.springboot.rest.exceptions.ApiBadRequestException
import dk.sunepoulsen.tes.springboot.rest.exceptions.ApiConflictException
import dk.sunepoulsen.tes.springboot.rest.exceptions.ApiNotFoundException
import dk.sunepoulsen.tes.springboot.rest.logic.exceptions.DuplicateResourceException
import dk.sunepoulsen.tes.springboot.rest.logic.exceptions.ResourceNotFoundException
import dk.sunepoulsen.itemployee.client.rs.model.HolidayModel
import dk.sunepoulsen.tes.springboot.rest.logic.exceptions.ResourceViolationException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.mapping.PropertyReferenceException
import org.springframework.data.util.ClassTypeInformation
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate

class HolidayControllerSpec extends Specification {

    HolidayLogic holidayLogic
    HolidayController sut

    void setup() {
        holidayLogic = Mock(HolidayLogic)
        sut = new HolidayController(holidayLogic)
    }

    @Unroll
    void "Create holiday returns OK: #_testcase"() {
        given:
            HolidayModel model = new HolidayModel(
                id: null,
                name: 'name',
                date: _date
            )
            HolidayModel expected = new HolidayModel(
                id: 1L,
                name: model.name,
                date: model.date
            )

        when:
            HolidayModel result = sut.create(model)

        then:
            result == expected
            1 * holidayLogic.create(model) >> expected

        where:
            _testcase          | _date
            'date is not null' | LocalDate.now()
    }

    void "Create holiday rejected because of logic exception"() {
        given:
            HolidayModel model = new HolidayModel(
                id: null,
                name: 'name',
                date: LocalDate.now()
            )

        when:
            sut.create(model)

        then:
            ApiConflictException ex = thrown(ApiConflictException)
            ex.serviceError.param == 'param'
            ex.serviceError.message == 'message'

            1 * holidayLogic.create(model) >> {
                throw new DuplicateResourceException('param', 'message')
            }
    }

    void "Find all holidays returns OK"() {
        given:
            Pageable pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.ASC, 'wrong'))

            Page<HolidayModel> pageFound = new PageImpl([new HolidayModel(id: 7)], pageable, 1)

        when:
            PaginationModel<HolidayModel> pageResult = sut.findAll(pageable)

        then:
            pageResult.results.size() == 1
            pageResult.results.get(0) == new HolidayModel(id: 7)
            pageResult.metadata.totalPages == 1

            0 * holidayLogic.create(_)
            1 * holidayLogic.findAll(pageable) >> pageFound
    }

    void "Find all holidays with unknown sorting"() {
        given:
            Pageable pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.ASC, 'wrong'))

        when:
            sut.findAll(pageable)

        then:
            ApiBadRequestException exception = thrown(ApiBadRequestException)
            exception.getServiceError() == new ServiceErrorModel(
                param: 'sort',
                message: 'Unknown sort property'
            )

            0 * holidayLogic.create(_)
            1 * holidayLogic.findAll(pageable) >> {
                throw new PropertyReferenceException('wrong', ClassTypeInformation.from(HolidayEntity.class), [])
            }
    }

    void "Find all holidays with logical error"() {
        given:
            Pageable pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.ASC, 'wrong'))

        when:
            sut.findAll(pageable)

        then:
            ApiBadRequestException exception = thrown(ApiBadRequestException)
            exception.getServiceError() == new ServiceErrorModel(
                param: 'param',
                message: 'message'
            )

            0 * holidayLogic.create(_)
            1 * holidayLogic.findAll(pageable) >> {
                throw new ResourceViolationException('param', 'message')
            }
    }

    void "Get holiday returns OK"() {
        given:
            HolidayModel model = new HolidayModel(
                id: 5L,
                name: 'name',
                date: LocalDate.now()
            )

        when:
            HolidayModel result = sut.get(model.id)

        then:
            result == model
            1 * holidayLogic.get(model.id) >> model
    }

    void "Get holiday with illegal argument"() {
        when:
            sut.get(null)

        then:
            ApiBadRequestException exception = thrown(ApiBadRequestException)
            exception.getServiceError() == new ServiceErrorModel(
                param: 'id',
                message: 'message'
            )

            1 * holidayLogic.get(null) >> {
                throw new IllegalArgumentException('message')
            }
    }

    void "Get holiday with logical error"() {
        when:
            sut.get(5L)

        then:
            ApiNotFoundException exception = thrown(ApiNotFoundException)
            exception.getServiceError() == new ServiceErrorModel(
                param: 'id',
                message: 'message'
            )

            1 * holidayLogic.get(5L) >> {
                throw new ResourceNotFoundException('id', 'message')
            }
    }


    void "Patch holiday returns OK"() {
        given:
            HolidayModel model = new HolidayModel(
                name: 'new-name'
            )
            HolidayModel expected = new HolidayModel(
                id: 5L,
                name: 'new-name',
                date: LocalDate.now()
            )

        when:
            HolidayModel result = sut.patch(5L, model)

        then:
            result == expected
            1 * holidayLogic.patch(5L, model) >> expected
    }

    void "Patch holiday with illegal argument"() {
        given:
            HolidayModel model = new HolidayModel(
                name: 'new-name'
            )

        when:
            sut.patch(5L, model)

        then:
            ApiBadRequestException exception = thrown(ApiBadRequestException)
            exception.getServiceError() == new ServiceErrorModel(
                param: 'id',
                message: 'message'
            )

            1 * holidayLogic.patch(_, _) >> {
                throw new IllegalArgumentException('message')
            }
    }

    void "Patch holiday with logical error"() {
        given:
            HolidayModel model = new HolidayModel(
                name: 'new-name'
            )

        when:
            sut.patch(5L, model)

        then:
            ApiNotFoundException exception = thrown(ApiNotFoundException)
            exception.getServiceError() == new ServiceErrorModel(
                param: 'id',
                message: 'message'
            )

            1 * holidayLogic.patch(_, _) >> {
                throw new ResourceNotFoundException('id', 'message')
            }
    }

    void "Delete holiday returns OK"() {
        when:
            sut.delete(5L)

        then:
            1 * holidayLogic.delete(5L)
    }

    void "Delete holiday returns illegal argument"() {
        when:
            sut.delete(null)

        then:
            ApiBadRequestException exception = thrown(ApiBadRequestException)
            exception.getServiceError() == new ServiceErrorModel(
                param: 'id',
                message: 'message'
            )

            1 * holidayLogic.delete(null) >> {
                throw new IllegalArgumentException('message')
            }
    }

    void "Delete holiday with logical error"() {
        when:
            sut.delete(5L)

        then:
            ApiNotFoundException exception = thrown(ApiNotFoundException)
            exception.getServiceError() == new ServiceErrorModel(
                param: 'id',
                message: 'message'
            )

            1 * holidayLogic.delete(5L) >> {
                throw new ResourceNotFoundException('id', 'message')
            }
    }

}
