package plazadecomidas.restaurants.adapters.driving.http.rest.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.request.AddEmployeeRequest;
import plazadecomidas.restaurants.adapters.driving.http.rest.exception.IdMismatchException;
import plazadecomidas.restaurants.adapters.driving.http.rest.mapper.IEmployeeRequestMapper;
import plazadecomidas.restaurants.adapters.driving.http.rest.util.ControllerConstants;
import plazadecomidas.restaurants.domain.primaryport.IEmployeeServicePort;
import plazadecomidas.restaurants.util.ITokenUtils;

@AllArgsConstructor
@RestController
@RequestMapping("/employees")
public class EmployeeControllerAdapter {

    private final IEmployeeServicePort employeeServicePort;
    private final IEmployeeRequestMapper employeeRequestMapper;
    private final ITokenUtils tokenUtils;

    @PostMapping("register")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Void> registerEmployee(@RequestHeader("Authorization") String token, @RequestBody AddEmployeeRequest addEmployeeRequest) {
        Long ownerId = getUserId(token);

        if (!ownerId.equals(addEmployeeRequest.idOwner())) {
            throw new IdMismatchException(ControllerConstants.OWNER_ID_MISMATCH_MESSAGE);
        }

        employeeServicePort.save(
                employeeRequestMapper.addEmployeeRequestToEmployee(
                        addEmployeeRequest), ownerId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private Long getUserId(String token) {
        String jwt = token.substring(7);
        return tokenUtils.getSpecificClaim(tokenUtils.validateToken(jwt), ControllerConstants.USER_CLAIM).asLong();
    }
}
