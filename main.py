import os
import sys
import time
from widgets.main_screen import MainScreen

from PySide6 import QtWidgets
from PySide6.QtWidgets import QApplication, QMainWindow
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.edge.service import Service as EdgeService

class MainWindow(QMainWindow):
    def __init__(self):
        super().__init__()
        self.window = QtWidgets.QWidget()
        self.setCentralWidget(self.window)
        self.main_screen = MainScreen()
        self.layout = QtWidgets.QVBoxLayout()
        self.layout.addWidget(self.main_screen)
        self.window.setLayout(self.layout)
        self.window.maximumSize = (sys.maxsize, sys.maxsize)
        self.setWindowTitle("Report Automation")
        self.setGeometry(300, 300, 600, 400)
        self.show()



def main():
    app = QApplication(sys.argv)
    window = MainWindow()
    
    sys.exit(app.exec_())
    # USERPROFILE = os.environ["USERPROFILE"]
    # driver_location = os.path.join(
    #     USERPROFILE,
    #     "AppData",
    #     "Local",
    #     "Programs",
    #     "report_automation",
    #     "msedgedriver.exe",
    # )
    # service = EdgeService(driver_location)
    # driver = webdriver.Edge(service=service)
    # driver.get("https://www.google.com")
    # time.sleep(5)
    # search_box = driver.find_element(By.NAME, "q")
    # search_box.send_keys("EdgeDriver")
    # search_box.submit()
    # time.sleep(5)  # Let the user actually see something!
    # driver.quit()


if __name__ == "__main__":
    main()
