/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2021
 */
package io.miragon.bpmrepo.server.configuration;


import io.miragon.bpmrepo.core.artifact.domain.enums.DeploymentStatus;
import io.miragon.bpmrepo.core.artifact.infrastructure.entity.DeploymentEntity;
import io.miragon.bpmrepo.core.artifact.infrastructure.repository.DeploymentJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This class implements a scheduled job in {@link DeploymentCleanupJobConfiguration#cleanUpPendingDeployments()}
 * that timeouts all PENDING deployments once per day
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class DeploymentCleanupJobConfiguration {

    private final DeploymentJpaRepository deploymentJpaRepository;

    /**
     * This method timeouts all PENDING deployments that run for more than 6 hours with the Status ERROR and the message timeout.
     * Therefore, this prevents the frontend from polling deployments infinitely if any old deployment is still in the status PENDING
     *
     * This job runs every day at 3:00 am and sets every deployments status to ERROR with the message timeout
     * if the deployment was PENDING for more than 6 hours.
     */
    @Scheduled(cron = "0 0 3 * * *")
    public void cleanUpPendingDeployments() {
        final List<DeploymentEntity> deployments = this.deploymentJpaRepository.findAll();
        deployments
                .stream()
                .filter(deployment -> deployment.getStatus().equals(DeploymentStatus.PENDING))
                .filter(deployment -> deployment.getTimestamp().isBefore(LocalDateTime.now().minusHours(6)))
                .forEach(deployment -> {
                    // set the deployment status to error if it did not succeed within 6 hours
                    deployment.setStatus(DeploymentStatus.ERROR);
                    deployment.setMessage("Timeout");
                    this.deploymentJpaRepository.save(deployment);
                });
    }

}
