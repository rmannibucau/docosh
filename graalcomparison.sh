#! /bin/bash

#
# WARNING: before running that script, ensure to create a dependency/ folder with the following libs (you can use mvn dependency-copy-dependencies to get them):
# $ ls dependency/
# docosh-1.0-SNAPSHOT.jar  tomitribe-crest-0.10.jar  tomitribe-crest-api-0.10.jar  tomitribe-util-1.0.0.jar  xbean-asm7-shaded-4.12.jar  xbean-finder-shaded-4.12.jar
#

for i in {1..10}; do

echo "Oracle 1.8u191"
time ~/softwares/jdk1.8.0_191/bin/java -cp "dependency/*" com.github.rmannibucau.docker.compose.cli.Launcher &> /dev/null
echo
echo

echo "Zulu 1.8u202"
time ~/.sdkman/candidates/java/8.0.202-zulufx/bin/java -cp "dependency/*" com.github.rmannibucau.docker.compose.cli.Launcher &> /dev/null
echo
echo

echo "Graal 14"
time ~/softwares/graalvm-ce-1.0.0-rc14/bin/java -cp "dependency/*" com.github.rmannibucau.docker.compose.cli.Launcher  &> /dev/null
echo
echo

echo "Graal CE 15"
time ~/softwares/graalvm-ce-1.0.0-rc15/bin/java -XX:-UseJVMCINativeLibrary -cp "dependency/*" com.github.rmannibucau.docker.compose.cli.Launcher &> /dev/null
echo
echo

echo "Graal EE 15"
time ~/softwares/graalvm-ee-1.0.0-rc15/bin/java -XX:-UseJVMCINativeLibrary -cp "dependency/*" com.github.rmannibucau.docker.compose.cli.Launcher &> /dev/null
echo
echo

done

