import json
from web3 import Web3, HTTPProvider
import yaml

with open("../config.yaml", "r") as yamlfile:
    data = yaml.load(yamlfile, Loader=yaml.FullLoader)
    print("Config read successful")

# truffle development blockchain address
blockchain_address = data["blockchain_address"]
# Client instance to interact with the blockchain
web3 = Web3(HTTPProvider(blockchain_address))
# Set the default account (so we don't need to set the "from" for every transaction call)
web3.eth.defaultAccount = web3.eth.accounts[0]

# Path to the compiled contract JSON file
compiled_contract_path = data["compiled_contract_path"]
# Deployed contract address (see `migrate` command output: `contract address`)
deployed_contract_address = data["deployed_contract_address"]

with open(compiled_contract_path) as file:
    contract_json = json.load(file)  # load contract info as JSON
    contract_abi = contract_json['abi']  # fetch contract's abi - necessary to call its functions

# Fetch deployed contract reference
contract = web3.eth.contract(address=deployed_contract_address, abi=contract_abi)

# Call contract function (this is not persisted to the blockchain)
tx_hash = contract.functions.setFootprint("16159988945","121125").transact()
tx_receipt = web3.eth.waitForTransactionReceipt(tx_hash)
print('tx_hash: {}'.format(tx_hash.hex()))

station = contract.functions.getLastStation().call()
print("Last station: ",station)
timestamp = contract.functions.getLastTimestamp().call()
print("Last timestamp: ",timestamp)
