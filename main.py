import sys
import os
import requests
import subprocess
from PyQt5.QtWidgets import QApplication, QWidget, QVBoxLayout, QLineEdit, QPushButton, QFileDialog, \
    QLabel, QProgressBar, QGroupBox
from PyQt5.QtCore import pyqtSlot, Qt
from PyQt5.QtGui import QPixmap, QIcon
from yt_dlp import YoutubeDL

# Global variable to hold the selected download directory
download_directory = ""


def setup_ffmpeg_path():
    if getattr(sys, 'frozen', False):
        base_path = sys._MEIPASS
    else:
        base_path = os.path.dirname(__file__)
    ffmpeg_path = os.path.join(base_path, 'ffmpeg')
    os.environ['PATH'] += os.pathsep + ffmpeg_path

@pyqtSlot()
def download_video():
    url = url_input.text()
    if not url or not download_directory:
        error_label.setText("Please provide a valid URL and select a download directory.")
        return

    output_path = f"{download_directory}/%(title)s.%(ext)s"
    ydl_opts = {
        'format': 'bestvideo[height>=1080]+bestaudio/best[ext=mp4]/best',
        'merge_output_format': 'mp4',
        'outtmpl': output_path,
        'progress_hooks': [hook],
        'noplaylist': True
    }

    try:
        with YoutubeDL(ydl_opts) as ydl:
            ydl.download([url])
    except Exception as e:
        error_label.setText(f"An error occurred: {e}")
        progress_bar.setValue(0)


@pyqtSlot()
def download_mp3():
    url = url_input.text()
    if not url or not download_directory:
        error_label.setText("Please provide a valid URL and select a download directory.")
        return

    output_path = f"{download_directory}/%(title)s.%(ext)s"
    ydl_opts = {
        'format': 'bestaudio/best',
        'postprocessors': [{
            'key': 'FFmpegExtractAudio',
            'preferredcodec': 'mp3',
            'preferredquality': '192',
        }],
        'outtmpl': output_path,
        'progress_hooks': [hook],
        'noplaylist': True
    }

    try:
        with YoutubeDL(ydl_opts) as ydl:
            ydl.download([url])
    except Exception as e:
        error_label.setText(f"An error occurred: {e}")
        progress_bar.setValue(0)


def load_last_directory():
    try:
        with open('last_directory.txt', 'r') as file:
            return file.read().strip()
    except FileNotFoundError:
        return ""


def save_last_directory(directory):
    with open('last_directory.txt', 'w') as file:
        file.write(directory)


def select_directory():
    global download_directory
    directory = QFileDialog.getExistingDirectory(window, "Select Directory", download_directory)
    if directory:
        download_directory = directory
        directory_label.setText(f"Download to: {directory}")
        save_last_directory(directory)


def open_file_location():
    if download_directory:
        if sys.platform == 'win32':
            os.startfile(download_directory)
        elif sys.platform == 'darwin':  # macOS
            subprocess.run(['open', download_directory])
        else:  # Linux variants
            subprocess.run(['xdg-open', download_directory])
    else:
        error_label.setText("No directory selected or file downloaded.")


def hook(d):
    if d['status'] == 'downloading':
        downloaded_bytes = d['downloaded_bytes']
        total_bytes = d.get('total_bytes') or d.get('total_bytes_estimate')
        if total_bytes:
            percentage = int(downloaded_bytes / total_bytes * 100)
            progress_bar.setValue(percentage)
    elif d['status'] == 'finished':
        progress_bar.setValue(100)
        error_label.setText("Download successful!")
        open_location_button.show()  # Show the button upon successful download


def fetch_video_info():
    url = url_input.text()
    if not url:
        error_label.setText("Please enter a valid URL.")
        return

    error_label.setText("")  # Clear the error message for new URL processing

    ydl_opts = {'noplaylist': True}
    try:
        with YoutubeDL(ydl_opts) as ydl:
            info_dict = ydl.extract_info(url, download=False)
            video_title = info_dict.get('title', 'No title found')
            video_thumbnail = info_dict.get('thumbnail', None)
            video_uploader = info_dict.get('uploader', 'Unknown uploader')

        video_info_group.setTitle("Video Information")
        video_title_label.setText(f"Title: {video_title}")
        uploader_label.setText(f"Uploaded by: {video_uploader}")

        if video_thumbnail:
            response = requests.get(video_thumbnail)
            pixmap = QPixmap()
            pixmap.loadFromData(response.content)
            thumbnail_label.setPixmap(pixmap.scaled(240, 135, Qt.KeepAspectRatio, Qt.SmoothTransformation))
        else:
            thumbnail_label.setPixmap(QPixmap())  # Clear if no thumbnail available

    except Exception as e:
        # Clear all fields if there's an error in fetching data
        video_title_label.setText("Title: Not loaded")
        uploader_label.setText("Uploaded by: Not loaded")
        thumbnail_label.setPixmap(QPixmap())
        error_label.setText("Failed to fetch video data. Check URL and network connection.")


app = QApplication(sys.argv)
window = QWidget()
window.setWindowTitle('YouTube Downloader')
window.setWindowIcon(QIcon('icon.ico'))
window.resize(500, 400)

main_layout = QVBoxLayout(window)

url_input = QLineEdit()
url_input.setPlaceholderText('Enter video URL here...')
url_input.textChanged.connect(fetch_video_info)
main_layout.addWidget(url_input)

download_video_button = QPushButton('Download MP4')
download_video_button.clicked.connect(download_video)
main_layout.addWidget(download_video_button)

download_mp3_button = QPushButton('Download MP3')
download_mp3_button.clicked.connect(download_mp3)
main_layout.addWidget(download_mp3_button)

directory_button = QPushButton('Select Download Directory')
directory_button.clicked.connect(select_directory)
main_layout.addWidget(directory_button)

open_location_button = QPushButton('Open File Location')
open_location_button.clicked.connect(open_file_location)
open_location_button.hide()  # Initially hide the button
main_layout.addWidget(open_location_button)

directory_label = QLabel("No directory selected")
main_layout.addWidget(directory_label)

progress_bar = QProgressBar()
progress_bar.setAlignment(Qt.AlignCenter)
main_layout.addWidget(progress_bar)

video_info_group = QGroupBox("Video Information")
video_info_layout = QVBoxLayout()
video_title_label = QLabel("Title: Not loaded")
video_info_layout.addWidget(video_title_label)

uploader_label = QLabel("Uploaded by: Not loaded")
video_info_layout.addWidget(uploader_label)

thumbnail_label = QLabel()
thumbnail_label.setFixedSize(240, 135)
video_info_layout.addWidget(thumbnail_label)

video_info_group.setLayout(video_info_layout)
main_layout.addWidget(video_info_group)

error_label = QLabel()
main_layout.addWidget(error_label)

window.setLayout(main_layout)
window.show()

download_directory = load_last_directory()
if download_directory:
    directory_label.setText(f"Download to: {download_directory}")

setup_ffmpeg_path()

sys.exit(app.exec_())
