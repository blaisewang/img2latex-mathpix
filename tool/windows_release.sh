#!/usr/bin/env bash

cd build/libs || exit

FILENAME="$(ls ./*windows.zip)"

unzip -q "$FILENAME"

rm -f "$FILENAME"

printf 'set ws=WScript.CreateObject("WScript.Shell")\r\nws.Run "bin\Image2LaTeX.bat",hide' >./Image2LaTeX-windows/Image2LaTeX.vbs

zip -r -q "$FILENAME" ./Image2LaTeX-windows

rm -rf ./Image2LaTeX-windows
