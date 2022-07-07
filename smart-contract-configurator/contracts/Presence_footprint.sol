// SPDX-License-Identifier: MIT
pragma solidity >=0.4.22 <0.9.0;

contract Presence_footprint {

    struct footprint{
        uint timestamp;
        uint station;
    }

    footprint[] public footprints;

    // constructor() public {
		//     footprints[0] = footprint({
    //         timestamp:10, station: 1
    //     });
	  // }

    function setFootprint(uint timestamp, uint station) public {
      // user_footprint = footprint({
      //       timestamp:timestamp, station: station
      //   });
      footprints.push(footprint({
            timestamp:timestamp, station: station
        }));

    }

    function getStation(uint id) public returns (uint) {
        return (footprints[id].station);
    }

    function getTimestamp(uint id) public returns (uint) {
        return (footprints[id].timestamp);
    }

    // function set(uint timestamp, uint station_id) public {
    //   last_presence_timestamp = timestamp;
    //   last_station_id = station_id;
    // }

    // function get() public returns (uint, uint) {
    //   return (last_presence_timestamp, last_station_id);
    // }

}
