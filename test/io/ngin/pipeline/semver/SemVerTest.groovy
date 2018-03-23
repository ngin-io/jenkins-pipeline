package io.ngin.pipeline.semver

import static io.ngin.pipeline.semver.Component.*
import static org.junit.Assert.*

import io.ngin.pipeline.semver.Component
import spock.lang.Specification

class SemVerTest extends Specification {

    def "findTag extracts tag with specified tagName"(String body, String tagName, String value) {
        when:
        String tagValue = SemVer.findTag(body, tagName)

        then:
        value == tagValue

        where:
        body                                  | tagName      || value
        '[VERSION major]'                     | 'version'    || 'major'
        '[version major]'                     | 'notpresent' || null
        '[version minor][ semver foobar    ]' | 'semver'     || 'foobar'
        """Long
multiline
body
[Version 
  here 
 ]
more stuff [issuetag foo]"""                  | 'version'    || 'here'
    }

    def "findTag defaults to 'version'"(String body, String value) {
        when:
        String tagValue = SemVer.findTag(body)

        then:
        value == tagValue

        where:
        body                                  || value
        '[version major]'                     || 'major'
        '[semver major]'                      || null
        '[  Version  minor  ][semver foobar]' || 'minor'
        """Long
multiline
body
[verSIOn 
  here 
 ]
more stuff [issuetag foo]"""                  || 'here'
    }

    def "parsing successful for correct versions"(String version, SemVer expected) {
        expect:
        expected == SemVer.parse(version)

        where:
        version                || expected
        '0.0.0'                || new SemVer([0, 0, 0] as int[])
        '1.2.3'                || new SemVer([1, 2, 3] as int[])
        '63475.2120.292222000' || new SemVer([63475, 2120, 292222000] as int[])
    }

    def "parsing throws for invalid number of components"(String version) {
        when:
        SemVer.parse(version)

        then:
        IllegalArgumentException ex = thrown()
        ex.message.contains version

        where:
        version << [ '1.0', '2.3.4.5', '' ]
    }

    def "parsing throws for non-integer components"(String version) {
        when:
        SemVer.parse(version)

        then:
        IllegalArgumentException ex = thrown()
        ex.message.contains version
        ex.cause instanceof NumberFormatException

        where:
        version << [ '1.0.A', '2.3.4-SNAPSHOT' ]
    }

    def "parsing throws for negative components"(String version) {
        when:
        SemVer.parse(version)

        then:
        IllegalArgumentException ex = thrown()
        ex.message.contains version
        ex.message.contains 'negative'

        where:
        version << [ '1.0.-1', '2.-3.4', '-1.2.3' ]
    }

    def "updater returns correct results"(int[] current, Component level, int[] expected) {
        given:
        def currentVersion = new SemVer(current)
        def expectedVersion = new SemVer(expected)

        expect:
        expectedVersion == currentVersion.increment(level)

        where:
        current   | level || expected
        [0, 0, 0] | PATCH || [0, 0, 1]
        [0, 0, 0] | MINOR || [0, 1, 0]
        [0, 0, 0] | MAJOR || [1, 0, 0]

        [5, 6, 7] | PATCH || [5, 6, 8]
        [5, 6, 7] | MINOR || [5, 7, 0]
        [5, 6, 7] | MAJOR || [6, 0, 0]
    }
    
    def "toString joins correctly"(int[] integers, String string) {
        expect:
        string == new SemVer(integers).toString()

        where:
        integers                 || string
        [0, 0, 0]                || '0.0.0'
        [1, 2, 3]                || '1.2.3'
        [63475, 2120, 292222000] || '63475.2120.292222000'
    }

    def "end-to-end update test"(String current, String level, int[] expected) {
        given:
        def expectedVersion = new SemVer(expected)

        expect:
        expectedVersion == SemVer.increment(current, level)

        where:
        current    | level   || expected
        '0.0.0'    | 'patch' || [0, 0, 1]
        '0.0.0'    | 'Minor' || [0, 1, 0]
        '0.0.0'    | 'MAJOR' || [1, 0, 0]

        '12.13.14' | 'PaTCH' || [12, 13, 15]
        '12.13.14' | 'miNoR' || [12, 14,  0]
        '12.13.14' | 'major' || [13,  0,  0]
    }
}
