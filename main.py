import sys
from PyQt5.QtWidgets import QApplication, QWidget, QVBoxLayout, QLineEdit, QPushButton, QFileDialog, QLabel, QProgressBar
from PyQt5.QtCore import pyqtSlot, Qt
from yt_dlp import YoutubeDL
from PyQt5.QtGui import QIcon

# Global variable to hold the selected download directory
download_directory = ""

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
    if directory:  # Check if a directory was selected
        download_directory = directory
        directory_label.setText(f"Download to: {directory}")
        save_last_directory(directory)

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

@pyqtSlot()
def download_video():
    url = url_input.text()
    output_path = f"{download_directory}/%(title)s.%(ext)s"

    ydl_opts = {
        'format': 'bestvideo[ext=mp4][vcodec=h264]+bestaudio[ext=m4a]/best[ext=mp4][vcodec=h264]/best',
        'merge_output_format': 'mp4',
        'outtmpl': output_path,
        'progress_hooks': [hook],  # Add progress hook
        'noplaylist': True
    }

    with YoutubeDL(ydl_opts) as ydl:
        ydl.download([url])

@pyqtSlot()
def download_mp3():
    url = url_input.text()
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
    with YoutubeDL(ydl_opts) as ydl:
        ydl.download([url])

app = QApplication(sys.argv)
window = QWidget()
window.setWindowTitle('Youtube Downloader')
window.setWindowIcon(QIcon('icon.ico'))

window.resize(500, 300)

layout = QVBoxLayout()

url_input = QLineEdit()
url_input.setPlaceholderText('Enter video URL here...')
layout.addWidget(url_input)

download_video_button = QPushButton('Download MP4')
download_video_button.clicked.connect(download_video)
layout.addWidget(download_video_button)

download_mp3_button = QPushButton('Download MP3')
download_mp3_button.clicked.connect(download_mp3)
layout.addWidget(download_mp3_button)

directory_button = QPushButton('Select Download Directory')
directory_button.clicked.connect(select_directory)
layout.addWidget(directory_button)

directory_label = QLabel("No directory selected")
layout.addWidget(directory_label)

progress_bar = QProgressBar(window)
progress_bar.setAlignment(Qt.AlignCenter)
layout.addWidget(progress_bar)

error_label = QLabel("")  # Label to display download status or errors
layout.addWidget(error_label)

window.setLayout(layout)
window.show()

# Load the last used directory at startup
download_directory = load_last_directory()
if download_directory:
    directory_label.setText(f"Download to: {download_directory}")

sys.exit(app.exec_())
