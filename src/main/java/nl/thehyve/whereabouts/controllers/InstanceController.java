/*
 * Copyright (c) 2019 The Hyve
 * This file is distributed under the MIT License (see accompanying file LICENSE).
 */

package nl.thehyve.whereabouts.controllers;

import nl.thehyve.whereabouts.dto.InstanceRepresentation;
import nl.thehyve.whereabouts.services.InstanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static nl.thehyve.whereabouts.user.UserRoleSecurityExpression.*;

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
     * @return the list of all instances,
     *         or an exception (403) if user does not have a role 'read-instances'
     */
    @GetMapping
    @PreAuthorize(READ_INSTANCES)
    public List<InstanceRepresentation> getInstances() {
        return instanceService.findAll();
    }

    /**
     * GET /instances/{id} : gets the instance with the id.
     *
     * @param id the id of the instance.
     * @return the instance with the id, if it exists;
     *         an exception (404) if does not exist
     *         or (403) if user does not have a role 'read-instances'
     */
    @GetMapping("/{id}")
    @PreAuthorize(READ_INSTANCES)
    public InstanceRepresentation getInstance(@PathVariable("id") Long id) {
        return instanceService.findById(id);
    }

    /**
     * POST /instances : adds a new instance.
     *
     * @param newInstance the properties of the new instance.
     * @return the added instance with the generated id, if the data is valid,
     *         an exception (400) if the data is not valid
     *         or (403) if user does not have a role 'create-instances'
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(CREATE_INSTANCES)
    public ResponseEntity<InstanceRepresentation> addInstance(
            @Valid @RequestBody InstanceRepresentation newInstance) {
        return new ResponseEntity<>(instanceService.addInstance(newInstance), HttpStatus.CREATED);
    }

    /**
     * PUT /instances/{id} : updates the instance with the id.
     *
     * @param id the id of the instance.
     * @param newInstance the object with properties used for updating the instance.
     * @return the updated instance, if it exists and the data is valid;
     *         an exception if the instance cannot be found (404)
     *         or the data is invalid (400),
     *         or user does not have a role 'change-instances' (403)
     */
    @PutMapping("/{id}")
    @PreAuthorize(CHANGE_INSTANCES)
    public InstanceRepresentation replaceInstance(
            @PathVariable("id") Long id,
            @Valid @RequestBody InstanceRepresentation newInstance) {
        newInstance.setId(id);
        return instanceService.replaceInstance(newInstance);
    }
}
