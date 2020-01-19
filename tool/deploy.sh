#!/usr/bin/env bash

JDK_FOLDER=jdk-11.0.5+10/

wget -nc -O OpenJDK_linux.tar.gz https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.5%2B10/OpenJDK11U-jdk_x64_linux_hotspot_11.0.5_10.tar.gz
wget -nc -O OpenJDK_macos.tar.gz https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.5%2B10/OpenJDK11U-jdk_x64_mac_hotspot_11.0.5_10.tar.gz
wget -nc -O OpenJDK_windows.zip https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.5%2B10/OpenJDK11U-jdk_x64_windows_hotspot_11.0.5_10.zip

mkdir -p jdk

if [ ! -d "jdk/osx-jdk.jdk/" ]; then
  tar -zxf OpenJDK_macos.tar.gz && mv $JDK_FOLDER jdk/osx-jdk.jdk/
fi

if [ ! -d "jdk/linux-jdk/" ]; then
  tar -zxf OpenJDK_linux.tar.gz && mv $JDK_FOLDER jdk/linux-jdk/
fi

if [ ! -d "jdk/windows-jdk/" ]; then
  unzip -q OpenJDK_windows.zip && mv $JDK_FOLDER jdk/windows-jdk/
fi
