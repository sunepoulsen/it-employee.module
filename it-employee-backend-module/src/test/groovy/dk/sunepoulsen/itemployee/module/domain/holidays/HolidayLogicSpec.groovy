package dk.sunepoulsen.itemployee.module.domain.holidays


import dk.sunepoulsen.itemployee.client.rs.model.HolidayModel
import dk.sunepoulsen.itemployee.module.domain.persistence.HolidayPersistence
import dk.sunepoulsen.itemployee.module.domain.persistence.model.HolidayEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import spock.lang.Specification

import java.time.LocalDate

class HolidayLogicSpec extends Specification {

    private HolidayTransformations holidayTransformations
    private HolidayPersistence holidayPersistence
    private HolidayLogic sut

    void setup() {
        this.holidayTransformations = Mock(HolidayTransformations)
        this.holidayPersistence = Mock(HolidayPersistence)
        this.sut = new HolidayLogic(holidayTransformations, holidayPersistence)
    }

    void "Create new holiday"() {
        given:
            HolidayModel model = new HolidayModel(
                name: 'name',
                date: LocalDate.now()
            )
            HolidayModel expected = new HolidayModel(
                id: 50L,
                name: 'name',
                date: LocalDate.now()
            )

        when:
            HolidayModel result = sut.create(model)

        then:
            result == expected
            1 * holidayTransformations.toEntity(model) >> new HolidayEntity(id:1L)
            1 * holidayPersistence.create(new HolidayEntity(id:1L)) >> new HolidayEntity(id:2L)
            1 * holidayTransformations.toModel(new HolidayEntity(id:2L)) >> expected
    }

    void "Find all agreements"() {
        given:
            List<HolidayModel> agreements = (1..5).collect() {
                new HolidayModel(
                    id: it,
                    name: "name-${it}",
                    date: LocalDate.now().plusDays(it)
                )
            }

            List<HolidayEntity> entities = (1..5).collect() {
                new HolidayEntity(
                    id: it,
                    name: "name-${it}",
                    date: LocalDate.now().plusDays(it)
                )
            }

            Pageable pageable = PageRequest.of(0, 20)

        when:
            Page<HolidayModel> result = sut.findAll(pageable)

        then:
            result == new PageImpl<>(agreements, pageable, 5)
            1 * holidayPersistence.findAll(pageable) >> new PageImpl<>(entities, pageable, 5)
            1 * holidayTransformations.toModel(entities[0]) >> agreements[0]
            1 * holidayTransformations.toModel(entities[1]) >> agreements[1]
            1 * holidayTransformations.toModel(entities[2]) >> agreements[2]
            1 * holidayTransformations.toModel(entities[3]) >> agreements[3]
            1 * holidayTransformations.toModel(entities[4]) >> agreements[4]
    }

    void "Get an agreement"() {
        given:
            HolidayModel expected = new HolidayModel(
                id: 50L,
                name: 'name',
                date: LocalDate.now()
            )

        when:
            HolidayModel result = sut.get(50L)

        then:
            result == expected
            1 * holidayPersistence.get(50L) >> new HolidayEntity(id:2L)
            1 * holidayTransformations.toModel(new HolidayEntity(id:2L)) >> expected
    }

    void "Patch an agreement"() {
        given:
            HolidayModel patchModel = new HolidayModel(name: 'Christmas')

            HolidayModel expected = new HolidayModel(
                id: 50L,
                name: 'name',
                date: LocalDate.now()
            )

        when:
            HolidayModel result = sut.patch(50L, patchModel)

        then:
            result == expected
            1 * holidayTransformations.toEntity(patchModel) >> new HolidayEntity(id:1L)
            1 * holidayPersistence.patch(50L, new HolidayEntity(id:1L)) >> new HolidayEntity(id:2L)
            1 * holidayTransformations.toModel(new HolidayEntity(id:2L)) >> expected
    }

    void "Delete an agreement"() {
        when:
            sut.delete(50L)

        then:
            1 * holidayPersistence.delete(50L)
    }
}
