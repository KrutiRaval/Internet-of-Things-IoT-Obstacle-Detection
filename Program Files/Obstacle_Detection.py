import RPi.GPIO as GPIO
import time
import requests
import json
import urllib2
import urllib


GPIO.setmode(GPIO.BCM)

TRIG = 23
ECHO = 24
url = 'http://www.myandroidhost.16mb.com/api/python/send.php'
send_url = 'http://freegeoip.net/json'

while(1):

    print "Distance Measurement In Progress"

    GPIO.setup(TRIG,GPIO.OUT)
    GPIO.setup(ECHO,GPIO.IN)

    GPIO.output(TRIG, False)
    print "Waiting For Sensor To Settle"
    time.sleep(0.5)

    GPIO.output(TRIG, True)
    time.sleep(0.00001)
    GPIO.output(TRIG, False)

    while GPIO.input(ECHO)==0:
      pulse_start = time.time()

    while GPIO.input(ECHO)==1:
      pulse_end = time.time()

    pulse_duration = pulse_end - pulse_start

    distance = pulse_duration * 17150

    distance = round(distance, 2)

    print "Distance:",distance,"cm"

    r = requests.get(send_url)
    j = json.loads(r.text)
    lat = j['latitude']
    lon = j['longitude']
    print(lat)
    print(lon)
    query_args = { 'action':"UPDATE_LATLONG",'lat':lat , 'lon':lon}
    data = urllib.urlencode(query_args)

    request = urllib2.Request(url, data)

    response = urllib2.urlopen(request).read()    


    GPIO.setup(18, GPIO.OUT) 		                        # sets 18 pin as output

    i=0
    j=5
    k=0
    if (distance<75):
              if (distance<45):
                  if(distance<10):
                        while(k<2):
                                GPIO.output(18, True)		# true sets buzzer pin high
                                time.sleep(0.8)     		#delay of 0.5 sec
                                GPIO.output(18, False)		#false sets buzzer pin low
                                time.sleep(0.8)			#delay of 0.5 sec
                                k=k+1
                                
                  else:
                        while (i<j):
                          GPIO.output(18, True) 	# true sets buzzer pin high
                          time.sleep(0.3) 	    	#delay of 0.3 sec
                          GPIO.output(18, False)    	#false sets buzzer pin low
                          time.sleep(0.3)
                          i=i+1
                        i=0
            
              else:
                GPIO.output(18, True)		# true sets buzzer pin high
                time.sleep(0.8)     		#delay of 0.5 sec
                GPIO.output(18, False)		#false sets buzzer pin low
                time.sleep(0.8)			#delay of 0.5 sec
            
            

GPIO.output(18, False)
GPIO.cleanup()
