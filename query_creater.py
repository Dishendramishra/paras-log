#%%
from datetime import datetime
from string import Template
import glob

def parse_log_file(filename):
    lines = open(filename).readlines()
    observers = lines[4][12:].strip()
    date = lines[3][11:17]+" "+lines[3][-5:].strip()
    fmt = "%b %d %Y"
    date = datetime.strptime(date,fmt).strftime("%d-%m-%Y")

    lines = lines[8:]
    data = []

    for line in lines:
        line = line.split()
        if line[5].startswith("Star"):
            data.append([line[3],line[6]])

    return observers,date,data

def create_queries(observers,date,data):
    query = 'db.observations.insert({instrument:"PARAS", object_name:"$obj", observer:"$obsrvr",observing_mode:"science",sky_condition:"Spectroscopic",date:"$date"})\n'
    queries = []

    for row in data:
        queries.append(Template(query).substitute(obj=row[1], obsrvr=observers, date=date+' '+row[0]))

    with open(date+".txt","w") as file:
        file.writelines(queries)
    
#%%
log_filenames = glob.glob("paraslog*.dat") 

for filename in log_filenames:
    observers,date,data = parse_log_file(filename)
    create_queries(observers,date,data)
    print("processed: ",filename)
