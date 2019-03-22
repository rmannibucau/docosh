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

import static java.util.Collections.list;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import org.graalvm.nativeimage.Feature;
import org.tomitribe.crest.cmds.processors.Commands;

import com.oracle.svm.core.annotate.AutomaticFeature;
import com.oracle.svm.core.jdk.Resources;

// default UseServiceLoaderFeature impl breaks the build so handle it manually, better to have includes/excludes
@AutomaticFeature
public class Feature_SPI implements Feature {

    @Override
    public void beforeAnalysis(final BeforeAnalysisAccess access) {
        forAllIncludedSpi().forEach(spi -> {
            final String marker = "META-INF/services/" + spi;
            final String impls = findImpls(marker);
            Resources.registerResource(marker, new ByteArrayInputStream(impls.getBytes(StandardCharsets.UTF_8)));
        });

    }

    private String findImpls(final String spi) {
        try {
            return list(Thread.currentThread().getContextClassLoader().getResources(spi)).stream()
                .flatMap(url -> {
                    try (final BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                        return reader.lines()
                                     .map(String::trim)
                                     .filter(it -> !it.isEmpty() && !it.startsWith("#"))
                                     .collect(toList()) // materialize before the stream is closed
                                     .stream();
                    } catch (final IOException e) {
                        throw new IllegalStateException(e);
                    }
                })
                .collect(joining("\n"));
        } catch (final IOException e) {
            return "";
        }
    }

    // todo: external config
    private Stream<String> forAllIncludedSpi() {
        return Stream.of(Commands.Loader.class.getName());
    }
}
