package com.pwawrzyniak.tlog.server.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.pwawrzyniak.tlog.server.controller.ControllerPaths.BILL;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BillControllerTest {

    private MockMvc mockMvc;

    @Before
    public void setup() {

        mockMvc = MockMvcBuilders.standaloneSetup(new BillController()).build();
    }

    @Test
    public void shouldReturnData() throws Exception {

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(BILL));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].billItems[0].labelList[0].value", is("clothes")));
    }
}