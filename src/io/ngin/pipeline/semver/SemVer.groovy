package io.ngin.pipeline.semver

import com.cloudbees.groovy.cps.NonCPS

final class SemVer implements Serializable {
    private static final long serialVersionUID = 1L

    private final int[] version = new int[3]

    SemVer(int[] version) {
        if(version.length != 3) {
            throw new IllegalArgumentException('version must have 3 numeric parts')
        }

        // System.arraycopy isn't whitelisted, and we know we need exactly three bits
        this.version[0] = version[0]
        this.version[1] = version[1]
        this.version[2] = version[2]
    }

    @NonCPS
    SemVer increment(Component component) {
        SemVer updated = new SemVer(version)

        Component.each { Component it ->
            if(it < component) {
                updated.version[it.index] = 0
            } else if(it == component) {
                updated.version[it.index]++
            } // else do nothing
        }

        return updated
    }

    @Override
    @NonCPS
    boolean equals(Object o) {
        if(!(o instanceof SemVer)) {
            return false
        }

        version == ((SemVer) o).version
    }

    @Override
    @NonCPS
    String toString() {
        version[0] + '.' + version[1] + '.' + version[2]
    }

    @NonCPS
    static String findTag(String body, String tagName = 'version') {
        def matcher = (body =~ /\[\s*${tagName}\s+(\w*)\s*\]/)
        return matcher ? matcher[0][1] : null
    }

    @NonCPS
    static SemVer parse(String version) {
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

        return new SemVer(components)
    }

    @NonCPS
    static SemVer increment(String current, String level) {
        Component l = Component.find(level)
        if(!l) {
            throw new IllegalArgumentException("update level '$level' is not one of patch, minor, major")
        }

        SemVer c = parse(current)

        return c.increment(l)
    }
}
