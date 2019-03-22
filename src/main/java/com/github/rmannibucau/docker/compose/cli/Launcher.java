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
package com.github.rmannibucau.docker.compose.cli;

import static java.util.Collections.singletonMap;

import java.util.Map;

import org.tomitribe.crest.Main;
import org.tomitribe.crest.environments.SystemEnvironment;

import com.github.rmannibucau.docker.compose.cli.service.DockerComposeParser;

public class Launcher {

    private Launcher() {
        // no-op
    }

    public static void main(final String... args) throws Exception {
        final Map<Class<?>, Object> services = singletonMap(DockerComposeParser.class, new DockerComposeParser());
        final SystemEnvironment environment = new SystemEnvironment(services);
        // Environment.ENVIRONMENT_THREAD_LOCAL.set(environment); // this makes it work
        new Main().main(environment, args);
    }
}
