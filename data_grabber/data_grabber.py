from dotenv import load_dotenv
from selenium.webdriver.edge.service import Service as EdgeService
from selenium.webdriver.support.wait import WebDriverWait
from selenium import webdriver
from selenium.webdriver.support import expected_conditions as EC
import os


class DataGrabber:
    def __init__(self, password1):
        self.driver_location = os.path.join(os.environ["USERPROFILE"], "AppData", "Local", "Programs", "report_automation", "msedgedriver.exe")
        self.service = EdgeService(self.driver_location)
        self.driver = webdriver.Edge(service=self.service)
        load_dotenv()
        self.base_url = os.environ["BASE_URL"]
        self.username1 = os.environ["USERNAME1"]
        self.password1 = password1
        self.username2 = os.environ["USERNAME2"]
        self.password2 = os.environ["PASSWORD2"]
        
    def wait_for_element(self, element):
        WebDriverWait(self.driver, 10).until(EC.visibility_of_element_located(element))
        