import sys
from Adafruit_IO import MQTTClient
import time
import random
from simple_ai import *
from uart import *

AIO_FEED_IDs = ["nutnhan1","nutnhan2"]
AIO_USERNAME = "thinhphatmai2001"
AIO_KEY = "aio_Yhyk75BKOy9m29hxtqS5RMDIuf3y"

def connected(client):
    print("Ket noi thanh cong ...")
    for topic in AIO_FEED_IDs:
        client.subscribe(topic)

def subscribe(client , userdata , mid , granted_qos):
    print("Subscribe thanh cong ...")

def disconnected(client):
    print("Ngat ket noi ...")
    sys.exit (1)

def message(client , feed_id , payload):
    print("Nhan du lieu: " + payload + " , feed id: ",feed_id)

client = MQTTClient(AIO_USERNAME , AIO_KEY)
client.on_connect = connected
client.on_disconnect = disconnected
client.on_message = message
client.on_subscribe = subscribe
client.connect()
client.loop_background()
counter = 10
sensor_type = 0
counter_ai = 5
ai_result = ""
previous_result = ""
while True:
    # counter= counter -1
    # if counter <= 0:
    #     counter = 10
    #     # todo
    #     print("Random data is publising...")
    #     if sensor_type == 0:  
    #         print("Temperature...")
    #         temp = random.randint(10,20)
    #         client.publish("cambien1",temp)
    #         sensor_type = 1
    #     elif sensor_type == 1:
    #         print("Humidity...")
    #         humi = random.randint(50,70)
    #         client.publish("cambien2",humi)
    #         sensor_type = 2
    #     elif sensor_type == 2:
    #         print("Light...")
    #         light = random.randint(100,500)
    #         client.publish("cambien3",light)
    #         sensor_type = 0
    
    # counter_ai = counter_ai - 1
    # if counter_ai <= 0 :
    #     # todo for AI
    #     counter_ai = 5
    #     previous_result = ai_result
    #     ai_result = image_detector()
    #     print("AI Output: ", ai_result)
    #     if previous_result != ai_result:
    #         client.publish("ai",ai_result[2:])
    readSerial(client)        
    time.sleep(1)
