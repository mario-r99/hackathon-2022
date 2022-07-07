import flask
import requests
from flask import request
import json
from blockchainer import *
from utils import *
from ..visualization.push import push_data

app = flask.Flask(__name__)

#to display the connection status
@app.route('/', methods=['GET'])
def handle_call():
    return "Successfully Connected"

# this must be used for further improvement
@app.route('/getlast', methods=['GET'])
def get_last_block():
    config = read_config_file()
    station, timestamp = get_last_values_from_blockchain(config)
    data = "{\"id\":\"" + str(station)+"\",\"timestamp\":\"" + str(timestamp) + "\"}"
    return data

#the post method. when we call this with a string containing a name, it will return the name with the text "I got your name"
@app.route('/scanner', methods=['POST'])
def extract_data():
    global data

    ## Writing data
    data = request.get_json()
    config = read_config_file()
    persist_in_blockchain(config, data["id"], data["timestamp"])

    ## Reading data
    station, timestamp = get_last_values_from_blockchain(config)
    data = "{\"id\":\"" + str(station)+"\",\"timestamp\":\"" + str(timestamp) + "\"}"
    push_data(data)

    return "OK"

#this commands the script to run in the given port
if __name__ == '__main__':
    # app.run(host="0.0.0.0", port=5000, debug=True)
    app.debug = True
    app.run(host="0.0.0.0")