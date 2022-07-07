import json

from web3 import Web3, HTTPProvider

def dummy_block(id, timestamp):
    print("id: ", id)
    print("timestamp: ", timestamp)


def create_block(id, timestamp):
    # truffle development blockchain address
    blockchain_address = 'http://127.0.0.1:7548'
    # Client instance to interact with the blockchain
    web3 = Web3(HTTPProvider(blockchain_address))
    # Set the default account (so we don't need to set the "from" for every transaction call)
    web3.eth.defaultAccount = web3.eth.accounts[0]

    # Path to the compiled contract JSON file
    compiled_contract_path = '../smart-contract-configurator/build/contracts/Presence_footprint.json'
    # Deployed contract address (see `migrate` command output: `contract address`)
    deployed_contract_address = '0x3aB88f35a15c6003Ed0043f1f0b3496Ba671505b'

    with open(compiled_contract_path) as file:
        contract_json = json.load(file)  # load contract info as JSON
        contract_abi = contract_json['abi']  # fetch contract's abi - necessary to call its functions

    # Fetch deployed contract reference
    contract = web3.eth.contract(address=deployed_contract_address, abi=contract_abi)

    # Call contract function (this is not persisted to the blockchain)

    tx_hash = contract.functions.setFootprint(16159988944,121124).transact()
    tx_receipt = web3.eth.waitForTransactionReceipt(tx_hash)
    print('tx_hash: {}'.format(tx_hash.hex()))

    station = contract.functions.getStation(0).call()
    print(station)
    timestamp = contract.functions.getTimestamp(1).call()
    print(timestamp)

def get_block():

    # truffle development blockchain address
    blockchain_address = 'http://127.0.0.1:7548'
    # Client instance to interact with the blockchain
    web3 = Web3(HTTPProvider(blockchain_address))
    # Set the default account (so we don't need to set the "from" for every transaction call)
    web3.eth.defaultAccount = web3.eth.accounts[0]

    # Path to the compiled contract JSON file
    compiled_contract_path = '../smart-contract-configurator/build/contracts/Presence_footprint.json'
    # Deployed contract address (see `migrate` command output: `contract address`)
    deployed_contract_address = '0x3aB88f35a15c6003Ed0043f1f0b3496Ba671505b'

    with open(compiled_contract_path) as file:
        contract_json = json.load(file)  # load contract info as JSON
        contract_abi = contract_json['abi']  # fetch contract's abi - necessary to call its functions

    # Fetch deployed contract reference
    contract = web3.eth.contract(address=deployed_contract_address, abi=contract_abi)

    # Call contract function (this is not persisted to the blockchain)

    station = contract.functions.getStation(0).call()
    print(station)
    timestamp = contract.functions.getTimestamp(1).call()
    print(timestamp)

    data = (station, timestamp)

    return data