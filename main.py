import os
import time

from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.edge.service import Service as EdgeService


def main():
    USERPROFILE = os.environ["USERPROFILE"]
    driver_location = os.path.join(
        USERPROFILE,
        "AppData",
        "Local",
        "Programs",
        "report_automation",
        "msedgedriver.exe",
    )
    service = EdgeService(driver_location)
    driver = webdriver.Edge(service=service)
    driver.get("https://www.google.com")
    time.sleep(5)
    search_box = driver.find_element(By.NAME, "q")
    search_box.send_keys("EdgeDriver")
    search_box.submit()
    time.sleep(5)  # Let the user actually see something!
    driver.quit()


if __name__ == "__main__":
    main()
