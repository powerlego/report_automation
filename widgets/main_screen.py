from PySide6 import QtWidgets
from PySide6.QtCore import Qt
import sys


class MainScreen(QtWidgets.QWidget):
    def __init__(self):
        super().__init__()
        self.header = QtWidgets.QLabel("Please enter your credentials")
        self.header.setStyleSheet("font-size: 20px; font-weight: bold;")
        self.expander = QtWidgets.QSpacerItem(2,100,QtWidgets.QSizePolicy.Policy.Minimum, QtWidgets.QSizePolicy.Policy.Expanding)
        self.header.sizePolicy().setHorizontalPolicy(QtWidgets.QSizePolicy.Policy.Expanding)
        self.header.setAlignment(Qt.AlignCenter)
        self.sizePolicy().setHorizontalPolicy(QtWidgets.QSizePolicy.Policy.Expanding)

        self.username_label = QtWidgets.QLabel("Username")
        self.username_input = QtWidgets.QLineEdit()

        self.layout = QtWidgets.QVBoxLayout()
        self.layout.maximumSize = (sys.maxsize, sys.maxsize)
        self.layout.addWidget(self.header)
        self.layout.addItem(self.expander)
        self.setLayout(self.layout)
        # self.hbox = QtWidgets.QHBoxLayout()
        # self.hbox.addWidget(self.username_label)
        # self.hbox.addWidget(self.username_input)
        # self.layout.addLayout(self.hbox)
