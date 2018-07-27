package io.ngin.pipeline.dependency

import java.util.regex.Matcher
import java.util.regex.Pattern

import com.cloudbees.groovy.cps.NonCPS

import groovy.transform.Immutable

class VersionsPluginUpdateParser {

    @NonCPS
    static Map<String, Section> parse(String versionsOutput) {
        return asSectionStrings(versionsOutput)
            .collect { Section.fromString(it) }
            .collectEntries { [it.name, it] }
    }

    @NonCPS
    static List<String> asSectionStrings(String versionsOutput) {
        versionsOutput.split('\n\n').toList()
            .collect { it.trim() }
            .findAll { !it.empty }
    }
    
    @Immutable
    static class Section {
        String name
        SortedSet<DependencyUpdate> updates
        
        static final Pattern HEADER_LINE = ~/^The following dependencies in ([\w\s]+) have newer versions:$/

        @NonCPS
        static Section fromString(String sectionString) {
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

            return new Section(name, updates)
        }
    }
}
