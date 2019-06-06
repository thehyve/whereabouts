/*
 * Copyright (c) 2019 The Hyve
 * This file is distributed under the MIT License (see accompanying file LICENSE).
 */

package nl.thehyve.whereabouts.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * A DTO for the Instance entity.
 */
@Data
public class InstanceRepresentation implements Serializable {

    private Long id;

    private @NotBlank String address;

    private @NotBlank String sourceQuery;

}
