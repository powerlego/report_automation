import os
import time

from selenium import webdriver

def main():
    USERPROFILE = os.environ['USERPROFILE']
    driver_location = os.path.join(USERPROFILE, 'AppData', 'Local','Programs','report_automation', 'msedgedriver.exe')
    print('Hello World!')
    
    
if __name__ == '__main__':
    main()
    