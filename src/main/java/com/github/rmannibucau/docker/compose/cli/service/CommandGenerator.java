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

import java.util.stream.Stream;

public class CommandGenerator {

    public Stream<String> generateCommands(final String service,
                                           final String image,
                                           final String imageName) {
        return Stream.of(image)
                .flatMap(it -> Stream.of(
                        "alias log_" + service + "='" + log(imageName) + "'",
                        "alias tail_" + service + "='" + tail(imageName) + "'",
                        "alias sh_" + service + "='" + sh(imageName) + "'"
                ));
    }

    private String tail(final String imageName) {
        return "docker logs --tail 100 -f " + imageName;
    }

    private String log(final String imageName) {
        return "docker logs " + imageName;
    }

    private String sh(final String imageName) {
        return "docker exec -it " + imageName + " sh";
    }
}
