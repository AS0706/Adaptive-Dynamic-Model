from tkinter import *
import pymysql
from tkinter import messagebox
from tkinter import ttk
import ttkthemes
from login import username_entry

row_info = ttkthemes.ThemedTk()
row_info.get_themes()
row_info.set_theme('radiance')
row_info.title("Userdata Database")
row_info.state('zoomed')
con=pymysql.connect(host='localhost' , user= 'root' , password = 'Ambika@07')
mycursor=con.cursor()

def show_specific_employee_data():
    query='use userdata'
    mycursor.execute(query)
    query = "SELECT * FROM data WHERE username=%s"
    mycursor.execute(query,(username_entry,))
    employee_table.delete(*employee_table.get_children())
    fetchdata=mycursor.fetchall()
    for data in fetchdata:
        employee_table.insert('',END,values=data)

def add_employee():
    def add_employee_data():
        if id_entry.get()=='' or emailentry.get()=='' or usernameentry.get() == '' or designationentry.get()=='' or passwordentry.get()=='':
            messagebox.showerror('Error','All Fields are required',parent=add_window)
        else:
            try:
                query='use userdata'
                mycursor.execute(query)
                query='insert into data values (%s,%s,%s,%s,%s)'
                mycursor.execute(query,(id_entry.get(),emailentry.get(),usernameentry.get(),designationentry.get(),passwordentry.get()))
                con.commit()
                messagebox.showinfo('Add',"New employee added")
                query='Select * from data'
                mycursor.execute(query)
                employee_table.delete(*employee_table.get_children())
                fetchdata=mycursor.fetchall()
                for data in fetchdata:
                    employee_table.insert('',END,values=data)
                add_window.destroy()
            except:
                messagebox.showerror("Error","Duplicate Entry")

    add_window=Toplevel()
    add_window.resizable(False,False)
    add_window.grab_set()
    add_window.geometry('400x400')
    id=Label(add_window,text='ID',font=('times new roman' , 20  , "bold")).grid(row=0,column=0,pady=10,padx=10,sticky=W)
    id_entry=Entry(add_window,width=30)
    id_entry.grid(row=0,column=1)
    email=Label(add_window,text='Email',font=('times new roman' , 20  , "bold")).grid(row=1,column=0,pady=10,padx=10,sticky=W)
    emailentry= Entry(add_window,width=30)
    emailentry.grid(row=1,column=1)
    username=Label(add_window,text='Username',font=('times new roman' , 20  , "bold")).grid(row=2,column=0,pady=10,padx=10,sticky=W)
    usernameentry=Entry(add_window,width=30)
    usernameentry.grid(row=2,column=1)
    designation=Label(add_window,text='Designation',font=('times new roman' , 20  , "bold")).grid(row=3,column=0,pady=10,padx=10,sticky=W)
    designationentry=Entry(add_window,width=30)
    designationentry.grid(row=3,column=1)
    password=Label(add_window,text='Password',font=('times new roman' , 20  , "bold")).grid(row=4,column=0,pady=10,padx=10,sticky=W)
    passwordentry=Entry(add_window,width=30)
    passwordentry.grid(row=4,column=1)
    add_employee_button=ttk.Button(add_window,text='Add Employee',command=add_employee_data).grid(row=5,columnspan=2)

