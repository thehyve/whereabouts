/*
 * Copyright (c) 2019 The Hyve
 * This file is distributed under the MIT License (see accompanying file LICENSE).
 */

package nl.thehyve.whereabouts.services;

import nl.thehyve.whereabouts.domains.Instance;
import nl.thehyve.whereabouts.dto.InstanceRepresentation;
import nl.thehyve.whereabouts.exceptions.InstanceNotFoundException;
import nl.thehyve.whereabouts.repositories.InstanceRepository;
import nl.thehyve.whereabouts.services.mapper.InstanceMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InstanceService {

    private final InstanceRepository instanceRepository;

    InstanceService(InstanceRepository instanceRepository) {
        this.instanceRepository = instanceRepository;
    }

    public Page<InstanceRepresentation> getPage(Pageable pageable) {
        return instanceRepository.findAll(pageable)
            .map(InstanceMapper.MAPPER::instanceToInstanceRepresentation);
    }

    public InstanceRepresentation findById(Long id) {
        return InstanceMapper.MAPPER.instanceToInstanceRepresentation(instanceRepository.findById(id)
                .orElseThrow(() -> new InstanceNotFoundException(id)));
    }

    public InstanceRepresentation addInstance(InstanceRepresentation instanceRepresentation) {
        Instance instance = InstanceMapper.MAPPER.createInstanceFromInstanceRepresentation(instanceRepresentation);
        instanceRepository.save(instance);
        return InstanceMapper.MAPPER.instanceToInstanceRepresentation(instance);
    }

    public InstanceRepresentation replaceInstance(InstanceRepresentation instanceRepresentation) {

        Instance instance = instanceRepository.findById(instanceRepresentation.getId())
                .orElseThrow(() -> new InstanceNotFoundException(instanceRepresentation.getId()));

        instance = InstanceMapper.MAPPER.updateInstanceFromInstanceRepresentation(
                instanceRepresentation, instance
        );

        instanceRepository.save(instance);

        return InstanceMapper.MAPPER.instanceToInstanceRepresentation(instance);
    }
}
