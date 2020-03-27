#!/usr/bin/env bash

if [[ ! -f build.gradle ]]; then
  cd ../releases || exit
else
  cd ./releases || exit
fi

FILENAME="$(echo ./*windows)"

cp -r ../jdk/windows/jre ./"$FILENAME"/jre

VERSION="$(echo "$FILENAME" | cut -d'-' -f2)"

mv "$FILENAME" ./Image2LaTeX-windows

zip -r -q Image2LaTeX-"$VERSION"-windows.zip ./Image2LaTeX-windows

rm -rf ./Image2LaTeX-windows
