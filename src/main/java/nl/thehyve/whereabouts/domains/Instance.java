/*
 * Copyright (c) 2019 The Hyve
 * This file is distributed under the MIT License (see accompanying file LICENSE).
 */

package nl.thehyve.whereabouts.domains;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Instance {
    private @Id @GeneratedValue Long id;
    private String address;
    private String sourceQuery;

    Instance() {}

    public Instance(String address, String sourceQuery) {
        this.address = address;
        this.sourceQuery = sourceQuery;
    }
}
