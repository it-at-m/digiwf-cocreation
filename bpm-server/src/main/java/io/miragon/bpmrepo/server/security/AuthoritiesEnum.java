/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2021
 */
package io.miragon.bpmrepo.server.security;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;


/**
 * Each possible authority in this project is represented by an enum.
 * The enums are used within the {@link PagingAndSortingRepository}
 * in the annotation e.g. {@link PreAuthorize}.
 */
public enum AuthoritiesEnum {
    BPM_SERVER_READ_THEENTITY,
    BPM_SERVER_WRITE_THEENTITY,
    BPM_SERVER_DELETE_THEENTITY,
    // add your authorities here and also add these new authorities to sso-authorisation.json.
}
