#!/usr/bin/env python3

# Google Spreadsheet DHT Sensor Data-logging Example

# Depends on the 'gspread' and 'oauth2client' package being installed.  If you
# have pip installed execute:
#   sudo pip install gspread oauth2client

# Also it's _very important_ on the Raspberry Pi to install the python-openssl
# package because the version of Python is a bit old and can fail with Google's
# new OAuth2 based authentication.  Run the following command to install the
# the package:
#   sudo apt-get update
#   sudo apt-get install python-openssl

# Copyright (c) 2014 Adafruit Industries
# Author: Tony DiCola

# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:

# The above copyright notice and this permission notice shall be included in all
# copies or substantial portions of the Software.

# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.
import json
import os
import sys
import time
import datetime
import serial
import codecs
import requests

import Adafruit_DHT
import gspread
from oauth2client.service_account import ServiceAccountCredentials

import Adafruit_ADS1x15
import RPi.GPIO as GPIO

# Type of sensor, can be Adafruit_DHT.DHT11, Adafruit_DHT.DHT22, or Adafruit_DHT.AM2302.
DHT_TYPE = Adafruit_DHT.AM2302

# Example of sensor connected to Raspberry Pi pin 23
DHT_PIN = 4
# Example of sensor connected to Beaglebone Black pin P8_11
#DHT_PIN  = 'P8_11'

# Google Docs OAuth credential JSON file.  Note that the process for authenticating
# with Google docs has changed as of ~April 2015.  You _must_ use OAuth2 to log
# in and authenticate with the gspread library.  Unfortunately this process is much
# more complicated than the old process.  You _must_ carefully follow the steps on
# this page to create a new OAuth service in your Google developer console:
#   http://gspread.readthedocs.org/en/latest/oauth2.html
#
# Once you've followed the steps above you should have downloaded a .json file with
# your OAuth2 credentials.  This file has a name like SpreadsheetData-<gibberish>.json.
# Place that file in the same directory as this python script.
#
# Now one last _very important_ step before updating the spreadsheet will work.
# Go to your spreadsheet in Google Spreadsheet and share it to the email address
# inside the 'client_email' setting in the SpreadsheetData-*.json file.  For example
# if the client_email setting inside the .json file has an email address like:
#   149345334675-md0qff5f0kib41meu20f7d1habos3qcu@developer.gserviceaccount.com
# Then use the File -> Share... command in the spreadsheet to share it with read
# and write acess to the email address above.  If you don't do this step then the
# updates to the sheet will fail!
GDOCS_OAUTH_JSON       = '/home/pi/Downloads/My Pi-40b20d54ceaf.json'

# Google Docs spreadsheet name.
GDOCS_SPREADSHEET_NAME = '1MitMA7-IRWZGl8g-D1ef9CXoVHpoz0tnY2WVwtyQUJI'

# How long to wait (in seconds) between measurements.
FREQUENCY_SECONDS      = 60

def login_open_sheet(oauth_key_file, spreadsheet):
    """Connect to Google Docs spreadsheet and return the first worksheet."""
    try:
        scope = ['https://spreadsheets.google.com/feeds']
        credentials = ServiceAccountCredentials.from_json_keyfile_name(oauth_key_file, scope)
        gc = gspread.authorize(credentials)
        #worksheet = gc.open(spreadsheet).sheet1
        worksheet = gc.open_by_key(spreadsheet).sheet1
        return worksheet
    except Exception as ex:
        print('Unable to login and get spreadsheet.  Check OAuth credentials, spreadsheet name, and make sure spreadsheet is shared to the client_email address in the OAuth .json file!')
        print('Google sheet login failed with error:', ex)
        #sys.exit(1)
        return None

'''
def num_format(num_hex):
    try:
        num_int = int(num_hex, 16)
        return float(format(num_int, '.10f'))
    except:
        print('num_hex is null')
        return None

def get_vout(hex_str):
    index = hex_str.find('aa')
    Vout_h_index = index + 2
    Vout_l_index = Vout_h_index + 2

    Vout_h = num_format(hex_str[Vout_h_index : Vout_h_index + 2])
    Vout_l = num_format(hex_str[Vout_l_index : Vout_l_index + 2])

    if Vout_h is None or Vout_l is None:
        return None
    else:
        print('Vout_h: {0:0.1f}, Vout_l: {1:0.1f}'.format(Vout_h, Vout_l))
        Vout = ((Vout_h * 256) + Vout_l) / 1024 * 5
        Vout = round(Vout, 3)
        return Vout
'''

def get_vout(hex_str):
    index = hex_str.find(b'\xaa')

    if index is -1 or sum(hex_str[index + 1 : index + 5]) is not hex_str[index + 5]:
        return None

    Vout_h = float(hex_str[index + 1])
    Vout_l = float(hex_str[index + 2])

    print('Vout_h: {0:0.1f}, Vout_l: {1:0.1f}'.format(Vout_h, Vout_l))
    Vout = ((Vout_h * 256) + Vout_l) / 1024 * 5
    Vout = round(Vout, 3)
    return Vout

def get_adc(channel):
    values = [0] * 10

    for i in range(10):
        values[i] = adc.read_adc(channel, gain = GAIN)

    return (sum(values) / 10.0) / 32768.0 * 6.144

f = open(os.devnull, 'w')
sys.stdout = f

print('Logging sensor measurements to {0} every {1} seconds.'.format(GDOCS_SPREADSHEET_NAME, FREQUENCY_SECONDS))
print('Press Ctrl-C to quit.')

worksheet = None
last_humidity = last_temp = None

