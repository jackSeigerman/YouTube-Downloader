# Youtube-Downloader <img src="assets/YoutubeDownloaderLogo.png" width="120px" alt="YoutubeDownloaderLogo" align="right">

![Static Badge](https://img.shields.io/badge/Java-f89820)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
![GitHub Release](https://img.shields.io/github/v/release/jackSeigerman/Youtube-Downloader)


A program to download videos from YouTube as an Mp4 or Mp3. You can change the directory that the video or audio downloads to. 

![Canvas](assets/YoutubeDownloader.gif)

## Table of contents
- [Features](#features)
- [Installation](#installation)
- [User Guide](#user-guide)
- [License](#license)

## Features

### Directory saving
This Youtube Downloader saves your download directory prefrence through sessions, so you dont have to remeber to set the directory you want to download to each time you open the downloader.

![Canvas](assets/YoutubeDownloaderDirectory.gif)

### Compatibilty with Premiere pro and Davinci Resolve
Many downloaders have issues with the codec used in refrence to Adobe Premiere pro and Davinci Resolve. This downloader downloads videos as Mp4's in a codec that both Premiere and Davinci Resolve supports.

![Canvas](assets/Downloading.gif)

### Mp4 and Mp3 Downloadabilty
Download any video on Youtube as either a video or just an audio. Both are the highest quality available for the video in question with no compromises. 

![Canvas](assets/Gallery.gif)

## Installation

*READ ENTIRELY BEFORE BEGGINING ANY STEPS*

---

### *Windows*

1. A JDK of at least version 23 is needed to run either the jar or exe. click the [Download](https://download.oracle.com/java/23/latest/jdk-23_windows-x64_bin.exe) button to download the JDK if you dont already have it.

2. If you just downloaded the jdk exe listed above, run the exe and follow the instructions, it should only take a few seconds

3. On the right side of the page there is a "[Releases](https://github.com/jackSeigerman/YouTube-Downloader/releases)" tab, click it and then click "[Windows release](https://github.com/jackSeigerman/YouTube-Downloader/releases/tag/v1.0.3)" *(or just click the Windows release button)*

4. Download the two exe files at the bottom of the square. They should be called "Youtube Downloader.exe" and "yt-dlp.exe" 

5. Once downloaded, place both exe files into a folder somewhere on your computer, the location does not matter as long as you can recall where you placed it, you can also name this folder whatever you want

---

### *MacOS*

1. A JDK of at least version 23 is needed to run the jar. click the [Download](https://download.oracle.com/java/23/latest/jdk-23_macos-aarch64_bin.dmg) button to download the JDK if you dont already have it.

2. If you just downloaded the jdk dmg listed above, run the dmg and follow the instructions, it should only take a few seconds

3. On the right side of the page there is a "[Releases](https://github.com/jackSeigerman/YouTube-Downloader/releases)" tab, click it and then click "[MacOS release](https://github.com/jackSeigerman/YouTube-Downloader/releases/tag/v1.0.1)" *(or just click the MacOs release button)*

6. Download the jar file and the yt-dlp file at the bottom of the square. They should be called "Youtube Downloader Mac.jar" and "yt-dlp" 

7. Once downloaded, place both the jar and yt-dlp file into a folder somewhere on your computer, the location does not matter as long as you can recall where you placed it, you can also name this folder whatever you want


---

### Linux

1. A JDK of at least version 23 is needed to run the jar. click the [Download](https://download.oracle.com/java/23/latest/jdk-23_linux-x64_bin.deb) button to download the JDK if you dont already have it.

2. If you just downloaded the jdk deb listed above, run the deb and follow the instructions, it should only take a few seconds (yes you can also curl or wget it if you know what that means)

3. On the right side of the page there is a "[Releases](https://github.com/jackSeigerman/YouTube-Downloader/releases)" tab, click it and then click "[Linux release](https://github.com/jackSeigerman/YouTube-Downloader/releases/tag/v1.0.0)" *(or just click the Linux release button)*

6. Download the jar file and the yt-dlp file at the bottom of the square. They should be called "Youtube Downloader.jar" and "yt-dlp" 

7. Once downloaded, place both the jar and yt-dlp file into a directory somewhere on your computer, the location does not matter as long as you can recall where you placed it, you can also name this directory whatever you want

---

>Likewise, you can download the entire repository and run the Exe or Jar from the build folder


## User Guide

Insert a Youtube link  in **youtube video URL** to set the video you want to download

![Canvas](assets/Gallery.JPG)

Click the **MP4** dropdown to switch between Mp4 and Mp3

![Canvas](assets/GenerateImage.JPG)

Click **Download** to download the selected video as the selected format

![Canvas](assets/Download.JPG)


## License
This project is licensed under the MIT License - see the [LICENSE.md](LICENSE) file for details
