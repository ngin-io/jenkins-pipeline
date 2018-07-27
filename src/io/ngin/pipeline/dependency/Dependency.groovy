package io.ngin.pipeline.dependency

import com.cloudbees.groovy.cps.NonCPS

import groovy.transform.EqualsAndHashCode
import groovy.transform.Immutable

@EqualsAndHashCode
@Immutable
class Dependency {
    String groupId
    String artifactId
    String version

    @NonCPS
    static Dependency fromGav(String gav) {
        String[] parts = gav.split(':')
        if(parts.length != 3) {
            throw new IllegalArgumentException("invalid GAV coordinates: $gav")
        }
        
        new Dependency(parts[0], parts[1], parts[2])
    }

    @NonCPS
    static Dependency fromReactorArtifact(String raf) {
        String[] parts = raf.trim().split(':')
        if(parts.length != 5) {
            throw new IllegalArgumentException("invalid artifact specification: $raf")
        }

        new Dependency(parts[0], parts[1], parts[3])
    }
}
