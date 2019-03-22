/**
 * Copyright (C) 2019 Romain Manni-Bucau
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
package com.github.rmannibucau.docker.compose.cli.command;

import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Collection;

import org.tomitribe.crest.api.Command;
import org.tomitribe.crest.api.Option;
import org.tomitribe.crest.api.Out;

import com.github.rmannibucau.docker.compose.cli.service.DockerComposeParser;

public class Docosh {
    @Command("list-images")
    public void listImages(@Out final PrintStream stdout,
                           @Option("docker-compose") final Path compose,
                           final DockerComposeParser parser) {
        final Collection<String> images = parser.extractImages(compose);
        images.forEach(stdout::println);
    }
}
