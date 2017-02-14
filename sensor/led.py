#!/usr/bin/env python3

import RPi.GPIO as GPIO
import time

GPIO_KEY = 12
GPIO_LED = 16

loop = True

def my_callback(GPIO_KEY):
    global loop
    loop = False

GPIO.setmode(GPIO.BOARD)
GPIO.setup(GPIO_KEY, GPIO.IN, pull_up_down = GPIO.PUD_UP)
GPIO.setup(GPIO_LED, GPIO.OUT)
GPIO.add_event_detect(GPIO_KEY, GPIO.RISING, callback = my_callback, bouncetime = 200)

#p = GPIO.PWM(GPIO_LED, 5)
#p.start(50)
#input('Press return to stop:')
#p.stop()

while loop:
    GPIO.output(GPIO_LED, GPIO.HIGH)
    time.sleep(0.5)
    GPIO.output(GPIO_LED, GPIO.LOW)
    time.sleep(0.5)

GPIO.remove_event_detect(GPIO_KEY)
GPIO.cleanup()

print('Exit!')
