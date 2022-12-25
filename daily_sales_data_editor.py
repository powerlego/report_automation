import pandas
from datetime import datetime
import os

date = datetime.now()
program_data_dir = os.path.join( os.environ["USERPROFILE"],
            "AppData",
            "Local",
            "Programs",
            "report_automation")
isExist = os.path.exists(program_data_dir)
if not isExist:
    os.makedirs(program_data_dir)
    

# if date.strftime("%A") == "Friday":
curr_date = str(date.month) + "/" + str(date.day) + "/" + str(date.year)
file_path = os.environ['USERPROFILE'] + '\\Desktop\\SampleFile.csv'
columns = ['Location', 'Store', 'Day', 'Item', 'Desc', 'Desc_2', 'Metrics', 'Unit_Sales', '$_Sales']
data = pandas.read_csv(file_path, header=0, names=columns)
print(data)
# data = data.dropna()
# data = data.reset_index(drop=True)
data = data[(data.Day != curr_date)]
print("Data Ready")
csv_path = os.path.join(program_data_dir, 'out.csv')
data.to_csv(csv_path, index=False)

# excel_path = os.environ['USERPROFILE']+'\\Desktop\\WTD Tracker Menards 2023.xlsx'
# writer = pandas.ExcelWriter(path=excel_path,engine="openpyxl", mode='a', if_sheet_exists='overlay')
# print('Starting to write')
# data.to_excel(writer, sheet_name="2023 Raw", header=columns, startrow=5, startcol=-1)
# writer.close()
# else:
#    print("It's not Friday")
