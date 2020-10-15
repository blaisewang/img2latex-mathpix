#!/usr/bin/env bash

if [[ ! -f build.gradle ]]; then
  JDK_PARENT_PATH=../jdk
else
  JDK_PARENT_PATH=./jdk
fi

LINUX_JDK_PATH=linux-jdk
MACOS_JDK_PATH=osx-jdk.jdk
WINDOWS_JRE_PATH=windows/jre

LINUX_JDK_FILE=./linux_jdk.tar.gz
MACOS_JDK_FILE=./macos_jdk.tar.gz
WINDOWS_JRE_FILE=./windows_jre.zip

mkdir -p $JDK_PARENT_PATH/$LINUX_JDK_PATH $JDK_PARENT_PATH/$MACOS_JDK_PATH $JDK_PARENT_PATH/$WINDOWS_JRE_PATH

wget -nc -O $LINUX_JDK_FILE https://github.com/AdoptOpenJDK/openjdk15-binaries/releases/download/jdk-15%2B36/OpenJDK15U-jdk_x64_linux_hotspot_15_36.tar.gz
wget -nc -O $MACOS_JDK_FILE https://github.com/AdoptOpenJDK/openjdk15-binaries/releases/download/jdk-15%2B36/OpenJDK15U-jdk_x64_mac_hotspot_15_36.tar.gz
wget -nc -O $WINDOWS_JRE_FILE https://github.com/AdoptOpenJDK/openjdk15-binaries/releases/download/jdk-15%2B36/OpenJDK15U-jre_x64_windows_hotspot_15_36.zip

bsdtar -zxf $LINUX_JDK_FILE -C $JDK_PARENT_PATH/$LINUX_JDK_PATH --strip 1 && rm $LINUX_JDK_FILE
bsdtar -zxf $MACOS_JDK_FILE -C $JDK_PARENT_PATH/$MACOS_JDK_PATH --strip 1 && rm $MACOS_JDK_FILE
bsdtar -zxf $WINDOWS_JRE_FILE -C $JDK_PARENT_PATH/$WINDOWS_JRE_PATH --strip 1 && rm $WINDOWS_JRE_FILE