def search_employee():
    def search_employee_data():
        query='use userdata'
        mycursor.execute(query)
        query='Select * from data where id=%s or email=%s or username = %s or designation=%s'
        mycursor.execute(query,(id_entry.get(),emailentry.get(),usernameentry.get(),designationentry.get()))
        fetched_data=mycursor.fetchall()
        for data in fetched_data:
            employee_table.insert('',END,values=data)         
    search_window=Toplevel()
    search_window.resizable(False,False)
    search_window.grab_set()
    search_window.geometry('400x400')
    id=Label(search_window,text='ID',font=('times new roman' , 20  , "bold")).grid(row=0,column=0,pady=10,padx=10,sticky=W)
    id_entry=Entry(search_window,width=30)
    id_entry.grid(row=0,column=1)
    email=Label(search_window,text='Email',font=('times new roman' , 20  , "bold")).grid(row=1,column=0,pady=10,padx=10,sticky=W)
    emailentry= Entry(search_window,width=30)
    emailentry.grid(row=1,column=1)
    username=Label(search_window,text='Username',font=('times new roman' , 20  , "bold")).grid(row=2,column=0,pady=10,padx=10,sticky=W)
    usernameentry=Entry(search_window,width=30)
    usernameentry.grid(row=2,column=1)
    designation=Label(search_window,text='Designation',font=('times new roman' , 20  , "bold")).grid(row=3,column=0,pady=10,padx=10,sticky=W)
    designationentry=Entry(search_window,width=30)
    designationentry.grid(row=3,column=1)
    search_employee_button=ttk.Button(search_window,text='Search Employee',command=search_employee_data).grid(row=5,columnspan=2)

def delete_employee():
    indexing=employee_table.focus()
    print(indexing)
    if indexing=="":
        messagebox.showerror('Error','You must select an employee to be deleted!')
    else:
        content=employee_table.item(indexing)
        content_id=content['values'][0]
        query="delete from data where id=%s"
        mycursor.execute(query,content_id)
        con.commit()
        messagebox.showinfo('Delete',"Deleted Successfully")
        query='select * from data'
        employee_table.delete(*employee_table.get_children())
        mycursor.execute(query)

        fetch_data=mycursor.fetchall()
        for data in fetch_data:
            employee_table.insert('',END,values=data)

def show_employee_data():
    query='use userdata'
    mycursor.execute(query)
    query='Select * from data'
    mycursor.execute(query)
    employee_table.delete(*employee_table.get_children())
    fetchdata=mycursor.fetchall()
    for data in fetchdata:
        employee_table.insert('',END,values=data)

def update_employee():
    def update_employee_data():
        query='use userdata'
        mycursor.execute(query)
        query='update data set email=%s,username=%s,designation=%s,passward=%s where id=%s'
        mycursor.execute(query,(emailentry.get(),usernameentry.get(),designationentry.get(),passwordentry.get(),id_entry.get()))
        con.commit()
        messagebox.showinfo('Update','Id has been updated!')
        update_window.destroy()
        query='use userdata'
        mycursor.execute(query)
        query='Select * from data'
        mycursor.execute(query)
        employee_table.delete(*employee_table.get_children())
        fetchdata=mycursor.fetchall()
        for data in fetchdata:
            employee_table.insert('',END,values=data)

    indexing=employee_table.focus()
    print(indexing) 
    if indexing=="":
        messagebox.showerror('Error','You must select an employee to be updated!')
    else:
        update_window=Toplevel()
        update_window.resizable(False,False)
        update_window.grab_set()
        update_window.geometry('400x400')
        id=Label(update_window,text='ID',font=('times new roman' , 20  , "bold")).grid(row=0,column=0,pady=10,padx=10,sticky=W)
        id_entry=Entry(update_window,width=30,state=DISABLED)
        id_entry.grid(row=0,column=1)
        email=Label(update_window,text='Email',font=('times new roman' , 20  , "bold")).grid(row=1,column=0,pady=10,padx=10,sticky=W)
        emailentry= Entry(update_window,width=30)
        emailentry.grid(row=1,column=1)
        username=Label(update_window,text='Username',font=('times new roman' , 20  , "bold")).grid(row=2,column=0,pady=10,padx=10,sticky=W)
        usernameentry=Entry(update_window,width=30)
        usernameentry.grid(row=2,column=1)
        designation=Label(update_window,text='Designation',font=('times new roman' , 20  , "bold")).grid(row=3,column=0,pady=10,padx=10,sticky=W)
        designationentry=Entry(update_window,width=30)
        designationentry.grid(row=3,column=1)
        password=Label(update_window,text='Password',font=('times new roman' , 20  , "bold")).grid(row=4,column=0,pady=10,padx=10,sticky=W)
        passwordentry=Entry(update_window,width=30)
        passwordentry.grid(row=4,column=1)
    
        content=employee_table.item(indexing)
        list_data=content['values']
        id_entry.insert(0,list_data[0])
        emailentry.insert(0,list_data[1])
        usernameentry.insert(0,list_data[2])
        designationentry.insert(0,list_data[3])
        passwordentry.insert(0,list_data[4])
        update_employee_button=ttk.Button(update_window,text='Update Employee',command=update_employee_data).grid(row=5,columnspan=2)

