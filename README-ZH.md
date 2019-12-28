# Image2LaTeX

[English](https://github.com/blaisewang/img2latex-mathpix#readme) | 中文

[![CI Status](https://github.com/blaisewang/img2latex-mathpix/workflows/CI/badge.svg)](https://github.com/blaisewang/img2latex-mathpix/actions)
[![Releases](https://img.shields.io/github/v/release/blaisewang/img2latex-mathpix?include_prereleases)](https://github.com/blaisewang/img2latex-mathpix/releases)
[![Downloads](https://img.shields.io/github/downloads/blaisewang/img2latex-mathpix/total?color=orange)](https://github.com/blaisewang/img2latex-mathpix/releases)
[![License](https://img.shields.io/github/license/blaisewang/img2latex-mathpix)](https://github.com/blaisewang/img2latex-mathpix/blob/master/LICENSE)

[Snip](https://mathpix.com/) 是由 Mathpix 打造的一款出色的应用。
它可以帮助你提取文档中的公式及文本将其转换为LaTeX格式，并每月免费提供给所有用户 50 次识别。

在 [MathpixOCR](https://mathpix.com/ocr/) 以及 [Image2LaTeX](https://github.com/blaisewang/img2latex-mathpix/) 这款补充应用的帮助下，你可以每月免费进行 1000 次的 OCR 识别。

Image2LaTeX 是空闲时间的个人项目，仅保留了 Snip 的核心功能——将图片转换为特定 LaTeX 格式的公式或文本。
如果需要进阶功能，请每月 4.99 美元订阅 Snip。

## 功能

查看 MathpixOCR 网站 [fetures](https://mathpix.com/ocr#features) 章节来了解其 API 的功能。

### 新功能

- 适用于 macOS 的独立可执行应用程序.

## 使用要求

- [MathpixOCR 控制台](https://dashboard.mathpix.com/) 中的 API keys（与 Snip 不是同一个账号体系）。

- 如果你打算使用 `Image2LaTeX-x.x.x.jar` 请安装 JDK 11 或更高版本（推荐使用 [OpenJDK](https://openjdk.java.net/)。

## 运行

在程序第一次启动时输入你的 API keys（不含单引号）。

macOS 和 Windows 用户可以在系统托盘找到 `API Key` 选项来修改你的 API keys。

Linux 用户需要首先找到 `config` 文件，再通过文本编辑器进行修改。

**不要与其他人分享你的 `config` 文件。**
这款应用同样也不会将你的 API keys 发送给除 MathpixOCR API 服务器之外的任何人（查看 [OCRRequest.java](https://github.com/blaisewang/img2latex-mathpix/blob/master/src/main/java/OCRRequest.java) 了解你的 API keys 是如何被使用的）。

## 使用

### macOS

执行 `Image2LaTeX.app`。

### Windows

执行 `Image2LaTeX-windows/bin/` 目录下的 `Image2LaTeX.vbs`。

### Linux

执行 `Image2LaTeX-linux/bin/` 目录下的 `Image2LaTeX` 或者终端执行以下命令：

```
./Image2LaTeX
```

### JAR

如果安装了 JAR 启动器，双击 `Image2LaTeX-x.x.x.jar` 或者终端执行以下命令：

```
java -jar Image2LaTeX-x.x.x.jar
```

使用操作系统默认的方式（或者其他工具）对公式或文本进行截取（macOS 默认为 Shift (⇧)-Control (⌃)-Command (⌘)-4）后，应用会显示被截取的内容，按下 `Return` 或者 `Enter` 健进行 OCR 请求。
查看下面的[展示](#展示)章节了解使用流程。

## 展示

![demo](demo/demo.gif)

## 常见问题

### Windows 和 Linux 没有独立可执行程序提供？

2020 年 Java 14 GA 发布之后，会使用 [jpackage](https://jdk.java.net/jpackage/) 重新打包发布。

### 为什么有三种版本的程序可以下载？我应该下载哪一个？

`Image2LaTeX.app` 仅适用于macOS。

`Image2LaTeX-x.x.x-windows.zip` 和 `Image2LaTeX-x.x.x-linux.zip` 是由 [Badass Runtime Plugin](https://badass-runtime-plugin.beryx.org/releases/latest/) 编译的，体积庞大。

以上版本不需要安装 JDK 11 即可使用。

考虑到使用这类应用的用户群体，可能有很大一部分已经安装了 JDK ，所以发布了体积更小的 `Image2LaTeX-x.x.x.jar` 版本。

### 想要第一时间知道新版本的发布？

查看GitHub这篇关于关注和取消关注仓库的发行版的[文章](https://help.github.com/cn/github/receiving-notifications-about-activity-on-github/watching-and-unwatching-releases-for-a-repository)。

### 开发计划？

- 为特定 Linux 发行版添加系统托盘图标的支持。

## 问题

当出现错误对话框时，请先阅读 Mathpix OCR [API 文档](https://docs.mathpix.com/#error-id-types) 中关于 Error Types 章节。

### 还有问题 :thinking: ？

欢迎使用 [bug](https://github.com/blaisewang/img2latex-mathpix/labels/bug) 或者 [question](https://github.com/blaisewang/img2latex-mathpix/labels/question) 标签提出 [issue](https://github.com/blaisewang/img2latex-mathpix/issues) ， 但是非紧急问题的修复时间无法保证.

## 贡献

本项目欢迎各类贡献。
包括错误修正、新功能、代码格式指导等的 [Pull Requests](https://github.com/blaisewang/img2latex-mathpix/pulls) 将会尽快被审核。

## 开源许可证

[Apache License 2.0](https://github.com/blaisewang/img2latex-mathpix/blob/master/LICENSE)。
