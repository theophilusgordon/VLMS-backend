package com.theophilusgordon.vlmsbackend.clocking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ClockingControllerTest {
    @Mock
    private ClockingService clockingService;

    @InjectMocks
    private ClockingController clockingController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testClockIn() {
        ClockInRequest request = new ClockInRequest(
                "123",
                "REMOTE"
        );
        request.setUserId("123");
        request.setWorkLocation("REMOTE");

        ClockingResponse expectedResponse = mock(ClockingResponse.class);
        when(clockingService.clockIn(request)).thenReturn(expectedResponse);

        ResponseEntity<ClockingResponse> responseEntity = clockingController.clockIn(request);

        assertEquals(expectedResponse, responseEntity.getBody());
        verify(clockingService, times(1)).clockIn(request);
    }

    @Test
    void testClockOut() {
        ClockOutRequest request = new ClockOutRequest(1);
        request.setClockingId(1);

        ClockingResponse expectedResponse = mock(ClockingResponse.class);
        when(clockingService.clockOut(request)).thenReturn(expectedResponse);

        ResponseEntity<ClockingResponse> responseEntity = clockingController.clockOut(request);

        assertEquals(expectedResponse, responseEntity.getBody());
        verify(clockingService, times(1)).clockOut(request);
    }
}