#!/usr/bin/env bash

if [[ ! -f build.gradle ]]; then
  JDK_PARENT_PATH=../jdk
else
  JDK_PARENT_PATH=./jdk
fi

LINUX_JDK_PATH=linux-jdk
MACOS_JDK_PATH=osx-jdk.jdk
WINDOWS_JDK_PATH=windows-jdk

LINUX_JDK_FILE=./OpenJDK_linux.tar.gz
MACOS_JDK_FILE=./OpenJDK_macos.tar.gz
WINDOWS_JDK_FILE=./OpenJDK_windows.zip

mkdir -p $JDK_PARENT_PATH/$LINUX_JDK_PATH $JDK_PARENT_PATH/$MACOS_JDK_PATH $JDK_PARENT_PATH/$WINDOWS_JDK_PATH

wget -nc -O $LINUX_JDK_FILE https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.6%2B10/OpenJDK11U-jdk_x64_linux_hotspot_11.0.6_10.tar.gz
wget -nc -O $MACOS_JDK_FILE https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.6%2B10/OpenJDK11U-jdk_x64_mac_hotspot_11.0.6_10.tar.gz
wget -nc -O $WINDOWS_JDK_FILE https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.6%2B10/OpenJDK11U-jdk_x64_windows_hotspot_11.0.6_10.zip

bsdtar -zxf $LINUX_JDK_FILE -C $JDK_PARENT_PATH/$LINUX_JDK_PATH --strip 1 && rm $LINUX_JDK_FILE
bsdtar -zxf $MACOS_JDK_FILE -C $JDK_PARENT_PATH/$MACOS_JDK_PATH --strip 1 && rm $MACOS_JDK_FILE
bsdtar -zxf $WINDOWS_JDK_FILE -C $JDK_PARENT_PATH/$WINDOWS_JDK_PATH --strip 1 && rm $WINDOWS_JDK_FILE
