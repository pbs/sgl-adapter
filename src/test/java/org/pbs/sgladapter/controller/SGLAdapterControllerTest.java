package org.pbs.sgladapter.controller;

import org.junit.jupiter.api.Test;
import org.pbs.sgladapter.service.ISGLAdapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(SGLAdapterController.class)
@AutoConfigureMockMvc(addFilters = false)
class SGLAdapterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ISGLAdapterService sglAdapterService;

    private static final String CREATE_TASK = "/v1/task";

    @Test
    void testCreateTask() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(CREATE_TASK)
                .contentType(MediaType.TEXT_PLAIN)
                        .content("{}");
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(200, response.getStatus());

    }
}