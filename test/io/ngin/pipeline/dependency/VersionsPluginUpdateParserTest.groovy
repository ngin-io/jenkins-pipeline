package io.ngin.pipeline.dependency

import spock.lang.Specification

class VersionsPluginUpdateParserTest extends Specification {
    final String VERSIONS_OUT = """The following dependencies in Dependencies have newer versions:
  com.marketngin:metric-client .................. [2.0.0,3.0.0) -> 3.0.0
  javax.xml.bind:jaxb-api .................. 2.3.0 -> 2.4.0-b180725.0427
  org.codehaus.groovy:groovy-all ............... 2.4.15 -> 3.0.0-alpha-3
  org.codehaus.groovy.modules.http-builder:http-builder ...
                                                          0.7.1 -> 0.7.2
  org.hibernate:hibernate-c3p0 ............. 5.2.17.Final -> 5.3.3.Final
  org.postgresql:postgresql ............... 9.4.1212.jre7 -> 42.2.4.jre7
  org.springframework:spring-test ...... 4.3.16.RELEASE -> 5.0.8.RELEASE
  org.springframework.boot:spring-boot-autoconfigure ...
                                         1.5.12.RELEASE -> 2.0.3.RELEASE
  org.springframework.boot:spring-boot-configuration-processor ...
                                         1.5.12.RELEASE -> 2.0.3.RELEASE
  org.springframework.boot:spring-boot-starter ...
                                         1.5.12.RELEASE -> 2.0.3.RELEASE
  org.springframework.boot:spring-boot-starter-mail ...
                                         1.5.12.RELEASE -> 2.0.3.RELEASE
  org.springframework.boot:spring-boot-starter-test ...
                                         1.5.12.RELEASE -> 2.0.3.RELEASE
  org.springframework.boot:spring-boot-starter-thymeleaf ...
                                         1.5.12.RELEASE -> 2.0.3.RELEASE
  org.springframework.boot:spring-boot-starter-web ...
                                         1.5.12.RELEASE -> 2.0.3.RELEASE
  org.springframework.data:spring-data-commons ...
                                        1.13.11.RELEASE -> 2.0.8.RELEASE
  org.springframework.data:spring-data-redis ...
                                         1.8.11.RELEASE -> 2.0.8.RELEASE
  org.springframework.security:spring-security-web ...
                                          4.2.5.RELEASE -> 5.0.7.RELEASE
  org.springframework.session:spring-session-data-redis ...
                                          1.3.2.RELEASE -> 2.0.4.RELEASE

The following dependencies in pluginManagement of plugins have newer versions:
  org.apache.maven.surefire:surefire-junit47 .......... 2.21.0 -> 2.22.0

The following dependencies in Plugin Dependencies have newer versions:
  org.apache.maven.surefire:surefire-junit47 .......... 2.21.0 -> 2.22.0


"""

    def "test parser"() {
        given:
        def s = VersionsPluginUpdateParser.asSectionStrings(VERSIONS_OUT)
        
        when:
        def u = VersionsPluginUpdateParser.parse(VERSIONS_OUT)
        
        then:
        ['Dependencies', 'pluginManagement of plugins', 'Plugin Dependencies'] as Set == u.keySet()

        and:
        DependencyUpdate surefire = u['Plugin Dependencies'].updates.find()
        'org.apache.maven.surefire' == surefire.groupId
        'surefire-junit47' == surefire.artifactId
        '2.21.0' == surefire.currentVersion
        '2.22.0' == surefire.newVersion

        and:
        12 == u['Dependencies'].updates.count { it.groupId.startsWith 'org.springframework' }
    }
}
