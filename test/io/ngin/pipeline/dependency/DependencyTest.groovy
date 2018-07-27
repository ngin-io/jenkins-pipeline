package io.ngin.pipeline.dependency

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import static org.apache.commons.lang3.RandomUtils.nextInt

import io.ngin.pipeline.semver.SemVer
import spock.lang.Specification

class DependencyTest extends Specification {
    
    def "GAV factory returns correct values"() {
        given:
        String groupId = 'com.example.' + randomAlphabetic(10)
        String artifactId = randomAlphabetic(10)
        SemVer version = [nextInt(1,5), nextInt(0,10), nextInt(0,100)]
        String gav = "$groupId:$artifactId:$version"

        and:
        Dependency dependency = Dependency.fromGav(gav)
        
        expect:
        groupId == dependency.groupId
        artifactId == dependency.artifactId
        version == SemVer.parse(dependency.version)
    }

    def "RAF factory returns correct values"(String raf, String groupId, String artifactId, String version) {
        given:
        Dependency dependency = Dependency.fromReactorArtifact(raf)

        expect:
        groupId == dependency.groupId
        artifactId == dependency.artifactId
        version == dependency.version
        
        where:
        raf || groupId | artifactId | version
        'io.ngin.service.emailsender:email-sender-api:jar:1.2.0:compile' || 'io.ngin.service.emailsender' | 'email-sender-api' | '1.2.0'
        'org.ow2.asm:asm:jar:5.0.3:test' || 'org.ow2.asm' | 'asm' | '5.0.3'
        'org.springframework.boot:spring-boot-autoconfigure:jar:1.5.12.RELEASE:compile' || 'org.springframework.boot' | 'spring-boot-autoconfigure' | '1.5.12.RELEASE'
    }
}
