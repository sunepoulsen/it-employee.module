package dk.sunepoulsen.itemployee.module.domain.holidays;

import dk.sunepoulsen.tes.rest.models.PaginationModel;
import dk.sunepoulsen.tes.rest.models.ServiceErrorModel;
import dk.sunepoulsen.tes.rest.models.transformations.PaginationTransformations;
import dk.sunepoulsen.tes.springboot.rest.exceptions.ApiBadRequestException;
import dk.sunepoulsen.tes.springboot.rest.logic.exceptions.LogicException;
import dk.sunepoulsen.itemployee.client.rs.model.HolidayModel;
import dk.sunepoulsen.tes.validation.model.OnCrudCreate;
import dk.sunepoulsen.tes.validation.model.OnCrudUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@CrossOrigin
@RestController
@Tag(name = "Holidays", description = "Management of holidays")
@Validated
public class HolidayController {

    private final HolidayLogic holidayLogic;

    @Autowired
    public HolidayController(HolidayLogic holidayLogic) {
        this.holidayLogic = holidayLogic;
    }

    @RequestMapping( value = "/holidays", method = RequestMethod.POST )
    @ResponseStatus( HttpStatus.CREATED )
    @Operation(summary = "Create a new holiday")
    @ApiResponses({
        @ApiResponse(responseCode = "201",
            description = "A new holiday has been created",
            content = { @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = HolidayModel.class)
            ) }
        ),
        @ApiResponse(responseCode = "400",
            description = "The holiday model is not valid",
            content = { @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ServiceErrorModel.class)
            ) }
        )
    })
    @Validated(OnCrudCreate.class)
    public HolidayModel create(
        @RequestBody
        @Parameter(description = "The holiday model to create")
        @Valid
        HolidayModel model)
    {
        try {
            return holidayLogic.create(model);
        }
        catch( LogicException ex ) {
            throw ex.mapApiException();
        }
    }

    @RequestMapping( value = "/holidays", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @Operation(summary = "Find all holidays")
    @ApiResponses({
        @ApiResponse(responseCode = "200",
            description = "Returns all found holidays in a paginating result",
            content = { @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = HolidayModel.class)
            ) }
        ),
        @ApiResponse(responseCode = "400",
            description = "The query parameters are not valid",
            content = { @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ServiceErrorModel.class)
            ) }
        )
    })
    public PaginationModel<HolidayModel> findAll(@Valid Pageable pageable) {
        try {
            return PaginationTransformations.toPaginationResult(holidayLogic.findAll(pageable));
        } catch (PropertyReferenceException ex) {
            throw new ApiBadRequestException("sort", "Unknown sort property", ex);
        } catch (LogicException ex) {
            throw ex.mapApiException();
        }
    }

    @RequestMapping( value = "/holidays/{id}", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @Operation(summary = "Returns a holiday")
    @ApiResponses({
        @ApiResponse(responseCode = "200",
            description = "Returns one holiday by its id",
            content = { @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = HolidayModel.class)
            ) }
        ),
        @ApiResponse(responseCode = "400",
            description = "The {id} parameters is not a number",
            content = { @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ServiceErrorModel.class)
            ) }
        ),
        @ApiResponse(responseCode = "404",
            description = "Unable to find a holiday with the given id",
            content = { @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ServiceErrorModel.class)
            ) }
        )
    })
    public HolidayModel get(@PathVariable("id") @Valid Long id) {
        try {
            return holidayLogic.get(id);
        } catch (IllegalArgumentException ex) {
            throw new ApiBadRequestException("id", ex.getMessage(), ex);
        } catch (LogicException ex) {
            throw ex.mapApiException();
        }
    }

    @RequestMapping( value = "/holidays/{id}", method = RequestMethod.PATCH )
    @ResponseStatus( HttpStatus.OK )
    @Operation(summary = "Update an existing holiday")
    @ApiResponses({
        @ApiResponse(responseCode = "200",
            description = "The holiday has been updated",
            content = { @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = HolidayModel.class)
            ) }
        ),
        @ApiResponse(responseCode = "400",
            description = "The holiday model is not valid",
            content = { @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ServiceErrorModel.class)
            ) }
        ),
        @ApiResponse(responseCode = "404",
            description = "A holiday with the given id does not exist",
            content = { @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ServiceErrorModel.class)
            ) }
        )
    })
    @Validated(OnCrudUpdate.class)
    public HolidayModel patch(
        @PathVariable("id")
            Long id,
        @RequestBody
        @Parameter(description = "The holiday model to create")
        @Valid
        HolidayModel model)
    {
        try {
            return holidayLogic.patch(id, model);
        } catch (LogicException ex) {
            throw ex.mapApiException();
        }
    }

    @RequestMapping( value = "/holidays/{id}", method = RequestMethod.DELETE )
    @ResponseStatus( HttpStatus.NO_CONTENT )
    @Operation(summary = "Delete a holiday")
    @ApiResponses({
        @ApiResponse(responseCode = "200",
            description = "Delete one holiday by its id"
        ),
        @ApiResponse(responseCode = "400",
            description = "The {id} parameters is not a number",
            content = { @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ServiceErrorModel.class)
            ) }
        ),
        @ApiResponse(responseCode = "404",
            description = "Unable to find a holiday with the given id",
            content = { @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ServiceErrorModel.class)
            ) }
        )
    })
    public void delete(@PathVariable("id") @Valid Long id) {
        try {
            holidayLogic.delete(id);
        } catch (IllegalArgumentException ex) {
            throw new ApiBadRequestException("id", ex.getMessage(), ex);
        } catch (LogicException ex) {
            throw ex.mapApiException();
        }
    }
}
