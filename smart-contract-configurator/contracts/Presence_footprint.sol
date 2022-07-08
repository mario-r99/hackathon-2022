// SPDX-License-Identifier: MIT
pragma solidity >=0.4.22 <0.9.0;

contract Presence_footprint {

    struct footprint{
        string timestamp;
        string station;
        string location;
    }

    footprint[] public footprints;

    function setFootprint(string memory timestamp, string memory station, string memory location) public {
      footprints.push(footprint({
            timestamp:timestamp, station: station, location:location
        }));
    }

    function getLastStation() public returns (string memory) {
        return (footprints[footprints.length-1].station);
    }

    function getLastTimestamp() public returns (string memory) {
        return (footprints[footprints.length-1].timestamp);
    }

    function getLastLocation() public returns (string memory) {
        return (footprints[footprints.length-1].location);
    }

}
