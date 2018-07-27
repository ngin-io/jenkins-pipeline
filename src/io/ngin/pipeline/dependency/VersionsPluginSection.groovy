package io.ngin.pipeline.dependency

import java.util.regex.Matcher
import java.util.regex.Pattern

import com.cloudbees.groovy.cps.NonCPS

class VersionsPluginSection implements Serializable {
    private static final long serialVersionUID = 1L
    
    final String name
    final SortedSet<DependencyUpdate> updates
    
    static final Pattern HEADER_LINE = ~/^The following dependencies in ([\w\s]+) have newer versions:$/

    VersionsPluginSection(String name, SortedSet<DependencyUpdate> updates) {
        this.name = name
        this.updates = new TreeSet(updates)
    }
    
    @Override
    @NonCPS
    String toString() {
        "VersionsPluginSection[$name: $updates]"
    }

    @NonCPS
    static VersionsPluginSection fromString(String sectionString) {
        Scanner sc = new Scanner(sectionString)
        String header = sc.nextLine()
        
        Matcher m = HEADER_LINE.matcher(header)
        
        if(!m.matches()) {
            throw new IllegalArgumentException("no section header line found in: $header")
        }
        
        String name = m.group(1)
        SortedSet<DependencyUpdate> updates = new TreeSet()
        
        while(sc.hasNext()) {
            String[] ga = sc.next().split(':')
            assert ga.length == 2

            assert sc.next().matches('\\.+')

            String declaredVersion = sc.next()
            
            assert sc.next() == '->'
            
            String availableVersion = sc.next()
            
            updates << new DependencyUpdate(ga[0], ga[1], declaredVersion, availableVersion)
        }

        return new VersionsPluginSection(name, updates)
    }
}
