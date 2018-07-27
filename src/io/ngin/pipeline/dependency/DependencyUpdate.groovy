package io.ngin.pipeline.dependency

import com.cloudbees.groovy.cps.NonCPS

import groovy.transform.EqualsAndHashCode
import groovy.transform.Immutable
import groovy.transform.Sortable

@EqualsAndHashCode
@Immutable
@Sortable
class DependencyUpdate implements Serializable {
    private static final long serialVersionUID = 1L

    String groupId
    String artifactId
    String currentVersion
    String newVersion

    @Override
    @NonCPS
    String toString() {
        "$groupId:$artifactId $currentVersion → $newVersion"
    }
}
