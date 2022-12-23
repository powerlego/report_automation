from PySide6 import QtWidgets
from PySide6.QtCore import Qt, QCoreApplication
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

        self.password_label = QtWidgets.QLabel("6-Digit Code Password:")
        self.password_input = QtWidgets.QLineEdit()
        self.password_input.returnPressed.connect(self.login)
        self.hbox = QtWidgets.QHBoxLayout()
        self.hbox.addWidget(self.password_label)
        self.hbox.addWidget(self.password_input)
        self.hbox2 = QtWidgets.QHBoxLayout()
        self.button = QtWidgets.QPushButton("Login")
        self.button.clicked.connect(self.login)
        self.button.setDefault(True)
        self.expander2 = QtWidgets.QSpacerItem(100,2, QtWidgets.QSizePolicy.Policy.Expanding,QtWidgets.QSizePolicy.Policy.Minimum)
        self.hbox2.addItem(self.expander2)
        self.hbox2.addWidget(self.button)
        self.layout = QtWidgets.QVBoxLayout()
        self.layout.maximumSize = (sys.maxsize, sys.maxsize)
        self.layout.addWidget(self.header)
        self.layout.addLayout(self.hbox)
        self.layout.addItem(self.expander)
        self.layout.addLayout(self.hbox2)
        self.setLayout(self.layout)
        
    def login(self):
        print(self.password_input.text())
        self.password_input.setText("")
        QCoreApplication.quit()
        
        
