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

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DockerComposeParser {

    public Collection<Image> extractImages(final Path path) {
        if (path == null || !Files.exists(path)) {
            throw new IllegalArgumentException("No file: " + path);
        }
        final Collection<Image> images = new ArrayList<>();
        try {
            String service = null;
            final List<String> lines = Files.readAllLines(path);
            for (final String line : lines) {
                final String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }
                if (line.endsWith(":") && line.indexOf(trimmed) == 2) {
                    service = line.substring(2, line.length() - 1);
                } else if (line.startsWith("    image:")) {
                    images.add(new Image(
                            requireNonNull(service, "No service for " + trimmed),
                            line.substring("    image:".length()).trim()));
                }
            }
        } catch (final IOException e) {
            throw new IllegalArgumentException(e);
        }
        return images;
    }

    public static class Image {
        private final String service;
        private final String image;

        private Image(final String service, final String image) {
            this.service = service;
            this.image = image;
        }

        public String getService() {
            return service;
        }

        public String getImage() {
            return image;
        }

        @Override
        public String toString() {
            return service + " (" + image + ')';
        }
    }
}
