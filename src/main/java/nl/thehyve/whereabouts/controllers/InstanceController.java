/*
 * Copyright (c) 2019 The Hyve
 * This file is distributed under the MIT License (see accompanying file LICENSE).
 */

package nl.thehyve.whereabouts.controllers;

import nl.thehyve.whereabouts.domains.Instance;
import nl.thehyve.whereabouts.exceptions.InstanceNotFoundException;
import nl.thehyve.whereabouts.repositories.InstanceRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
public class InstanceController {

    private final InstanceRepository instanceRepository;

    InstanceController(InstanceRepository instanceRepository) {
        this.instanceRepository = instanceRepository;
    }

    @GetMapping("/instances")
    List<Instance> getInstances() {
        return instanceRepository.findAll();
    }

    @GetMapping("/instances/{id}")
    Instance getInstance(@PathVariable Long id) {
        return instanceRepository.findById(id)
                .orElseThrow(() -> new InstanceNotFoundException(id));
    }

    @PostMapping("/instances")
    Instance addInstance(@RequestBody Instance newInstance) {
        return instanceRepository.save(newInstance);
    }

    @PutMapping("/instances/{id}")
    Instance replaceInstance(@RequestBody Instance newInstance, @PathVariable Long id) {

        return instanceRepository.findById(id)
                .map(instance -> {
                    instance.setAddress(newInstance.getAddress());
                    instance.setSourceQuery(newInstance.getSourceQuery());
                    return instanceRepository.save(instance);
                })
                .orElseGet(() -> {
                    newInstance.setId(id);
                    return instanceRepository.save(newInstance);
                });
    }
}
