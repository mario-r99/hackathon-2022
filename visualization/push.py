from datetime import datetime
import json
from os import times
from influxdb_client import InfluxDBClient, Point, WritePrecision
from influxdb_client.client.write_api import SYNCHRONOUS

def push_data(data):
    token = "gaOjKsX3Hqt21_tsgJ4W5IDu7kKTiltXGwj-tQPFTPV7tEMufxGViDa9VW9cusc-5jLcXnarVoo0r46eR787yA=="
    org = "st175600@stud.uni-stuttgart.de"
    bucket = "blockchain"
    # test_data = '{"id":"QR_W0-G123-RLT012-TAG001","timestamp":"2022-07-07 14:51:26.81"}'

    client = InfluxDBClient(url="https://eu-central-1-1.aws.cloud2.influxdata.com", token=token, org=org)
    write_api = client.write_api(write_options=SYNCHRONOUS)

    data_object = json.loads(data)

    # point = Point("mem") \
    #         .tag("host", "host1") \
    #         .field("used_percent", 23.43234543) \
    #         .time(datetime.utcnow(), WritePrecision.NS)

    # timestamp = datetime.strptime(data_object["timestamp"], "%Y-%m-%d %H:%M:%S.%f")

    point = Point("attendance") \
        .tag("machine_id", data_object["id"]) \
        .tag("scanned_time", data_object["timestamp"]) \
        .field("field", 0) \

    write_api.write(bucket, org, point)

    client.close()

