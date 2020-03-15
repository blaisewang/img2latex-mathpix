# Image2LaTeX

[English](https://github.com/blaisewang/img2latex-mathpix#readme) | 中文

[![Codacy Badge](https://img.shields.io/codacy/grade/89cd8aa01451404f8d782ead0b4d153d?logo=codacy&style=flat-square)](https://www.codacy.com/manual/blaisewang/img2latex-mathpix)
[![Build Status](https://img.shields.io/travis/com/blaisewang/img2latex-mathpix/master?logo=travis&style=flat-square)](https://travis-ci.com/blaisewang/img2latex-mathpix)
[![Releases](https://img.shields.io/github/v/release/blaisewang/img2latex-mathpix?include_prereleases&style=flat-square)](https://github.com/blaisewang/img2latex-mathpix/releases)
[![Downloads](https://img.shields.io/github/downloads/blaisewang/img2latex-mathpix/total?color=orange&style=flat-square)](https://github.com/blaisewang/img2latex-mathpix/releases)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?color=ff69b4&style=flat-square)](http://makeapullrequest.com)
[![License](https://img.shields.io/github/license/blaisewang/img2latex-mathpix?style=flat-square)](https://github.com/blaisewang/img2latex-mathpix/blob/master/LICENSE)

[Snip](https://mathpix.com/) 是由 Mathpix 打造的一款出色的应用。
它可以帮助你提取文档中的公式及文本将其转换为LaTeX格式，并每月免费提供给所有用户 50 次识别。

在 [MathpixOCR](https://mathpix.com/ocr/) 以及 [Image2LaTeX](https://github.com/blaisewang/img2latex-mathpix/) 这款补充应用的帮助下，你可以每月免费进行 1000 次的 OCR 识别。

Image2LaTeX 是空闲时间的个人项目，仅保留了 Snip 的核心功能——将图片转换为特定 LaTeX 格式的公式或文本。
如果需要进阶功能，请每月 4.99 美元订阅 Snip。

## 功能

查看 MathpixOCR 网站 [fetures](https://mathpix.com/ocr#features) 章节来了解其 API 的功能。

### 新功能

- 新 `Improved OCR` 选项支持[多选](https://mathpix.com/blog/mathpix-text-endpoint)、[段落](https://mathpix.com/blog/snip-2.0)、[基础表格](https://mathpix.com/blog/v1-table-recognition)的识别。
- 支持使用 HTTP 代理发出 OCR 请求。

## 使用要求

- [MathpixOCR 控制台](https://dashboard.mathpix.com/) 中的 API keys（与 Snip 不是同一个账号体系）。
- 如果你打算使用 `Image2LaTeX-x.x.x.jar` 请安装 JDK 11 或更高版本（推荐使用 [OpenJDK](https://openjdk.java.net/)。

## 运行

在程序第一次启动时输入你的 API keys（不含单引号）。

macOS 和 Windows 用户可以在系统托盘找到 `Preferences` 菜单项中的 `API Credentials` 来修改你的 API keys。

Linux 用户可以通过在应用内任意位置右键单击的方式打开 `Preferences` 对话框。

**注意，**，应用不会将你的 API keys 发送给除 MathpixOCR API 服务器之外的任何人（查看 [OCRRequestHelper.java](https://github.com/blaisewang/img2latex-mathpix/blob/master/src/main/java/io/OCRRequestHelper.java) 了解你的 API keys 是如何被使用的）。

## 使用

### macOS

执行 `Image2LaTeX.app`。

### Windows

执行 `Image2LaTeX-windows/` 目录下的 `Image2LaTeX.vbs`。

### Linux

执行 `Image2LaTeX-linux/bin/` 目录下的 `Image2LaTeX` 或者终端执行以下命令：

```bash
./Image2LaTeX
```

### JAR

如果安装了 JAR 启动器，双击 `Image2LaTeX-x.x.x.jar` 或者终端执行以下命令：

```bash
java -jar Image2LaTeX-x.x.x.jar
```

使用操作系统默认的方式（或者其他工具）对公式或文本进行截取（macOS 默认为 Shift (⇧)-Control (⌃)-Command (⌘)-4）后，
按下`空格`键预览截图，或者直接按下`回车`键发送 OCR 请求。

查看下面的[展示](#展示)章节了解使用流程。

## 展示

![demo](demo/demo.gif)

## 常见问题

### Windows 和 Linux 平台的独立可执行程序

2020 年 Java 14 GA 发布之后，会使用 [jpackage](https://jdk.java.net/jpackage/) 重新打包发布。

### `*.jar` 和 `*.zip` 版本的区别

`*.zip` 版本不需要安装 JDK 11 就可使用。

### 第一时间获取新版本的发布

查看GitHub这篇关于关注和取消关注仓库的发行版的[文章](https://help.github.com/cn/github/receiving-notifications-about-activity-on-github/watching-and-unwatching-releases-for-a-repository)。

## 问题

当遇到错误时，请首先阅读 Mathpix OCR [API 文档](https://docs.mathpix.com/#error-id-types) 中关于 Error Types 章节以及[已报告的问题](https://github.com/blaisewang/img2latex-mathpix/issues)。

### 遇到了尚未报告的问题 🤔

欢迎使用 [bug](https://github.com/blaisewang/img2latex-mathpix/labels/bug) 或者 [question](https://github.com/blaisewang/img2latex-mathpix/labels/question) 标签提出 [issue](https://github.com/blaisewang/img2latex-mathpix/issues) ， 但是非紧急问题的修复时间无法保证.

## 贡献

本项目欢迎各类贡献。
包括错误修正、新功能、代码格式指导等的 [Pull Requests](https://github.com/blaisewang/img2latex-mathpix/pulls) 将会尽快被审核。

## 贡献者 ✨

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tr>
    <td align="center"><a href="https://nyxflower.github.io/"><img src="https://avatars1.githubusercontent.com/u/38955723?v=4" width="100px;" alt=""/><br /><sub><b>Hao Xu</b></sub></a><br />🤔</td>
    <td align="center"><a href="http://blog.gongyan.me"><img src="https://avatars0.githubusercontent.com/u/14838533?v=4" width="100px;" alt=""/><br /><sub><b>龚焱</b></sub></a><br />🤔 ✅</td>
    <td align="center"><a href="https://github.com/wtbsw"><img src="https://avatars1.githubusercontent.com/u/14332504?v=4" width="100px;" alt=""/><br /><sub><b>BOB</b></sub></a><br />🐛 ⚠️</td>
    <td align="center"><a href="https://github.com/YuiTH"><img src="https://avatars1.githubusercontent.com/u/22416062?v=4" width="100px;" alt=""/><br /><sub><b>YuiTH</b></sub></a><br />🐛</td>
  </tr>
</table>

<!-- markdownlint-enable -->
<!-- prettier-ignore-end -->
<!-- ALL-CONTRIBUTORS-LIST:END -->

## 开源许可证

[Apache License 2.0](https://github.com/blaisewang/img2latex-mathpix/blob/master/LICENSE)。
