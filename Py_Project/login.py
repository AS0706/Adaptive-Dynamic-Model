from tkinter import *
from tkinter import messagebox
import pymysql

login_window=Tk()
login_window.geometry('500x500')
login_window.configure(bg='#333333')
login_window.title('Login')

def popup2():
    messagebox.showinfo("Login","Login Successful!")

def signup_page():
    login_window.destroy()
    import signup

def login_user():
    if username_entry.get()=='' or password_entry.get()=='' :
        messagebox.showerror("Error","Fields can't be empty")
    else:
        try:
            con=pymysql.connect(host='localhost' , user= 'root' , password = 'Ambika@07')
            mycursor=con.cursor()
        except:
            messagebox.showerror('Error','Database Connection Error , Try Again')
        query='use userdata'
        mycursor.execute(query)
        query="select * from data where username=%s and passward=%s"
        mycursor.execute(query,(username_entry.get(),password_entry.get()))
        row=mycursor.fetchone()
        if row==None:
            messagebox.showerror('Error','Wrong username or password')
        else:
            messagebox.showinfo('Login','Login Successful!')
            login_window.destroy()
            import database1

login_window1=Frame(bg='#333333')
login_label=Label(login_window1,text="Login",bg='#333333',fg='#B53C17',font=("Aerial",30))
login_label.grid(row=0,column=0,columnspan=2,sticky='news',pady=40)
username=Label(login_window1,text="Username",bg='#333333',fg='#ffffff',font=("Aerial",15))
username.grid(row=1,column=0)
username_entry=Entry(login_window1)
username_entry.grid(row=1,column=1,pady=20)
password=Label(login_window1,text="Password",bg='#333333',fg='#ffffff',font=("Aerial",15))
password.grid(row=2,column=0)
password_entry=Entry(login_window1)
password_entry.grid(row=2,column=1,pady=10)
password_entry.config(show='*')
button=Button(login_window1,text="Login",bg='#B53C17',font=("Aerial",15) , command=login_user)
button.grid(row=3,column=0,columnspan=2,pady=20)
signup_connector=Label(login_window1,text="Already have an account?",font=("Aerial",15),bg='#333333',fg='#ffffff').grid(row=4,column=0)
signup_button=Button(login_window1,text="Signup",font=("Aerial",15),bg='#DDB967',command=signup_page).grid(row=4,column=1)
login_window1.pack()
login_window.mainloop()