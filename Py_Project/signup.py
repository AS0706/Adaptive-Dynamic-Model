from tkinter import *
from tkinter import messagebox
import pymysql

root=Tk()
root.title('Sign Up')
root.geometry("500x500")
root.configure(bg='#333333')
root.state('zoomed')

def popup():
    messagebox.showinfo("Sign Up","Created an account!")

def login_page():
    root.destroy()
    import login

def clear():
    email_entry.delete(0,END)
    username_entry.delete(0,END)
    designation_entry.delete(0,END)
    password_entry.delete(0,END)

def connect_database():
    if email_entry.get()=='' or username_entry.get()=='' or designation_entry.get()=='' or password_entry.get()=='' :
        messagebox.showerror("Error","All fields are required")
    else:
        try:
            con=pymysql.connect(host='localhost' , user= 'root' , password = 'Ambika@07')
            mycursor=con.cursor()
        except:
            messagebox.showerror('Error','Database Connection Error , Try Again')
        try:
             query='create database userdata'
             mycursor.execute(query)
             query='use userdata'
             mycursor.execute(query)
             query='create table data(id int auto_increment primary key not null, email varchar(50) unique,username varchar(30),designation varchar(50),password varchar(20))'
             mycursor.execute(query)
        except:
            mycursor.execute('use userdata')

        query='select * from data where username=%s OR email=%s'
        mycursor.execute(query,(username_entry.get(),email_entry.get())) 
        row=mycursor.fetchone()
        if row != None:
            messagebox.showerror('Error','Username or email already exists')
        else:
            query='insert into data (username,email,designation, passward) values(%s,%s,%s,%s)'
            mycursor.execute(query,(username_entry.get(),email_entry.get(),designation_entry.get(),password_entry.get())) 
            con.commit()
            con.close()
            messagebox.showinfo("Sign Up",'Sign Up successfull!')
            clear()
            root.destroy()
            import login

frame=Frame(bg='#333333')
signup_label=Label(frame,text="Sign Up",bg='#333333',fg='#B53C17',font=("Aerial",30))
signup_label.grid(row=0,column=0,columnspan=2,sticky='news',pady=40)
username=Label(frame,text="Username",bg='#333333',fg='#ffffff',font=("Aerial",15))
username.grid(row=1,column=0)
email=Label(frame,text="Email",bg='#333333',fg='#ffffff',font=("Aerial",15))
email.grid(row=2,column=0)
email_entry=Entry(frame)
email_entry.grid(row=2,column=1,pady=20)
designation=Label(frame,text="Designation",bg='#333333',fg='#ffffff',font=("Aerial",15))
designation.grid(row=3,column=0)
designation_entry=Entry(frame)
designation_entry.grid(row=3,column=1,pady=20)
username_entry=Entry(frame)
username_entry.grid(row=1,column=1,pady=20)
password=Label(frame,text="Password",bg='#333333',fg='#ffffff',font=("Aerial",15))
password.grid(row=4,column=0)
password_entry=Entry(frame)
password_entry.grid(row=4,column=1,pady=10)
password_entry.config(show='*')
button=Button(frame,text="Sign Up",bg='#B53C17',font=("Aerial",15) , command=connect_database)
button.grid(row=5,column=0,columnspan=2,pady=20)
login_connector=Label(frame,text="Already have an account?",font=("Aerial",15),bg='#333333',fg='#ffffff').grid(row=6,column=0)
login_button=Button(frame,text="Login",font=("Aerial",15),bg='#DDB967',command=login_page).grid(row=6,column=1)
frame.pack()

root.mainloop()