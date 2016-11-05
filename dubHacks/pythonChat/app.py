from flask import Flask, render_template
from flask_socketio import SocketIO, emit
from googleapiclient import discovery
import httplib2
import json
from oauth2client.client import GoogleCredentials
import os
from Tkinter import *
from tkMessageBox import *

app = Flask(__name__)
app.config['SECRET_KEY'] = 'secret!'
socketio = SocketIO(app)

DISCOVERY_URL = ('https://{api}.googleapis.com/'
	'$discovery/rest?version={apiVersion}')

def callServer(message):
	'''Run a sentiment analysis request on text within a passed filename'''

	http = httplib2.Http()

	credentials = GoogleCredentials.get_application_default().create_scoped(
		['https://www.googleapis.com/auth/cloud-platform'])
	http = httplib2.Http()
	credentials.authorize(http)
	
	service = discovery.build('language', 'v1beta1',
		http = http, discoveryServiceUrl = DISCOVERY_URL)

	service_request = service.documents().analyzeSentiment(
    	body={
      	'document': {
        	'type': 'PLAIN_TEXT',
         	'content': message,
      		}
    	})

	response = service_request.execute()
	polarity = response['documentSentiment']['polarity']
	magnitude = response['documentSentiment']['magnitude']
	print "Sentiment: Emotion of %s (-1 to 1)" % (polarity * magnitude)
	return polarity * magnitude

@app.route('/')
def index():
	return render_template('index.html')

@socketio.on('connect')
def connection():
	emit('welcome')

@socketio.on('chat')
def chat(message):
	emotion = callServer(message['body'])

	if emotion < -0.8:
		showwarning('Warning', 'Please be more considerate')

	message['body'] += "  (Emotion of %s (-1 to 1))" % emotion
	emit('chat', {'body': message['body']}, broadcast=True)
	
if __name__ == '__main__':
	port = int(os.environ.get("PORT", 8000))
	socketio.run(app, port=port)