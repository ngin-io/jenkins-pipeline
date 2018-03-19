package io.ngin.pipeline.semver

import io.ngin.pipeline.semver.Component
import spock.lang.Specification

class SemVer_ComponentTest extends Specification {

    def "lookup method finds correct value"(String name, Component expected) {
        expect:
        expected == Component.find(name)

        where:
        name      || expected
        'invalid' || null
        'patch'   || Component.PATCH
        'MiNOR'   || Component.MINOR
        'MAjor'   || Component.MAJOR
    }

    def "indexes are as expected"(String name, int index) {
        expect:
        index == Component.find(name).index

        where:
        name      || index
        'patch'   || 2
        'MiNOR'   || 1
        'MAjor'   || 0
    }
}
