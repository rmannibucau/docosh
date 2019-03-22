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
package com.github.rmannibucau.docker.compose.cli.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DockerComposeParser {

    public Collection<String> extractImages(final Path path) {
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("No file: " + path);
        }
        final Collection<String> images = new ArrayList<>();
        try {
            final List<String> lines = Files.readAllLines(path);
            for (final String line : lines) {
                String value = line.trim();
                if (value.startsWith("image:")) {
                    images.add(value.substring("image:".length()).trim());
                }
            }
        } catch (final IOException e) {
            throw new IllegalArgumentException(e);
        }
        return images;
    }
}
