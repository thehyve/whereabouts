/*
 * Copyright (c) 2019 The Hyve
 * This file is distributed under the MIT License (see accompanying file LICENSE).
 */

package nl.thehyve.whereabouts;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.thehyve.whereabouts.domains.Instance;
import nl.thehyve.whereabouts.dto.InstanceRepresentation;
import nl.thehyve.whereabouts.repositories.InstanceRepository;
import nl.thehyve.whereabouts.services.mapper.InstanceMapper;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @WithMockUser(username="spring", authorities={"create-instances"})
    @Test
    public void givenNoInstances_whenPostInstance_thenStatus200() throws Exception {

        int sizeBeforeCreate = repository.findAll().size();
        Instance instance = new Instance("test address", "test query");
        InstanceRepresentation instanceRepresentation = InstanceMapper.MAPPER.instanceToInstanceRepresentation(instance);

        mvc.perform(post("/instances")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(instanceRepresentation)))
                .andExpect(status().isCreated());

        int sizeAfterCreate = repository.findAll().size();
        Assert.assertEquals(sizeAfterCreate, sizeBeforeCreate + 1);
    }

    @WithMockUser(username="spring", authorities={"change-instances"})
    @Test
    public void givenInstances_whenPutInstance_thenStatus200() throws Exception {

        Instance instance = createTestInstance("test address", "test query");
        int sizeBeforeCreate = repository.findAll().size();
        instance.setAddress("changed address");
        InstanceRepresentation instanceRepresentation = InstanceMapper.MAPPER.instanceToInstanceRepresentation(instance);

        instanceRepresentation.setAddress("changed address");
        mvc.perform(put("/instances/" + instance.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(instanceRepresentation)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("address", Matchers.is("changed address")))
                .andExpect(MockMvcResultMatchers.jsonPath("sourceQuery", Matchers.is("test query")));

        int sizeAfterCreate = repository.findAll().size();
        Assert.assertEquals(sizeAfterCreate, sizeBeforeCreate);
    }

    @WithMockUser(username="spring", authorities={"change-instances"})
    @Test
    public void givenNonExistingInstance_whenPutInstance_thenStatus404() throws Exception {
        Instance instance = new Instance("test address", "test query");
        InstanceRepresentation instanceRepresentation = InstanceMapper.MAPPER.instanceToInstanceRepresentation(instance);

        mvc.perform(put("/instances/-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(instanceRepresentation)))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username="spring", authorities={"read-instances"})
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

    @WithMockUser(username="spring", authorities={"read-instances"})
    @Test
    public void givenInstances_whenGetInstanceById_thenStatus200() throws Exception {

        Instance instance = createTestInstance("address 1", "query 1");
        InstanceRepresentation instanceRepresentation = InstanceMapper.MAPPER.instanceToInstanceRepresentation(instance);

        mvc.perform(get("/instances/" + instanceRepresentation.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("address", Matchers.is("address 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("sourceQuery", Matchers.is("query 1")));

    }

    @WithMockUser(username="spring", authorities={"read-instances"})
    @Test
    public void givenNonExistingInstance_whenGetInstanceById_thenStatus404() throws Exception {
        mvc.perform(get("/instances/-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username="spring", authorities={"create-instances"})
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

    @WithMockUser(username="spring", authorities={"create-instances"})
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

    @WithMockUser(username="spring", authorities={})
    @Test
    public void givenNoRole_whenPostInstance_thenStatus403() throws Exception {

        Instance instance = new Instance("test address", "test query");
        InstanceRepresentation instanceRepresentation = InstanceMapper.MAPPER.instanceToInstanceRepresentation(instance);

        mvc.perform(post("/instances")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(instanceRepresentation)))
                .andExpect(status().isForbidden());

        int sizeAfterCreate = repository.findAll().size();
        Assert.assertEquals(sizeAfterCreate, 0);
    }

    @WithMockUser(username="spring", authorities={"create-instances"})
    @Test
    public void givenInvalidRole_whenGetInstance_thenStatus403() throws Exception {

        createTestInstance("address 1", "query 1");

        mvc.perform(get("/instances")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username="spring", authorities={})
    @Test
    public void givenNoRole_whenPutInstance_thenStatus403() throws Exception {

        Instance instance = createTestInstance("test address", "test query");
        InstanceRepresentation instanceRepresentation = InstanceMapper.MAPPER.instanceToInstanceRepresentation(instance);

        mvc.perform(put("/instances/" + instance.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(instanceRepresentation)))
                .andExpect(status().isForbidden());
    }

    private Instance createTestInstance(String address, String sourceQuery) {
        Instance instance = new Instance(address, sourceQuery);
        return repository.save(instance);
    }

}
