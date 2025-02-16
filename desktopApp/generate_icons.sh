#!/bin/bash

# Check for dependencies
if ! command -v convert &> /dev/null || ! command -v iconutil &> /dev/null; then
    echo "Error: ImageMagick and iconutil (for macOS) are required."
    exit 1
fi

# Check for input file
if [ -z "$1" ]; then
    echo "Usage: $0 <input-png>"
    exit 1
fi

INPUT_PNG="$1"

echo "Generating macOS .icns..."
OUTPUT_DIR="nativeDistributions/macOS"
mkdir -p "$OUTPUT_DIR/mac.iconset"
magick convert "$INPUT_PNG" -resize 16x16  "$OUTPUT_DIR/mac.iconset/icon_16x16.png"
magick convert "$INPUT_PNG" -resize 32x32  "$OUTPUT_DIR/mac.iconset/icon_16x16@2x.png"
magick convert "$INPUT_PNG" -resize 32x32  "$OUTPUT_DIR/mac.iconset/icon_32x32.png"
magick convert "$INPUT_PNG" -resize 64x64  "$OUTPUT_DIR/mac.iconset/icon_32x32@2x.png"
magick convert "$INPUT_PNG" -resize 128x128 "$OUTPUT_DIR/mac.iconset/icon_128x128.png"
magick convert "$INPUT_PNG" -resize 256x256 "$OUTPUT_DIR/mac.iconset/icon_128x128@2x.png"
magick convert "$INPUT_PNG" -resize 256x256 "$OUTPUT_DIR/mac.iconset/icon_256x256.png"
magick convert "$INPUT_PNG" -resize 512x512 "$OUTPUT_DIR/mac.iconset/icon_256x256@2x.png"
magick convert "$INPUT_PNG" -resize 512x512 "$OUTPUT_DIR/mac.iconset/icon_512x512.png"
magick convert "$INPUT_PNG" -resize 1024x1024 "$OUTPUT_DIR/mac.iconset/icon_512x512@2x.png"
iconutil -c icns -o "$OUTPUT_DIR/icon.icns" "$OUTPUT_DIR/mac.iconset"
rm -r "$OUTPUT_DIR/mac.iconset"

echo "Generating Windows .ico..."
OUTPUT_DIR="nativeDistributions/windows"
magick convert "$INPUT_PNG" -resize 16x16  "$OUTPUT_DIR/icon-16.png"
magick convert "$INPUT_PNG" -resize 32x32  "$OUTPUT_DIR/icon-32.png"
magick convert "$INPUT_PNG" -resize 48x48  "$OUTPUT_DIR/icon-48.png"
magick convert "$INPUT_PNG" -resize 64x64  "$OUTPUT_DIR/icon-64.png"
magick convert "$INPUT_PNG" -resize 128x128 "$OUTPUT_DIR/icon-128.png"
magick convert "$INPUT_PNG" -resize 256x256 "$OUTPUT_DIR/icon-256.png"
magick convert "$OUTPUT_DIR/icon-16.png" "$OUTPUT_DIR/icon-32.png" "$OUTPUT_DIR/icon-48.png" "$OUTPUT_DIR/icon-64.png" "$OUTPUT_DIR/icon-128.png" "$OUTPUT_DIR/icon-256.png" "$OUTPUT_DIR/icon.ico"
rm "$OUTPUT_DIR"/icon-*.png

echo "Generating Linux .png..."
OUTPUT_DIR="nativeDistributions/linux"
convert "$INPUT_PNG" -resize 512x512 "$OUTPUT_DIR/icon.png"

echo "Icons generated"