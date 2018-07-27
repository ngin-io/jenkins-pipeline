package io.ngin.pipeline.dependency

import static io.ngin.pipeline.semver.Component.MINOR
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import static org.apache.commons.lang3.RandomUtils.nextInt

import io.ngin.pipeline.semver.SemVer
import spock.lang.Specification

class DependencyUpdateTest extends Specification {

    String groupId = 'com.example.' + randomAlphabetic(10)
    String artifactId = randomAlphabetic(10)
    String currentVersion = new SemVer([nextInt(1,5), nextInt(0,10), nextInt(0,100)] as int[]) as String
    String newVersion = SemVer.increment(currentVersion, 'MINOR')

    def "equals and hashCode because Jenkins is nuts"() {
        given:
        DependencyUpdate a1 = new DependencyUpdate(groupId, artifactId, currentVersion, newVersion)
        DependencyUpdate a2 = new DependencyUpdate(groupId, artifactId, currentVersion, newVersion)
        DependencyUpdate b = new DependencyUpdate(groupId, artifactId + '-test', currentVersion, newVersion)

        expect:
        a1 == a2
        a1.hashCode() == a2.hashCode()

        a1 != b
        a1.hashCode() != b.hashCode()
    }
}
