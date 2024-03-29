package dk.sunepoulsen.itemployee.module.domain.holidays


import dk.sunepoulsen.itemployee.client.rs.model.HolidayModel
import dk.sunepoulsen.itemployee.module.domain.persistence.model.HolidayEntity
import spock.lang.Specification

import java.time.LocalDate

class HolidayTransformationsSpec extends Specification {

    private HolidayTransformations sut

    void setup() {
        this.sut = new HolidayTransformations()
    }

    void "Transform model to entity"() {
        given:
            HolidayModel model = new HolidayModel(
                id: 45L,
                name: 'name',
                date: LocalDate.now()
            )

        expect:
            sut.toEntity(model) == new HolidayEntity(
                id: model.id,
                name: model.name,
                date: model.date
            )
    }

    void "Transform entity to model"() {
        given:
            HolidayEntity entity = new HolidayEntity(
                id: 45L,
                name: 'name',
                date: LocalDate.now()
            )

        expect:
            sut.toModel(entity) == new HolidayModel(
                id: entity.id,
                name: entity.name,
                date: entity.date
            )
    }
}
