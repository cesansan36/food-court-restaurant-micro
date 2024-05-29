package plazadecomidas.restaurants.adapters.driving.http.rest.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import plazadecomidas.restaurants.adapters.driving.http.rest.dto.response.RecordResponse;
import plazadecomidas.restaurants.adapters.driving.http.rest.mapper.IAddOrderRecordRequestMapper;
import plazadecomidas.restaurants.adapters.driving.http.rest.mapper.IOrderRecordResponseMapper;
import plazadecomidas.restaurants.adapters.driving.http.rest.util.ControllerConstants;
import plazadecomidas.restaurants.domain.primaryport.IOrderRecordPrimaryPort;
import plazadecomidas.restaurants.util.ITokenUtils;

import java.util.List;

@RestController
@RequestMapping("/records")
@AllArgsConstructor
public class OrderRecordControllerAdapter {

    private final IOrderRecordPrimaryPort orderRecordPrimaryPort;
    private final IAddOrderRecordRequestMapper addOrderRecordRequestMapper;
    private final IOrderRecordResponseMapper orderRecordResponseMapper;
    private final ITokenUtils tokenUtils;

    @GetMapping("/listRecords")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<RecordResponse>> list(@RequestHeader("Authorization") String token) {
        Long clientId = getUserId(token);
        List<RecordResponse> response = orderRecordResponseMapper.recordsToRecordResponses(
                orderRecordPrimaryPort.listRecords(token, clientId));

        return ResponseEntity.ok(response);
    }

    private Long getUserId(String token) {
        String jwt = token.substring(7);
        return tokenUtils.getSpecificClaim(tokenUtils.validateToken(jwt), ControllerConstants.USER_CLAIM).asLong();
    }
}
