package com.theophilusgordon.guestlogixserver.checkin;

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

 class CheckinControllerTest {

    @InjectMocks
    private CheckinController checkInController;

    @Mock
    private CheckinService checkinService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(checkInController).build();
    }

    @Test
    void testCheckIn() throws Exception {
        CheckinRequest request = new CheckinRequest();
        request.setGuestId("guestId");
        request.setHostId("hostId");

        doNothing().when(checkinService).checkIn(any(CheckinRequest.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/check-in-out/check-in")
                        .content("{\"guestId\":\"guestId\", \"hostId\":\"hostId\"}")
                        .contentType("application/json"))
                .andExpect(status().isOk());

        verify(checkinService, times(1)).checkIn(any(CheckinRequest.class));
    }

    @Test
    void testCheckOut() throws Exception {
        doNothing().when(checkinService).checkOut(anyInt());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/check-in-out/check-out/1"))
                .andExpect(status().isOk());

        verify(checkinService, times(1)).checkOut(anyInt());
    }
}