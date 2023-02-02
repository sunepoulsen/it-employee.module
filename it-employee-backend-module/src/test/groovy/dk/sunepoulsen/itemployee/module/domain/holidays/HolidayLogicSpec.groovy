package dk.sunepoulsen.itemployee.module.domain.holidays


import dk.sunepoulsen.itemployee.client.rs.model.HolidayModel
import dk.sunepoulsen.itemployee.module.domain.persistence.HolidayPersistence
import dk.sunepoulsen.itemployee.module.domain.persistence.model.HolidayEntity
import spock.lang.Specification

import java.time.LocalDate

class HolidayLogicSpec extends Specification {

    private HolidayTransformations templateTransformations
    private HolidayPersistence templatePersistence
    private HolidayLogic sut

    void setup() {
        this.templateTransformations = Mock(HolidayTransformations)
        this.templatePersistence = Mock(HolidayPersistence)
        this.sut = new HolidayLogic(templateTransformations, templatePersistence)
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
            1 * templateTransformations.toEntity(model) >> new HolidayEntity(id:1L)
            1 * templatePersistence.create(new HolidayEntity(id:1L)) >> new HolidayEntity(id:2L)
            1 * templateTransformations.toModel(new HolidayEntity(id:2L)) >> expected
    }
}
