/*
 * Copyright (c) 2019 The Hyve
 * This file is distributed under the MIT License (see accompanying file LICENSE).
 */

package nl.thehyve.whereabouts.user;

/**
 * Spring Security Expressions for a user access control.
 * @see <a https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#expressions>SpEL</a>
 *
 * To be evaluated against the currently authenticated principal (current user)
 * in order to validate if the user has a required authority (client specific role).
 */
public final class UserRoleSecurityExpression {

    /**
     * Expression to control an access to GET operations on instances.
     */
    public final static String READ_INSTANCES = "hasAuthority('READ_INSTANCES')";

    /**
     * Controls access to POST operation on instances.
     */
    public final static String CREATE_INSTANCES = "hasAuthority('CREATE_INSTANCES')";

    /**
     * Controls access to PUT operation on instances.
     */
    public final static String CHANGE_INSTANCES = "hasAuthority('CHANGE_INSTANCES')";
}
