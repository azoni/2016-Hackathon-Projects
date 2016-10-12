# Hack the Commute: Bus Stop Accessibility

This app was developed to help people with mobility challenges plan their routes in Seattle, taking into account their specific accessibility needs.  It was first created for the Hack the Commute hackathon in April 2015. Today, the app allows users to check the terrain around the bus stop for accessibility issues, report obstacles and verify information contribued by other users. The future goal is to allow users to search for an accesible route based on their preferences.

Our app is live at [http://hackcessible.herokuapp.com/](http://hackcessible.herokuapp.com/).

Here's a pretty picture:
![Screenshot of Application](screenshot.png "Hackcessible Transit App")

## Challenge and Approach

Our submission at the hackathon was for [Challenge #2: Improve the commuter experience in any single mode](http://tmpl.at/1EykAbV).

Today, a person with mobility challenges does not have a full or unified access to up-to-date information about upgraded ramp curbs, sidewalk closures, road construction, steep hills or other obtacles that can block their mobility.

We believe that every person in Seattle should enjoy freedom and convenience of commuting.

**Design question**:

_How can we help people who use wheelchairs find routes tailored to their needs? How can we improve their commute experience in Seattle?_

Our approach for satisfying this challenge was to:

- Determine obstacles faced by people in wheelchairs when navigating their commute
- Create a design schema to easily convey obstacles to users
- Enhance information available in OneBusAway using open data sets
- Allow community reporting to improve adaptability and increase data size

Once we did that, we knew we could make a change for someone's life!

## Team Members

Our team is comprised of:

- [@Azoni](https://github.com/azoni) - UWT Student (Charlton Smith)
- [@vinhdesail](https://github.com/vinhdesail) - UWT Student (Vinh Vien)
- [@BrunoCode](https://github.com/BrunoCode) - UWT Student (Luis Solis)
- [@cinwan12](https://github.com/cinwan12) - UWT Student (Cindy Wang)
- [@RhaydenX](https://github.com/RhaydenX) - UWT Student (Alex Lambert)

## Technologies, APIs, and Datasets Utilized

We made use of:

- Python, Flask server, JavaScript, CSS, HTML 5
- [Sidewalk Data Set](https://data.seattle.gov/Transportation/SDOT-Sidewalks/pxgh-b4sz) from City of Seattle Department of Transportation: curb and sidewalk data
- [Google Maps Elevation API](https://developers.google.com/maps/documentation/elevation/): elevation data)
- [One Bus Away API](http://pugetsound.onebusaway.org/p/OneBusAwayApiService.action): bus stop information


## Contributing

#### Installation and Setup
Hackcessible is based on the Flask web framework using python 2.7. requirements.txt contains the list of pip-installable requirements:
* Flask >= 0.10
* Flask-WTF >= 0.11
* requests >= 2.6.0

To run the app:
1. Set a Mapbox API Token, OneBusAway API Key, and the location of the hackcessibleapi site. These can be defined either as environment variables or as config variables of the same name in instance/config.py (the latter will take precedence - if both environment variables and the config are present, the config's values are used). The variables have these titles:
    MAPBOX_TOKEN
    OBA_KEY
    API_URL
2. Get the elevation and sidewalk data. To save on bandwidth, requests, and client-side computation, we do not query Google Maps or the SDOT data set on the client side (javascript) and instead store the preprocessed data on the server. It is currently not tracked in git and must be downloaded separately (contact one of the contributors to get it). We are quickly moving to having a RESTful API to make accessing the data easier.
3. Run `python app.py`. This will use the Werkzeug server, which should not be used in production. We do not currently use gunicorn (as much is in flux), but will do so in the future.

#### Using the website

1. Visit [http://hackcessible.herokuapp.com/](http://hackcessible.herokuapp.com/)
2. Enter a bus stop number or click "Show Map of Current Location"
3. Use the filters at the bottom of the page to select the obstacles you wish to see
4. Longpress (click and hold without moving the mouse to report a new obstacle

Our code is licensed under the [MIT License](LICENSE.md). Pull requests will be accepted to this repo, pending review and approval.
