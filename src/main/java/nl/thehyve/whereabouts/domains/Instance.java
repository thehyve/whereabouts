/*
 * Copyright (c) 2019 The Hyve
 * This file is distributed under the MIT License (see accompanying file LICENSE).
 */

package nl.thehyve.whereabouts.domains;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class Instance {
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @EqualsAndHashCode.Include Long id;
    private @NonNull String address;
    private @NonNull String sourceQuery;
}
