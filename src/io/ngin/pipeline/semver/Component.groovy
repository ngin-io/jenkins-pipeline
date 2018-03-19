package io.ngin.pipeline.semver

import groovy.transform.CompileStatic

@CompileStatic
enum Component {
    PATCH(2),
    MINOR(1),
    MAJOR(0);

    final int index

    private Component(int index) {
        this.index = index
    }

    static Component find(String component) {
        (Component) Component.find { it -> it.toString().equalsIgnoreCase(component) }
    }
}
