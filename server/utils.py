import yaml
from yaml.loader import SafeLoader

def read_config_file():
    with open('config.yaml', "r") as yamlfile:
        data = yaml.load(yamlfile, Loader=yaml.FullLoader)
        print("Config read successful")
        return data
