#!/usr/bin/env bash
cd "$(dirname "$0")" || exit
BASE_PATH=$(pwd)
BUILD_PATH=../all/build

# Make Repository
cd "$BASE_PATH" || exit
mkdir -p $BUILD_PATH/cocoapods/repository/debug
mkdir -p $BUILD_PATH/cocoapods/repository/release

# Copy Podspec
cd "$BASE_PATH" || exit
cd $BUILD_PATH/cocoapods/publish/debug || exit
cp ksaypip.podspec ../../repository/ksaypip-debug.podspec
cd ../../repository/ || exit
sed -i -e "s|'ksaypip'|'ksaypip-debug'|g" ksaypip-debug.podspec
sed -i -e "s|'ksaypip.xcframework'|'debug/ksaypip.xcframework'|g" ksaypip-debug.podspec
rm *.podspec-e
cd "$BASE_PATH" || exit
cd $BUILD_PATH/cocoapods/publish/release || exit
cp ksaypip.podspec ../../repository/ksaypip-release.podspec
cd ../../repository/ || exit
sed -i -e "s|'ksaypip'|'ksaypip-release'|g" ksaypip-release.podspec
sed -i -e "s|'ksaypip.xcframework'|'release/ksaypip.xcframework'|g" ksaypip-release.podspec
rm *.podspec-e

# Copy Framework
cd "$BASE_PATH" || exit
cd $BUILD_PATH/cocoapods/publish/debug || exit
cp -r ksaypip.xcframework ../../repository/debug/ksaypip.xcframework
cd "$BASE_PATH" || exit
cd $BUILD_PATH/cocoapods/publish/release || exit
cp -r ksaypip.xcframework ../../repository/release/ksaypip.xcframework

# Copy README
cd "$BASE_PATH" || exit
cd ../ || exit
cp ./LICENSE ./all/build/cocoapods/repository/LICENSE
cp ./docs/pods/README.md ./all/build/cocoapods/repository/README.md
cp ./docs/pods/README_ja.md ./all/build/cocoapods/repository/README_ja.md
