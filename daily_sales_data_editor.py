import pandas
from datetime import datetime
import os

date = datetime.now()
if date.strftime("%A") == "Friday":
    curr_date = str(date.month) + "/" + str(date.day) + "/" + str(date.year)
    file_path = os.environ['USERPROFILE'] + '\\Desktop\\SampleFile.csv'
    columns = ['Location', 'Store', 'Day', 'Item', 'Desc', 'Desc_2', 'Metrics', 'Unit_Sales', '$_Sales']
    data = pandas.read_csv(file_path, header=0, names=columns)

    data = data.dropna()
    data = data.reset_index(drop=True)
    
    data = data[(data.Day != curr_date)]
    print("Data Ready")
    excel_path = os.environ['USERPROFILE']+'\\Desktop\\WTD Tracker Menards 2023.xlsx'
    ExcelWriter = pandas.ExcelWriter(path=excel_path, mode='a', if_sheet_exists='overlay')
    print('Starting to write')
    data.to_excel(excel_path, sheet_name="2023 Raw", header=columns, startrow=5, startcol=-1)
else:
    print("It's not Friday")
