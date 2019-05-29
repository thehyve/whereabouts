/*
 * Copyright (c) 2019 The Hyve
 * This file is distributed under the MIT License (see accompanying file LICENSE).
 */

package nl.thehyve.whereabouts.repositories;

import nl.thehyve.whereabouts.domains.Instance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstanceRepository extends JpaRepository<Instance, Long> {
}
