// SPDX-License-Identifier: MIT
pragma solidity >=0.4.22 <0.9.0;

contract Presence_footprint {

    struct footprint{
        string timestamp;
        string station;
    }

    footprint[] public footprints;

    function setFootprint(string memory timestamp, string memory station) public {
      footprints.push(footprint({
            timestamp:timestamp, station: station
        }));
    }

    function getLastStation() public returns (string memory) {
        return (footprints[footprints.length-1].station);
    }

    function getLastTimestamp() public returns (string memory) {
        return (footprints[footprints.length-1].timestamp);
    }

}
