/*
 * Copyright (c) 2019 The Hyve
 * This file is distributed under the MIT License (see accompanying file LICENSE).
 */

package nl.thehyve.whereabouts;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.thehyve.whereabouts.domains.Instance;
import nl.thehyve.whereabouts.dto.InstanceRepresentation;
import nl.thehyve.whereabouts.repositories.InstanceRepository;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = MOCK, classes = WhereaboutsApplication.class)
@AutoConfigureMockMvc
public class InstanceControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private InstanceRepository repository;

    @After
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    public void givenInstances_whenGetInstances_thenStatus200() throws Exception {

        createTestInstance("address 1", "query 1");
        createTestInstance("address 2", "query 2");

        mvc.perform(get("/instances")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].address", Matchers.is("address 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sourceQuery", Matchers.is("query 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].address", Matchers.is("address 2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].sourceQuery", Matchers.is("query 2")));
    }

    @Test
    public void givenInstances_whenGetInstanceById_thenStatus200() throws Exception {

        Instance instance = createTestInstance("address 1", "query 1");

        mvc.perform(get("/instances/" + instance.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("address", Matchers.is("address 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("sourceQuery", Matchers.is("query 1")));

    }

    @Test
    public void givenNonExistingInstance_whenGetInstanceById_thenStatus404() throws Exception {
        mvc.perform(get("/instances/-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenInvalidInstance_whenPostInstance_thenStatus400() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        InstanceRepresentation invalidInstance = new InstanceRepresentation();
        invalidInstance.setAddress("");
        invalidInstance.setSourceQuery("test");
        mvc.perform(post("/instances")
                .content(mapper.writeValueAsString(invalidInstance))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenValidInstance_whenPostInstance_thenStatus400() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        InstanceRepresentation instance = new InstanceRepresentation();
        instance.setAddress("address");
        instance.setSourceQuery("test");
        mvc.perform(post("/instances")
                .content(mapper.writeValueAsString(instance))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    private Instance createTestInstance(String address, String sourceQuery) {
        Instance instance = new Instance(address, sourceQuery);
        return repository.save(instance);
    }

}
