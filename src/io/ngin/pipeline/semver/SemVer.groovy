package io.ngin.pipeline.semver

import groovy.transform.CompileStatic

class SemVer {

    static String findTag(String body, String tagName = 'version') {
        def matcher = (body =~ /\[\s*${tagName}\s+(\w*)\s*\]/)
        return matcher ? matcher[0][1] : null
    }

}
