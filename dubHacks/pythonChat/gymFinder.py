from flask import Flask, render_template
from googleplaces import GooglePlaces, types, lang
from flask_socketio import SocketIO, emit
import os
import httplib2
import json
import re,requests

app = Flask(__name__)
app.config['SECRET_KEY'] = 'secret!'

YOUR_API_KEY = 'AIzaSyAs6zKhY63VwUB7QdXvuf96A45gmuh5UPw'
socketio = SocketIO(app)
google_places = GooglePlaces(YOUR_API_KEY)

# You may prefer to use the text_search API, instead.
# If types param contains only 1 item the request to Google Places API
# will be send as type param to fullfil:
# http://googlegeodevelopers.blogspot.com.au/2016/02/changes-and-quality-improvements-in_16.htm

@app.route('/')
def index():
	return render_template('index.html')

@socketio.on('connect')
def connection():
	emit('welcome')

@socketio.on('chat')
def chat(message):

	query_result = google_places.nearby_search(
        location=message['body'].split(), keyword='Gym',
        radius=int(message['body'].split()[-1]))

	emit('chat', {'body': message['body']}, broadcast=True)
	if query_result.has_attributions:
	    print query_result.html_attributions

	# print google_places.get_place(location=location)
	# print "!!!!!!!!!!!!!!!!!!!!!!"

	for place in query_result.places:
	    # Returned places from a query are place summaries.
	    print place.name
	    message['body'] = place.name
	    emit('chat', {'body': message['body']}, broadcast=True)
	    print place.geo_location
	    print place.place_id

	    # The following method has to make a further API call.
	    place.get_details()
	    # Referencing any of the attributes below, prior to making a call to
	    #get_details()# will raise a googleplaces.GooglePlacesAttributeError.
	    print place.details # A dict matching the JSON response from Google.
	    print place.local_phone_number
	    message['body'] = place.local_phone_number
	    emit('chat', {'body': message['body']}, broadcast=True)
	    print place.international_phone_number
	    message['body'] = place.international_phone_number
	    emit('chat', {'body': message['body']}, broadcast=True)
	    print place.website
	    message['body'] = place.website
	    emit('chat', {'body': message['body']}, broadcast=True)
	    website = place.url
	    message['body'] = place.url
	    emit('chat', {'body': message['body']}, broadcast=True)

	    message['body'] = '--------------------------------------------'
	    emit('chat', {'body': message['body']}, broadcast=True)
	print 'Done' 
	
	    # Getting place photos

	    # for photo in place.photos:
	    #     # 'maxheight' or 'maxwidth' is required
	    #     photo.get(maxheight=500, maxwidth=500)
	    #     # MIME-type, e.g. 'image/jpeg'
	    #     photo.mimetype
	    #     # Image URL
	    #     photo.url
	    #     # Original filename (optional)
	    #     photo.filename
	    #     # Raw image data
	    #     photo.data
	
if __name__ == '__main__':
	port = int(os.environ.get("PORT", 8000))
	socketio.run(app, port=port)

