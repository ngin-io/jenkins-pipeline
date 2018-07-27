package io.ngin.pipeline.dependency

import com.cloudbees.groovy.cps.NonCPS

class DependencyUpdate implements Serializable, Comparable<DependencyUpdate> {
    private static final long serialVersionUID = 1L

    String groupId
    String artifactId
    String currentVersion
    String newVersion

    DependencyUpdate(String groupId, String artifactId, String currentVersion, String newVersion) {
        this.groupId = groupId
        this.artifactId = artifactId
        this.currentVersion = currentVersion
        this.newVersion = newVersion
    }
    
    @Override
    @NonCPS
    boolean equals(Object o) {
        if(!(o instanceof DependencyUpdate)) {
            return false
        }
        
        DependencyUpdate that = (DependencyUpdate) o
        return groupId == that.groupId &&
            artifactId == that.artifactId &&
            currentVersion == that.currentVersion &&
            newVersion == that.newVersion
    }

    @Override
    int hashCode() {
        return groupId.hashCode() + 31 * (
            artifactId.hashCode() + 31 * (
                currentVersion.hashCode() + 31 * (
                    newVersion.hashCode() )))
    }
    
    @Override
    @NonCPS
    int compareTo(DependencyUpdate o) {
        groupId <=> o.groupId ?:
            artifactId <=> o.artifactId ?:
            currentVersion <=> o.currentVersion ?:
            newVersion <=> o.newVersion ?:
            0
    }

    @Override
    @NonCPS
    String toString() {
        "$groupId:$artifactId $currentVersion → $newVersion"
    }
}
