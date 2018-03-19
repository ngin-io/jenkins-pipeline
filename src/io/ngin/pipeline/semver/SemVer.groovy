package io.ngin.pipeline.semver

import com.cloudbees.groovy.cps.NonCPS

class SemVer {

    @NonCPS
    static String findTag(String body, String tagName = 'version') {
        def matcher = (body =~ /\[\s*${tagName}\s+(\w*)\s*\]/)
        return matcher ? matcher[0][1] : null
    }

    @NonCPS
    static int[] parse(String version) {
        final GString MESSAGE_BAD_VERSION = "version '$version' was not in the format 'major.minor.patch'"

        String[] stringParts = version.split('\\.')

        if(stringParts.length != 3) {
            throw new IllegalArgumentException(MESSAGE_BAD_VERSION)
        }

        int[] components = new int[3]

        try {
            for(int i = 0; i < 3; i++) {
                components[i] = Integer.parseInt(stringParts[i])
                if(components[i] < 0) {
                    throw new IllegalArgumentException("version component ${components[i]} was negative in '$version'")
                }
            }
        } catch(NumberFormatException nfe) {
            throw new IllegalArgumentException(MESSAGE_BAD_VERSION, nfe)
        }

        return components
    }

    @NonCPS
    static int[] increment(int[] current, Component level) {
        int[] updated = new int[3]
        System.arraycopy(current, 0, updated, 0, 3)

        Component.each { Component it ->
            if(it < level) {
                updated[it.index] = 0
            } else if(it == level) {
                updated[it.index]++
            } // else do nothing
        }

        return updated
    }

    @NonCPS
    static int[] increment(String current, String level) {
        Component l = Component.find(level)
        if(!l) {
            throw new IllegalArgumentException("update level '$level' is not one of patch, minor, major")
        }

        int[] c = parse(current)

        return increment(c, l)
    }
}
