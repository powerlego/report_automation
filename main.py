import os
import time


from selenium.webdriver.common.by import By
from dotenv import load_dotenv

from data_grabber import DataGrabber


def main():
    config = load_dotenv(".env")
    data_grabber = DataGrabber(os.environ["PASSWORD1"])
    driver = data_grabber.driver
    print(data_grabber.base_url)
    driver.get(data_grabber.base_url)
    time.sleep(5)
    
    driver.quit()


if __name__ == "__main__":
    main()
