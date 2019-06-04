/*
 * Copyright (c) 2019 The Hyve
 * This file is distributed under the MIT License (see accompanying file LICENSE).
 */

package nl.thehyve.whereabouts.controllers;

import nl.thehyve.whereabouts.dto.InstanceRepresentation;
import nl.thehyve.whereabouts.services.InstanceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
public class InstanceController {

    private final InstanceService instanceService;

    InstanceController(InstanceService instanceService) {
        this.instanceService = instanceService;
    }

    @GetMapping("/instances")
    public List<InstanceRepresentation> getInstances() {
        return instanceService.findAll();
    }

    @GetMapping("/instances/{id}")
    public InstanceRepresentation getInstance(@PathVariable Long id) {
        return instanceService.findById(id);
    }

    @PostMapping("/instances")
    public InstanceRepresentation addInstance(@RequestBody InstanceRepresentation newInstance) {
        return instanceService.addInstance(newInstance);
    }

    @PutMapping("/instances/{id}")
    public InstanceRepresentation replaceInstance(@RequestBody InstanceRepresentation newInstance, @PathVariable Long id) {
        newInstance.setId(id);
        return instanceService.replaceInstance(newInstance);
    }
}
