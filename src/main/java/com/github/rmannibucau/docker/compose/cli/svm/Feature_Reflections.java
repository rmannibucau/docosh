/**
 * Copyright (C) 2006-2019 Romain Manni-Bucau
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.rmannibucau.docker.compose.cli.svm;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.util.stream.Stream;

import org.apache.xbean.finder.ClassLoaders;
import org.apache.xbean.finder.archive.Archive;
import org.apache.xbean.finder.archive.ClasspathArchive;
import org.apache.xbean.finder.archive.CompositeArchive;
import org.apache.xbean.finder.archive.FilteredArchive;
import org.apache.xbean.finder.filter.Filter;
import org.apache.xbean.finder.filter.Filters;
import org.apache.xbean.finder.util.Files;
import org.graalvm.nativeimage.Feature;
import org.graalvm.nativeimage.RuntimeReflection;

import com.oracle.svm.core.annotate.AutomaticFeature;
import com.oracle.svm.core.annotate.TargetClass;

// register reflection meta we need for this app
@AutomaticFeature
public class Feature_Reflections implements Feature {
    // todo: config extraction
    private Filter createArchiveFilter() {
        return Filters.prefixes(
                // app
                "com.github.rmannibucau.docker.compose.cli",
                // crest
                "org.tomitribe.crest.cmds.targets.SimpleBean",
                "org.tomitribe.crest.cmds.processors.OptionParam",
                "org.tomitribe.crest.cmds.processors.Help");
    }

    // todo: move it to some plugin config
    private boolean isIncludedFile(final File jar) {
        return jar.getName().startsWith("docosh") ||
                jar.getName().startsWith("tomitribe-crest-0");
    }

    @Override
    public void beforeAnalysis(final BeforeAnalysisAccess access) {
        try {
            final ClassLoader loader = Thread.currentThread().getContextClassLoader();
            final Archive archive = new FilteredArchive(new CompositeArchive(
                    ClassLoaders.findUrls(loader).stream()
                         .filter(url -> isIncludedFile(Files.toFile(url)))
                         .map(url -> ClasspathArchive.archive(loader, url))
                         .collect(toList())),
                    createArchiveFilter());
            archive.forEach(entry -> {
                final Class<?> clazz = access.findClassByName(entry.getName());
                if (clazz != null
                        && Stream.of(AutomaticFeature.class, TargetClass.class).noneMatch(clazz::isAnnotationPresent)) {
                    registerModel(clazz);
                }
            });
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void registerModel(final Class<?> clazz) {
        RuntimeReflection.register(clazz);
        RuntimeReflection.register(Stream.of(clazz.getDeclaredConstructors()).toArray(Executable[]::new));
        RuntimeReflection.register(Stream.of(clazz.getDeclaredFields()).toArray(Field[]::new));
        RuntimeReflection.register(Stream.of(clazz.getDeclaredMethods()).toArray(Executable[]::new));
    }
}
