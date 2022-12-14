package dk.sunepoulsen.itemployee.client.rs.model;

import dk.sunepoulsen.tes.rest.models.BaseModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Schema(description = "Holiday model")
public class HolidayModel implements BaseModel {
    @Schema(description = "Unique id of a holiday")
    private Long id;

    @Schema(description = "A descriptive name of the holiday, that can be used in GUI's")
    private String name;

    @Schema(description = "The date of the holiday")
    private LocalDate date;
}
