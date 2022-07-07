import flask
import requests
from flask import request
import json

app = flask.Flask(__name__)

#to display the connection status
@app.route('/', methods=['GET'])
def handle_call():
    return "Successfully Connected"

#the get method. when we call this, it just return the text "Hey!! I'm the fact you got!!!"
@app.route('/getfact', methods=['GET'])
def get_fact():
    return "1856-5423"

#the post method. when we call this with a string containing a name, it will return the name with the text "I got your name"
@app.route('/scanner', methods=['POST'])
def extract_data():
    global data
    data = request.get_json()
    print(data)
    #with open('mysite/java.json', 'w') as outfile:
    #    json.dump(data, outfile)
    return "OK"

#this commands the script to run in the given port
if __name__ == '__main__':
    # app.run(host="0.0.0.0", port=5000, debug=True)
    app.debug = True
    app.run(host="0.0.0.0")