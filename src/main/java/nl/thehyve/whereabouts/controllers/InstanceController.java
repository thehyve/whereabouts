/*
 * Copyright (c) 2019 The Hyve
 * This file is distributed under the MIT License (see accompanying file LICENSE).
 */

package nl.thehyve.whereabouts.controllers;

import nl.thehyve.whereabouts.dto.InstanceRepresentation;
import nl.thehyve.whereabouts.exceptions.InvalidRequest;
import nl.thehyve.whereabouts.services.InstanceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

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

    private Set<String> sortFields = Arrays.stream(InstanceRepresentation.class.getDeclaredFields())
        .map(Field::getName)
        .collect(Collectors.toSet());

    private void checkSortFields(Pageable pageable) {
        Optional<String> invalidSortField = pageable.getSort().stream()
            .map(Sort.Order::getProperty)
            .filter(property -> !sortFields.contains(property))
            .findAny();
        if (invalidSortField.isPresent()) {
            throw new InvalidRequest(
                "Invalid sort field: '" + invalidSortField.get() + "'. " +
                    "Supported fields: " + String.join(", ", sortFields) + ".");
        }
    }

    /**
     * GET /instances?page={page}&size={size}&sort={sort} : get all instances.
     *
     * Example:
     *   GET /instances?page=1&size=20&sort=address,asc&sort=id,desc
     *
     *   This returns the second page, with page size 20, with the instances first
     *   ordered by address (in ascending order), then by id (in descending order).
     *
     * @param pageable pagination properties:
     *                 - size: the page size
     *                 - page: the page to display, zero-based
     *                 - sort: sort criteria in the form '{property},{asc|desc}'
     * @return the list of all instances,
     *         or an exception (403) if user does not have a role 'read-instances'
     */
    @GetMapping
    @PreAuthorize(READ_INSTANCES)
    public ResponseEntity<List<InstanceRepresentation>> getInstances(Pageable pageable) {
        checkSortFields(pageable);
        Page<InstanceRepresentation> page = instanceService.getPage(pageable);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", "" + Long.toString(page.getTotalElements()));
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
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