ser = serial.Serial(
    port = '/dev/serial0',
    baudrate = 2400,
    parity = serial.PARITY_NONE,
    stopbits = serial.STOPBITS_ONE,
    bytesize = serial.EIGHTBITS,
    timeout = 1
)

MQ_PIN = 18
MS_PIN = 22

GPIO.setmode(GPIO.BOARD)
GPIO.setup(MQ_PIN, GPIO.IN) # MQ-2
GPIO.setup(MS_PIN, GPIO.IN) # MS1100-P111

adc = Adafruit_ADS1x15.ADS1115(address = 0x48, busnum = 1)

# Choose a gain of 1 for reading voltages from 0 to 4.09V.
# Or pick a different gain to change the range of voltages that are read:
#  - 2/3 = +/-6.144V
#  -   1 = +/-4.096V
#  -   2 = +/-2.048V
#  -   4 = +/-1.024V
#  -   8 = +/-0.512V
#  -  16 = +/-0.256V
# See table 3 in the ADS1015/ADS1115 datasheet for more info on gain.
GAIN = 2/3

while True:
    # Login if necessary.
    if worksheet is None:
        worksheet = login_open_sheet(GDOCS_OAUTH_JSON, GDOCS_SPREADSHEET_NAME)

    # Attempt to get sensor reading.
    humidity, temp = Adafruit_DHT.read(DHT_TYPE, DHT_PIN)

    # Skip to the next reading if a valid measurement couldn't be taken.
    # This might happen if the CPU is under a lot of load and the sensor
    # can't be reliably read (timing is critical to read the sensor).
    if humidity is None or temp is None:
        time.sleep(2)
        continue

    humidity = round(humidity, 3)
    temp = round(temp, 3)
    
    if last_humidity is not None:
        if abs(last_humidity - humidity) >= 30.0 or abs(last_temp - temp) >= 5.0:
            continue

    last_humidity = humidity
    last_temp = temp

    print('Temperature: {0:0.1f} C'.format(temp))
    print('Humidity:    {0:0.1f} %'.format(humidity))

    '''
    byteData = str(codecs.encode(ser.read(21), 'hex'))
    byteData += str(codecs.encode(ser.read(ser.inWaiting()), 'hex'))
    #print('byteData: ', byteData)
    '''

    byteData = ser.read(21)
    byteData += ser.read(ser.inWaiting())
    #print('byteData: ', codecs.encode(byteData, 'hex'))

    A = 550
    #A = 700
    Vout = get_vout(byteData)

    if Vout is None:
        continue

    Dustdensity = int(A * Vout)
    print('Vout: {0}V, Dustdensity: {1}ug/m3'.format(Vout, Dustdensity))

    mq = get_adc(0)
    ms = get_adc(1)

    mq = round(mq, 3)
    ms = (-2.631) + 1.528 * ms + (-0.125) * (ms ** 2)
    ms = round(10 ** ms, 6)
    print('mq: {0}V ({1}), ms: {2}ppm ({3})'.format(mq, int(not GPIO.input(MQ_PIN)), ms, int(not GPIO.input(MS_PIN))))

    # Append the data in the spreadsheet, including a timestamp
    try:
        #worksheet = None
        if worksheet is not None:
            try:
                file = open('gspread.dat', 'r')
                last_rec = 0
                fail = False

                try:
                    rec = open('gspread.rec', 'r')
                    last_rec = int(rec.readline())
                    rec.close()
                except:
                    print('No gspread.rec')

                print('last_rec: ', last_rec)
                rec = open('gspread.rec', 'w')
                skip = 0

                for line in file:
                    if skip < last_rec:
                        skip += 1
                        continue

                    try:
                        append_data = line.split(',')
                        print(append_data)
                        worksheet.append_row((append_data[0], append_data[1], float(append_data[2]), float(append_data[3]), float(append_data[4]), float(append_data[5]), int(append_data[6]), float(append_data[7]), int(append_data[8])))
                        skip += 1
                        last_rec += 1
                        rec.seek(0, 0)
                        rec.write(str(last_rec))
                    except:
                        print('Append error again')
                        fail = True
                        break

                rec.close()
                file.close()

                if fail is not True:
                    try:
                        os.remove('gspread.dat')
                        os.remove('gspread.rec')
                    except:
                        print('No gspread files')
                else:
                    worksheet = None
                    time.sleep(FREQUENCY_SECONDS)
                    continue
            except:
                print('No gspread.dat')

        try:
            r = requests.get('https://www.google.com')
            r.connection.close()
            r.close()
        except:
            print('Connection fail')
            worksheet = None

        worksheet.append_row((datetime.datetime.now().date(), datetime.datetime.now().time(), temp, humidity, Dustdensity, mq, int(not GPIO.input(MQ_PIN)), ms, int(not GPIO.input(MS_PIN))))
    except:
        # Error appending data, most likely because credentials are stale.
        # Null out the worksheet so a login is performed at the top of the loop.
        print('Append error, logging in again')
        worksheet = None

        file = open('gspread.dat', 'a')
        data = '{0},{1},{2},{3},{4},{5},{6},{7},{8}\n'.format(datetime.datetime.now().date(), datetime.datetime.now().time(), temp, humidity, Dustdensity, mq, int(not GPIO.input(MQ_PIN)), ms, int(not GPIO.input(MS_PIN)))
        file.write(data)
        file.close()

        time.sleep(FREQUENCY_SECONDS)
        continue

    # Wait 30 seconds before continuing
    print('Wrote a row to {0}'.format(GDOCS_SPREADSHEET_NAME))
    time.sleep(FREQUENCY_SECONDS)
