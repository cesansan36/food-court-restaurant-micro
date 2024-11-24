package plazadecomidas.restaurants.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OperationResultTest {


    @ParameterizedTest
    @CsvSource({
        "true, Operation was successful",
        "false, Operation failed"
    })
    void createResultSuccessful(boolean success, String message) {

        OperationResult operationResult = new OperationResult(success, message);

        assertEquals(success, operationResult.isSuccess());
        assertEquals(message, operationResult.getMessage());
    }
}