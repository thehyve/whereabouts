/*
 * Copyright (c) 2019 The Hyve
 * This file is distributed under the MIT License (see accompanying file LICENSE).
 */

package nl.thehyve.whereabouts.controllers;

import nl.thehyve.whereabouts.dto.InstanceRepresentation;
import nl.thehyve.whereabouts.services.InstanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@CrossOrigin
@RequestMapping("/instances")
public class InstanceController {

    private final InstanceService instanceService;

    InstanceController(InstanceService instanceService) {
        this.instanceService = instanceService;
    }

    /**
     * GET /instances : get all instances.
     *
     * @return the list of all instances.
     */
    @GetMapping
    public List<InstanceRepresentation> getInstances() {
        return instanceService.findAll();
    }

    /**
     * GET /instances/{id} : gets the instance with the id.
     *
     * @param id the id of the instance.
     * @return the instance with the id, if it exists; an exception (404) otherwise.
     */
    @GetMapping("/{id}")
    public InstanceRepresentation getInstance(@PathVariable("id") Long id) {
        return instanceService.findById(id);
    }

    /**
     * POST /instances : adds a new instance.
     *
     * @param newInstance the properties of the new instance.
     * @return the added instance with the generated id, if the data is valid,
     * an exception (400) is the data is not valid.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<InstanceRepresentation> addInstance(
            @Valid @RequestBody InstanceRepresentation newInstance) {
        return new ResponseEntity<>(instanceService.addInstance(newInstance), HttpStatus.CREATED);
    }

    /**
     * PUT /instances/{id} : updates the instance with the id.
     *
     * @param id the id of the instance.
     * @param newInstance the object with properties used for updating the instance.
     * @return the updated instance, if it exists and the data is valid; an exception if the instance
     * cannot be found (404) or the data is invalid (400).
     */
    @PutMapping("/{id}")
    public InstanceRepresentation replaceInstance(
            @PathVariable("id") Long id,
            @Valid @RequestBody InstanceRepresentation newInstance) {
        newInstance.setId(id);
        return instanceService.replaceInstance(newInstance);
    }
}
