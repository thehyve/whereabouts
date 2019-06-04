/*
 * Copyright (c) 2019 The Hyve
 * This file is distributed under the MIT License (see accompanying file LICENSE).
 */

package nl.thehyve.whereabouts.services.mapper;

import nl.thehyve.whereabouts.domains.Instance;
import nl.thehyve.whereabouts.dto.InstanceRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = { })
public interface InstanceMapper {
    InstanceMapper MAPPER = Mappers.getMapper( InstanceMapper.class );

    InstanceRepresentation instanceToInstanceRepresentation(Instance instance);

    @Mapping(target = "id", ignore = true)
    Instance createInstanceFromInstanceRepresentation(InstanceRepresentation instanceRepresentation);

    @Mapping(target = "id", ignore = true)
    Instance updateInstanceFromOrganisationRepresentation(
            InstanceRepresentation instanceRepresentation,
            @MappingTarget Instance instance
    );
}
