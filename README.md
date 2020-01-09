# Image2LaTeX

English | [中文](https://github.com/blaisewang/img2latex-mathpix/blob/master/README-ZH.md#readme)

[![Build Status](https://travis-ci.com/blaisewang/img2latex-mathpix.svg?branch=master)](https://travis-ci.com/blaisewang/img2latex-mathpix)
[![Releases](https://img.shields.io/github/v/release/blaisewang/img2latex-mathpix?include_prereleases)](https://github.com/blaisewang/img2latex-mathpix/releases)
[![Downloads](https://img.shields.io/github/downloads/blaisewang/img2latex-mathpix/total?color=orange)](https://github.com/blaisewang/img2latex-mathpix/releases)
[![License](https://img.shields.io/github/license/blaisewang/img2latex-mathpix)](https://github.com/blaisewang/img2latex-mathpix/blob/master/LICENSE)

[Snip](https://mathpix.com/) is an amazing app built by Mathpix to help you extract LaTeX (also text) from documents.
It gives you the first 50 snips for free every single month.
With the help of [MathpixOCR](https://mathpix.com/ocr/) and this supplementary app, you can now make up to 1000 OCR requests per month for free.

[Image2LaTeX](https://github.com/blaisewang/img2latex-mathpix/) is a personal side project that keeps only core functions of the Snip such as convert images to certain LaTeX equation formats and OCR.
For other advanced functions and unlimited snips, subscribe to the Snip for $4.99 per month.

## Features

See the [features](https://mathpix.com/ocr#features) section on MathpixOCR website.

### New Featurs

- Self-contained executable application for macOS.

## Requirements

- Your API keys in your [MathpixOCR dashboard](https://dashboard.mathpix.com/) (different from the Mathpix account used in the Snip app)

- JDK 11 or higher version ([OpenJDK](https://openjdk.java.net/) is highly recommended) if you plan to use `Image2LaTeX-x.x.x.jar`

## Run

Enter your API keys (without single quotes) at the first launch of this app.

To change your API keys, macOS and Windows users will find a menu item called `API Key` by clicking this app's menu bar icon.

Linux users need to change it manually by finding the `config` file and edit it by text editor.

**Do not share your `config` file with others.**
This app will also not send your API keys to others except for MathpixOCR API server (see [OCRRequest.java](https://github.com/blaisewang/img2latex-mathpix/blob/master/src/main/java/OCRRequest.java) to learn how your API keys are used).

## Usage

### macOS

Run `Image2LaTeX.app`.

### Windows

Run `Image2LaTeX.vbs` at `Image2LaTeX-windows/`.

### Linux

Run `Image2LaTeX` at `Image2LaTeX-linux/bin/` or with:

```
./Image2LaTeX
```

### JAR

With a JAR launcher installed, run `Image2LaTeX-x.x.x.jar` or with:

```
java -jar Image2LaTeX-x.x.x.jar
```

Use your operating system's default methods (or other tools) to take a screenshot of equations or text (Shift (⇧)-Control (⌃)-Command (⌘)-4 on macOS by default).

The Image2LaTex app will display the image you captured.
Press the `Return` or `Enter` key to send the OCR request.
See [demo](#Demo) section below.

## Demo

![demo](demo/demo.gif)

## FQA

### No native self-contained executable application provided for Widnows or Linux?

Not yet. But, with the GA release of Java 14 in 2020, [jpackage](https://jdk.java.net/jpackage/) should be available for packaging self-contained JavaFX application.
At that time, I will drop the release of `.jar` and `.zip` files and no one (except me) needs to install JDK anymore.

### Why three types of the application released? Which one I should download?

The `Image2LaTeX.app` is for macOS only.

The `Image2LaTeX-x.x.x-windows.zip` 和 `Image2LaTeX-x.x.x-linux.zip` are built with the [Badass Runtime Plugin](https://badass-runtime-plugin.beryx.org/releases/latest/) which has a huge size.

You don't need to have JDK 11 installed for running these versions.

Considering the people using similar apps, I assumed that a large percentage of users should have JDK installed.
Therefore, a smaller size Fat JAR `Image2LaTeX-x.x.x.jar` has also released.

### Want to get updates on new releases ASAP?

See this [article](https://help.github.com/en/github/receiving-notifications-about-activity-on-github/watching-and-unwatching-releases-for-a-repository) about watching and unwatching releases for a repository by GitHub.

### Any plans?

- Add system tray icon support for some Linux distributions.

## Issues

Please first refer to the official [API Docs](https://docs.mathpix.com/#error-id-types) about error types if an error dialogue is displayed.

### Still have problems :thinking: ?

Welcome to open an [issue](https://github.com/blaisewang/img2latex-mathpix/issues) with the [bug](https://github.com/blaisewang/img2latex-mathpix/labels/bug) or [question](https://github.com/blaisewang/img2latex-mathpix/labels/question) label, but the time to fix non-vital bugs may not be guaranteed.

## Contributions

Contributions are highly welcomed.
Suggestions can be made through opening an [issue](https://github.com/blaisewang/img2latex-mathpix/issues) with [enhancement](https://github.com/blaisewang/img2latex-mathpix/labels/enhancement) label.
[Pull Requests](https://github.com/blaisewang/img2latex-mathpix/pulls) including bug fixes, new features, code style guidance, etc., will be reviewed as soon as possible.

## Contributors ✨

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->

<table>
  <tr>
    <td align="center"><a href="https://nyxflower.github.io/"><img src="https://avatars1.githubusercontent.com/u/38955723?v=4" width="100px;" alt=""/><br /><sub><b>Hao Xu</b></sub></a><br /><a href="#ideas-NYXFLOWER" title="Ideas, Planning, & Feedback">🤔</a></td>
    <td align="center"><a href="http://blog.gongyan.me"><img src="https://avatars0.githubusercontent.com/u/14838533?v=4" width="100px;" alt=""/><br /><sub><b>龚焱</b></sub></a><br /><a href="#ideas-gongyan200" title="Ideas, Planning, & Feedback">🤔</a> <a href="#tutorial-gongyan200" title="Tutorials">✅</a></td>
  </tr>
</table>

<!-- markdownlint-enable -->
<!-- prettier-ignore-end -->
<!-- ALL-CONTRIBUTORS-LIST:END -->

## License

This code is distributed under the terms of the [Apache License 2.0](https://github.com/blaisewang/img2latex-mathpix/blob/master/LICENSE).
