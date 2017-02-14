#!/usr/bin/env python3

import tkinter as tk

window = tk.Tk()
window.title('My Window')
window.geometry('600x300')

var = tk.StringVar()
on_hit = False

def hit_me():
	global on_hit

	if (on_hit == False):
		on_hit = True
		var.set('Oh! YA~')
	else:
		on_hit = False
		var.set('')

l = tk.Label(window, textvariable = var, bg = 'green', font = ('Arial', 12), width = 15, height = 2)
l.pack()

b = tk.Button(window, text = 'Hit me', bg = 'yellow', font = ('Arial', 12), width = 15, height = 2, command = hit_me)
b.pack()

# also see grid and place
frm = tk.Frame(window)
frm.pack()

frm_l = tk.Frame(frm)
frm_r = tk.Frame(frm)
frm_l.pack(side = 'left')
frm_r.pack(side = 'right')

e = tk.Entry(frm_l, show = '*')
e.pack()

def insert_point():
	var = e.get()
	t.insert('insert', var)

def insert_end():
	var = e.get()
	t.insert('end', var)

b2 = tk.Button(frm_l, text = 'insert point', width = 15, height = 2, command = insert_point)
b2.pack()

b3 = tk.Button(frm_l, text = 'insert end', command = insert_end)
b3.pack()

t = tk.Text(frm_l, width = 30, height = 2)
t.pack()

var2 = tk.StringVar()
var2.set((11, 22, 33, 44))
lb = tk.Listbox(frm_r, listvariable = var2)
list_items = [1, 2, 3, 4]
for item in list_items:
	lb.insert('end', item)
lb.insert(1, 'first')
lb.insert(2, 'second')
lb.pack()

window.mainloop()