heading=Label(row_info,text="User Data",font= "Aerial 20 bold").pack()
leftframe=Frame(row_info)
leftframe.place(x=50,y=80,width=300,height=600)
rightframe=Frame(row_info)
rightframe.place(x=380,y=80,width=900,height=600)

# scrollbarX=Scrollbar(rightframe,orient='horizontal').pack(side=BOTTOM,fill=X)
# scrollbarY=Scrollbar(rightframe).pack(side=RIGHT,fill=Y)

employee_table=ttk.Treeview(rightframe,columns=('ID','Email','Username','Designation','Password'))
employee_table.column("ID",width=150)
employee_table.column("Email",width=150)
employee_table.column("Username",width=150)
employee_table.column("Designation",width=150)
employee_table.column("Password",width=150)
# ,xscrollcommand=scrollbarX.set,yscrollcommand=scrollbarY.set
employee_table.pack(fill=BOTH,expand=1)
employee_table.config(show='headings')
employee_table.heading('ID',text='ID')
employee_table.heading('Email',text='Email')
employee_table.heading('Username',text='Username')
employee_table.heading('Designation',text='Designation')
employee_table.heading('Password',text='Password')

employee_table.column('ID',width=50,anchor=CENTER)
employee_table.column('Email',anchor=CENTER)
employee_table.column('Username',anchor=CENTER)
employee_table.column('Designation',anchor=CENTER)
employee_table.column('Password',anchor=CENTER)

style=ttk.Style()
style.configure('Treeview',rowheight=30,font=('arial',10,'bold'))
query='use userdata'
mycursor.execute(query)
query=('Select Admin_Rights from data where username=%s')
mycursor.execute(query,(username_entry,))
access_rights=mycursor.fetchone()
value=0
if access_rights:
    value=access_rights[0]
    print(value)
else:
    print("No such User found.")

if value==0:
    addemployee_button=ttk.Button(leftframe,text="Add Employee",width=15,state=DISABLED,command=add_employee).pack(pady=20)
    searchemployee_button=ttk.Button(leftframe,text="Search Employee",width=15,state=DISABLED,command=search_employee).pack(pady=20)
    deleteemployee_button=ttk.Button(leftframe,text="Delete Employee",width=15,state=DISABLED,command=delete_employee).pack(pady=20)
    updateemployee_button=ttk.Button(leftframe,text="Update Employee",width=15,state=DISABLED,command=update_employee).pack(pady=20)
    showdata_button=ttk.Button(leftframe,text="Show Employee",width=15,command=show_specific_employee_data).pack(pady=20)
    exit_button=ttk.Button(leftframe,text="Exit Button",command=row_info.destroy,width=15).pack(pady=20)

  
elif value==1:
    addemployee_button=ttk.Button(leftframe,text="Add Employee",width=15,command=add_employee).pack(pady=20)
    searchemployee_button=ttk.Button(leftframe,text="Search Employee",width=15,command=search_employee).pack(pady=20)
    deleteemployee_button=ttk.Button(leftframe,text="Delete Employee",width=15,command=delete_employee).pack(pady=20)
    updateemployee_button=ttk.Button(leftframe,text="Update Employee",width=15,command=update_employee).pack(pady=20)
    showdata_button=ttk.Button(leftframe,text="Show Employee",width=15,command=show_employee_data).pack(pady=20)
    exit_button=ttk.Button(leftframe,text="Exit Button",command=row_info.destroy,width=15).pack(pady=20)

# scrollbarX.config(command=employee_table.xview)
# scrollbarY.config(command=employee_table.yview)
row_info.mainloop()
# part 3 enabling buttons and try except block 2 at signup page
# add user admin priviledges on the login page
