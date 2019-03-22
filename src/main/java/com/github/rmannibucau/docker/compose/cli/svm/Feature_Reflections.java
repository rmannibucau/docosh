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

import java.lang.reflect.Executable;
import java.util.stream.Stream;

import org.graalvm.nativeimage.Feature;
import org.graalvm.nativeimage.RuntimeReflection;
import org.tomitribe.crest.cmds.processors.Help;
import org.tomitribe.crest.cmds.processors.OptionParam;
import org.tomitribe.crest.cmds.targets.SimpleBean;

import com.github.rmannibucau.docker.compose.cli.command.Docosh;
import com.github.rmannibucau.docker.compose.cli.editor.GlobalEditors;
import com.github.rmannibucau.docker.compose.cli.editor.PathEditor;
import com.github.rmannibucau.docker.compose.cli.loader.DocoshLoader;
import com.github.rmannibucau.docker.compose.cli.service.DockerComposeParser;
import com.oracle.svm.core.annotate.AutomaticFeature;

// register reflection meta we need for this app
@AutomaticFeature
public class Feature_Reflections implements Feature {

    @Override
    public void beforeAnalysis(final BeforeAnalysisAccess access) {
        // todo: it is at build time so we can use an AnnotationFinder
        try {
            Stream.of(
                    // app
                    Docosh.class, DocoshLoader.class, GlobalEditors.class, PathEditor.class, DockerComposeParser.class,
                    // crest
                    Help.class, SimpleBean.class, OptionParam.class,
                    SimpleBean.class.getClassLoader().loadClass("org.tomitribe.crest.cmds.CmdMethod$ComplexParam"))
                  .forEach(this::registerApi);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    private void registerApi(final Class<?> clazz) {
        RuntimeReflection.register(clazz);
        try {
            RuntimeReflection.registerForReflectiveInstantiation(clazz);
        } catch (final IllegalArgumentException iae) {
            // no-op
        }
        RuntimeReflection.register(Stream.of(clazz.getMethods()).toArray(Executable[]::new));
    }
}
