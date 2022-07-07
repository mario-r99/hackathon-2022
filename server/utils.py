import yaml
from yaml.loader import SafeLoader

def read_config_file():
    with open('config.yml') as f:
        data = yaml.loader(f, Loader=SafeLoader)
        print(data)
        return data