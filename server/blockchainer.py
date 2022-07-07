import json
from web3 import Web3, HTTPProvider
import web3
import yaml

def setup_blockchain(config):
    # truffle development blockchain address
    blockchain_address = config["blockchain_address"]
    # Client instance to interact with the blockchain
    web3 = Web3(HTTPProvider(blockchain_address))
    # Set the default account (so we don't need to set the "from" for every transaction call)
    web3.eth.defaultAccount = web3.eth.accounts[0]

    # Path to the compiled contract JSON file
    compiled_contract_path = config["compiled_contract_path"]
    # Deployed contract address (see `migrate` command output: `contract address`)
    deployed_contract_address = config["deployed_contract_address"]

    with open(compiled_contract_path) as file:
        contract_json = json.load(file)  # load contract info as JSON
        contract_abi = contract_json['abi']  # fetch contract's abi - necessary to call its functions

    # Fetch deployed contract reference
    contract = web3.eth.contract(address=deployed_contract_address, abi=contract_abi)
    return contract

def persist_in_blockchain(config, station, timestamp):
    contract = setup_blockchain(config)

    tx_hash = contract.functions.setFootprint(station, timestamp).transact()
    print('tx_hash: {}'.format(tx_hash.hex()))

def get_last_values_from_blockchain(config):
    contract = setup_blockchain(config)

    # Call contract function (this is not persisted to the blockchain)
    station = contract.functions.getLastStation().call()
    print("Last station: ",station)
    timestamp = contract.functions.getLastTimestamp().call()
    print("Last timestamp: ",timestamp)

    return station, timestamp

# if __name__ == "__main__":

#     ### get data method
#     station="jkcsnlds2323"
#     timestamp="scoicsjcp34"

    

#     persist_in_blockchain(config, station, timestamp)
#     last_station, last_timestamp = get_last_values_from_blockchain(config)