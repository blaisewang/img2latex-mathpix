APP_PATH=./Image2LaTeX.app/Contents

cd ../build/libs || exit

mkdir -p $APP_PATH $APP_PATH/MacOS $APP_PATH/Resources

cp ../../macOS/Info.plist.template $APP_PATH/Info.plist

cp ../../macOS/AppIcon.icns $APP_PATH/Resources/AppIcon.icns

FILENAME="$(ls *macos.zip)"

unzip -q "$FILENAME"

rm -f "$FILENAME"

mv ./Image2LaTeX-macos/* $APP_PATH/MacOS

rm -rf ./Image2LaTeX-macos

VERSION="$(echo "$FILENAME" | cut -d'-' -f2)"

sed -i "" "s/0.0.0/$VERSION/g" ./Image2LaTeX.app/Contents/Info.plist
