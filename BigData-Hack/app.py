import os

from flask import Flask, render_template, request


app = Flask(__name__, instance_relative_config=True)
# Get default config (main app dir config.py) or environment variables
app.config.from_object('config')
# Get instance config (hidden from git, is in app dir/instance/config.py)
# Overrides default config.py and environment variables settings
try:
    app.config.from_pyfile('config.py')
except IOError:
    pass

# Get keys from the config (not tracked by source)
OBA_KEY = app.config['OBA_KEY']
MAPBOX_TOKEN = app.config['MAPBOX_TOKEN']
API_URL = app.config['API_URL']


@app.route('/')
def index():
    return render_template('index.html')

@app.route('/map', methods=['GET'])
def map():
    location_args = {}
    if 'stop_id' in request.args:
        try:
            # TODO: Make this happen in javascript - pass the argument
            # to the template and do an AJAX request

            # HACK: This only works for King County Metro (the 1_ prefix)
            stop_id = "1_" + str(request.args.get('stop_id'))
            # Find the LatLng of the stop's ID
            oba_url = 'http://api.pugetsound.onebusaway.org/api'
            r = requests.get('{}/where/stop/{}.json'.format(oba_url, stop_id),
                             params={'key': OBA_KEY})
            data_entry = r.json()['data']['entry']
            location_args['lat'] = data_entry['lat']
            location_args['lon'] = data_entry['lon']
        except:
            # FIXME: Fail gracefully and catch a proper exception
            pass

    return render_template('map.html', location_args=location_args,
                           mapbox_token=MAPBOX_TOKEN, api_url=API_URL)


@app.route('/report', methods=['GET'])
def report():
    location_args = {'lat': 0, 'lon': 0}
    if 'lat' in request.args and 'lon' in request.args:
        try:
            location_args['lat'] = request.args.get('lat')
            location_args['lon'] = request.args.get('lon')
        except:
            # FIXME: Fail gracefully and catch a proper exception
            pass
    return render_template('report.html', location=location_args)


@app.route('/report-construction')
def construction():
    return render_template('report-construction.html')


@app.route('/report-incline')
def incline():
    return render_template('report-incline.html')


@app.route('/report-elevator')
def elevator():
    return render_template('report-elevator.html')


@app.route('/report-ramp')
def ramp():
    return render_template('report-ramp.html')


@app.route('/report-stairs')
def stairs():
    return render_template('report-stairs.html')


@app.route('/report-other')
def other():
    return render_template('report-other.html')


@app.route('/report-submitted', methods=['POST'])
def submit():
    obstacleType = request.form['type']
    description = request.form['description']
    lat = request.form['lat']
    lon = request.form['lon']
    #addToUserReportedDb(obstacleType, description, lat, lon)
    return render_template('report-submitted.html')


def addToUserReportedDb(obstacleType, description, lat, lon):
    new_entry = {'type': 'Feature',
                 'properties': {'type': obstacleType,
                                'description': description},
                 'geometry': {'type': 'Point',
                              'coordinates': [lon, lat]}}
    new_entry_s = str(new_entry)
    if not os.path.exists('static/data/userReported.json'):
        f = open('static/data/userReported.json', 'w+')
        f.write("["+new_entry_s+"]")
        f.close()
    else:
        f = open('static/data/userReported.json', 'r+')
        f.seek(-1, 2)  # write over the previous ]
        f.write(","+new_entry_s+"]")
        f.close()

if __name__ == '__main__':
    port = int(os.environ.get('PORT', 5000))
    app.run(host='0.0.0.0', port=port, debug=True)
