# Hackathon 2022
## Installation Instructions

### General Setup
- Clone this project:
`git clone https://github.com/mario-r99/hackathon-2022.git`

### Android Scanner App
1. Install [Android Studio](https://developer.android.com/studio)
2. Open 'scanner' folder as project
3. Build and run app on connected device or emulator

### Important files
- scanner\app\src\main\java\com\hackathon2022\scanner\ThirdFragment.kt
  - val serverUrl: Server URL for connection to Blockchain API Server
  - var latitude: Default latitude for smartphone location
  - var longitude: Default longitude for smartphone location

### Blockchain API Server
1. Install [Visual Studio Code](https://code.visualstudio.com)
2. Install [Python](https://www.python.org/) version 3.8
3. Open 'server' folder in VS Code
4. Start run script: `python server.py`
5. Set up the needed configuration to connect to the blockchain in config.yaml file.
6. Optional: Install [Postman](https://www.postman.com/) for Debugging HTTP requests

#### Important files
- server\utils.py
  - token: API token for connection to InfluxDB Cloud
  - org: InfluxDB organization
  - bucket: Default InfluxDB bucket
  - url: URL for connection to InfluxDB Cloud
  - default_coords: Default coordinates for geolocation check
  
### Blockchain
1. Install [Ganache](https://trufflesuite.com/ganache/)
2. Install [NodeJS](https://nodejs.org/en/) for node package manager (npm)
3. Install Truffle: `npm install truffle`
4. To connect Truffle smart-contract building with Granache Blockchain use the truffle-config.js file.
5. Build contracts: `truffle migrate`

### Dashboard
- InfluxDB runs currently on AWS Cloud
- Can also be hosted on any device, just URL and token has to be updated
