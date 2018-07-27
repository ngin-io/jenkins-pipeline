package io.ngin.pipeline.dependency

import java.util.regex.Matcher
import java.util.regex.Pattern

import com.cloudbees.groovy.cps.NonCPS

import groovy.transform.Immutable

class VersionsPluginUpdateParser {

    @NonCPS
    static Map<String, VersionsPluginSection> parse(String versionsOutput) {
        return asSectionStrings(versionsOutput)
            .collect { VersionsPluginSection.fromString(it) }
            .collectEntries { [it.name, it] }
    }

    @NonCPS
    static List<String> asSectionStrings(String versionsOutput) {
        versionsOutput.split('\n\n').toList()
            .collect { it.trim() }
            .findAll { !it.empty }
    }
}
