import argparse
from googleapiclient import discovery
import httplib2
import json
from oauth2client.client import GoogleCredentials

DISCOVERY_URL = ('https://{api}.googleapis.com/'
	'$discovery/rest?version={apiVersion}')
#https://www.googleapis.com/robot/v1/metadata/x509/dubhacks%40thinkagain-146603.iam.gserviceaccount.com
def main(movie_review_file):
	'''Run a sentiment analysis request on text within a passed filename'''

	http = httplib2.Http()

	credentials = GoogleCredentials.get_application_default().create_scoped(
		['https://www.googleapis.com/auth/cloud-platform'])
	http = httplib2.Http()
	credentials.authorize(http)

	service = discovery.build('language', 'v1beta1',
		http = http, discoveryServiceUrl = DISCOVERY_URL)

	review_text = open(movie_review_file, 'r')
	service_request = service.documents().analyzeSentiment(
    	body={
      	'document': {
        	'type': 'PLAIN_TEXT',
         	'content': review_text.read(),
      		}
    	})


	response = service_request.execute()
	polarity = response['documentSentiment']['polarity']
	magnitude = response['documentSentiment']['magnitude']
	print "Sentiment: polarity of %s with magnitude of %s" % (polarity, magnitude)
	return 0

if __name__ == '__main__':
	parser = argparse.ArgumentParser()
	parser.add_argument(
		'movie_review_file', help = 'The filename of the movie review you\'d like to analyze')
	args = parser.parse_args()
	main(args.movie_review_file)	