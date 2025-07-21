from tkinter import *
import pymysql
from tkinter import messagebox
from tkinter import ttk
import ttkthemes

row_info = ttkthemes.ThemedTk()
row_info.get_themes()
row_info.set_theme('radiance')
row_info.title("Userdata Database")
row_info.state('zoomed')

con=pymysql.connect(host='localhost' , user= 'root' , password = 'Ambika@07')
mycursor=con.cursor()

query='use userdata'
mycursor.execute(query)
query="Select * from data"
mycursor.execute(query)
myresult=mycursor.fetchall()

# def fetch_data():
    
#     mycursor.execute("select * from data")
#     rows=mycursor.fetchall()
#     if len(rows)!=0:
#         data_table.delete(*data_table.get_children())
#         for i in rows:
#             data_table.insert("",END,values=i)
#     con.commit()
# con.close()

# def print_heh():
#     for i in range(0,20):
#         text=Label(frame3,text="heh").pack()
        
frame3=Frame()
heading=Label(frame3,text="User Data",font= "Aerial 20 bold").pack()
# scroll_y=ttk.Scrollbar(frame3,orient=VERTICAL)
data_table=ttk.Treeview(frame3,column=("id","email","username","designation","password"))
# scroll_y.pack=(side=RIGHT,fill=Y)
Button_database=ttk.Button(frame3,text='Connect Database').place(x=800)

data_table.heading("id",text="ID")
data_table.heading("email",text="Email")
data_table.heading("username",text="Username")
data_table.heading("designation",text="Designation")
data_table.heading("password",text="Password")

data_table["show"]="headings"
data_table.pack()
# fetch_data()

close = Button(frame3, text="Edit", command=row_info.destroy)
# close = Button(frame3, text="Edit", command=print_heh)
close.pack()
frame3.pack()
row_info.mainloop()
