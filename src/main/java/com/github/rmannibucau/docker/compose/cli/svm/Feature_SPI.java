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

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import org.graalvm.nativeimage.Feature;
import org.tomitribe.crest.cmds.processors.Commands;

import com.github.rmannibucau.docker.compose.cli.loader.DocoshLoader;
import com.oracle.svm.core.annotate.AutomaticFeature;
import com.oracle.svm.core.jdk.Resources;

// default UseServiceLoaderFeature impl breaks the build so handle it manually
// todo: it is generalizable at build time easily, idea is to keep the JVM excluded
@AutomaticFeature
public class Feature_SPI implements Feature {

    @Override
    public void beforeAnalysis(final BeforeAnalysisAccess access) {
        Resources.registerResource("META-INF/services/" + Commands.Loader.class.getName(),
                new ByteArrayInputStream(DocoshLoader.class.getName().getBytes(StandardCharsets.UTF_8)));

    }
}
