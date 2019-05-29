/*
 * Copyright (c) 2019 The Hyve
 * This file is distributed under the MIT License (see accompanying file LICENSE).
 */

package nl.thehyve.whereabouts.exceptions;

public class InstanceNotFoundException extends RuntimeException {

    public InstanceNotFoundException(Long id) {
        super("Could not find instance " + id);
    }
}
