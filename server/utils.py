from yaml.loader import SafeLoader
from datetime import datetime
from os import times
from influxdb_client import InfluxDBClient, Point, WritePrecision
from influxdb_client.client.write_api import SYNCHRONOUS
import yaml
import json
import geopy.distance

def push_data(data):

    token = "gaOjKsX3Hqt21_tsgJ4W5IDu7kKTiltXGwj-tQPFTPV7tEMufxGViDa9VW9cusc-5jLcXnarVoo0r46eR787yA=="
    org = "st175600@stud.uni-stuttgart.de"
    bucket = "blockchain"
    default_coords = (48.74864645560312, 9.109050292535414)
    # test_data = '{"id":"QR_W0-G123-RLT012-TAG001","timestamp":"2022-07-07 14:51:26.81","location":"48.74869715710554;9.108480125963736"}'

    client = InfluxDBClient(url="https://eu-central-1-1.aws.cloud2.influxdata.com", token=token, org=org)
    write_api = client.write_api(write_options=SYNCHRONOUS)

    data_object = json.loads(data)

    location = data_object["location"].split(";")
    location_coords = (float(location[0]), float(location[1]))
    is_valid = False
    if geopy.distance.geodesic(location_coords, default_coords).km < 0.5:
        is_valid = True

    point = Point("attendance") \
        .tag("machine_id", data_object["id"]) \
        .tag("scanned_time", data_object["timestamp"]) \
        .tag("valid", is_valid) \
        .field("lat", float(location[0])) \
        .field("lon", float(location[1]))

    write_api.write(bucket, org, point)

    client.close()


def read_config_file():
    with open('config.yaml', "r") as yamlfile:
        data = yaml.load(yamlfile, Loader=yaml.FullLoader)
        print("Config read successful")
        return data
