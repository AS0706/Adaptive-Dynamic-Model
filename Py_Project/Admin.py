from tkinter import *
from tkinter import messagebox
import pymysql

Admin_window= Tk()
Admin_window.geometry('400x200')
Admin_window.title("Admin Password")
Admin_window.configure(bg='#333333')

def admin_password_match():
    ad_pass=admin_password_entry.get()
    if ad_pass == 'UserData123':
        try:
            con=pymysql.connect(host='localhost' , user= 'root' , password = 'Ambika@07')
            mycursor=con.cursor()
        except:
            messagebox.showerror('Error','Database Connection Error , Try Again')
        query='use userdata'
        mycursor.execute(query)
        query="Select * from data"
        mycursor.execute(query)
        myresult=mycursor.fetchall()
        for row in myresult:
            print(row)
        Admin_window.destroy()
        import database
        
       
    else:
        messagebox.showerror('Error','Wrong Password')
        Admin_window.destroy()


frame2= Frame (bg='#333333')
admin_password=Label(frame2, text="Enter Admin Password",bg='#333333',font="Aerial 20 bold",fg='#B53C17',pady=20)
admin_password.grid(sticky='news')
admin_password_entry=Entry(frame2,width=20,font=10)
admin_password_entry.grid(pady=15)
admin_password_entry.config(show='*')
button=Button(frame2,text='Enter',bg='#B53C17', command = admin_password_match).grid()
frame2.pack()

Admin_window.mainloop()