package com.theophilusgordon.guestlogixserver.checkInOut;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CheckInOutControllerTest {

    @InjectMocks
    private CheckInOutController checkInOutController;

    @Mock
    private CheckInOutService checkInOutService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(checkInOutController).build();
    }

    @Test
    void testCheckIn() throws Exception {
        CheckInRequest request = new CheckInRequest();
        request.setGuestId("guestId");
        request.setHostId("hostId");

        doNothing().when(checkInOutService).checkIn(any(CheckInRequest.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/check-in-out/check-in")
                        .content("{\"guestId\":\"guestId\", \"hostId\":\"hostId\"}")
                        .contentType("application/json"))
                .andExpect(status().isOk());

        verify(checkInOutService, times(1)).checkIn(any(CheckInRequest.class));
    }

    @Test
    void testCheckOut() throws Exception {
        doNothing().when(checkInOutService).checkOut(anyInt());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/check-in-out/check-out/1"))
                .andExpect(status().isOk());

        verify(checkInOutService, times(1)).checkOut(anyInt());
    }
}