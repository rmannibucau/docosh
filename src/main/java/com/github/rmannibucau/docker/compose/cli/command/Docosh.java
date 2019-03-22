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

import static java.util.stream.Collectors.joining;

import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Stream;

import org.tomitribe.crest.api.Command;
import org.tomitribe.crest.api.Default;
import org.tomitribe.crest.api.Option;
import org.tomitribe.crest.api.Out;

import com.github.rmannibucau.docker.compose.cli.service.CommandGenerator;
import com.github.rmannibucau.docker.compose.cli.service.DockerComposeParser;

public class Docosh {
    @Command("list-images")
    public void listImages(@Out final PrintStream stdout,
                           @Option("docker-compose") final Path compose,
                           final DockerComposeParser parser) {
        parser.extractImages(compose).forEach(stdout::println);
    }

    @Command("generate-sh")
    public void generateSource(@Out final PrintStream stdout,
                               @Option("docker-compose") final Path compose,
                               @Option("use-weave") @Default("true") final boolean weave,
                               @Option("weave-index") @Default("1") final int weaveIndex,
                               final DockerComposeParser parser,
                               final CommandGenerator generator) {
        if (!weave) {
            throw new IllegalArgumentException("Not using weave is not yet supported");
        }
        final String weaveNamePattern = compose.getParent().getFileName().toString() + "_%s_" + weaveIndex;
        final Collection<DockerComposeParser.Image> images = parser.extractImages(compose);
        Stream.concat(
                    images
                      .stream()
                      .flatMap(image -> generator.generateCommands(image.getService(), image.getImage(), String.format(weaveNamePattern, image))),
                    Stream.of(
                            list("services", images, DockerComposeParser.Image::getService),
                            list("images", images, DockerComposeParser.Image::getImage)))
              .forEach(stdout::println);
    }

    private String list(final String what, final Collection<DockerComposeParser.Image> images,
                        final Function<DockerComposeParser.Image, String> extractor) {
        return "alias list_" + what + "='for i in " +
                images.stream().map(extractor).distinct().sorted().collect(joining(" ")) +
                "; do echo $i; done'";
    }
}
