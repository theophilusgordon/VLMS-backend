package com.theophilusgordon.guestlogixserver.visit;

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

 class VisitControllerTest {

    @InjectMocks
    private VisitController checkInController;

    @Mock
    private VisitService visitService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(checkInController).build();
    }

    @Test
    void testCheckIn() throws Exception {
        VisitRequest request = new VisitRequest();
        request.setGuestId("guestId");
        request.setHostId("hostId");

        doNothing().when(visitService).checkIn(any(VisitRequest.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/check-in-out/check-in")
                        .content("{\"guestId\":\"guestId\", \"hostId\":\"hostId\"}")
                        .contentType("application/json"))
                .andExpect(status().isOk());

        verify(visitService, times(1)).checkIn(any(VisitRequest.class));
    }

    @Test
    void testCheckOut() throws Exception {
        doNothing().when(visitService).checkOut(anyInt());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/check-in-out/check-out/1"))
                .andExpect(status().isOk());

        verify(visitService, times(1)).checkOut(anyInt());
    }
}