package io.ngin.pipeline.semver

import static org.junit.Assert.*

import org.junit.Test

import spock.lang.Specification

class SemVerTest extends Specification {

    def "findTag extracts tag with specified tagName"(String body, String tagName, String value) {
        when:
        String tagValue = SemVer.findTag(body, tagName)

        then:
        value == tagValue

        where:
        body                                  | tagName      || value
        '[version major]'                     | 'version'    || 'major'
        '[version major]'                     | 'notpresent' || null
        '[version minor][ semver foobar    ]' | 'semver'     || 'foobar'
        """Long
multiline
body
[version 
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
        '[  version  minor  ][semver foobar]' || 'minor'
        """Long
multiline
body
[version 
  here 
 ]
more stuff [issuetag foo]"""                  || 'here'
    }
}
