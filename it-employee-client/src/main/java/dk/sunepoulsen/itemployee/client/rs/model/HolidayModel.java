package dk.sunepoulsen.itemployee.client.rs.model;

import dk.sunepoulsen.tes.rest.models.BaseModel;
import dk.sunepoulsen.tes.validation.model.OnCrudCreate;
import dk.sunepoulsen.tes.validation.model.OnCrudRead;
import dk.sunepoulsen.tes.validation.model.OnCrudUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;

@Data
@Schema(description = "Holiday model")
public class HolidayModel implements BaseModel {
    @Schema(description = "Unique id of a holiday")
    @Null(groups = { OnCrudCreate.class, OnCrudUpdate.class })
    @NotNull(groups = OnCrudRead.class)
    private Long id;

    @Schema(description = "A descriptive name of the holiday, that can be used in GUI's")
    @NotNull(groups = {OnCrudCreate.class, OnCrudRead.class})
    private String name;

    @Schema(description = "The date of the holiday")
    @NotNull(groups = {OnCrudCreate.class, OnCrudRead.class})
    private LocalDate date;
}
