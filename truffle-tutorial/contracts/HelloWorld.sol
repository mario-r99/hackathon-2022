// pragma solidity >= 0.5.0 < 0.7.0;

// contract HelloWorld {
//     function sayHello() public pure returns (string memory) {
//         return 'Hello World!';
//     }
// }

 pragma solidity >= 0.5.0;

 contract HelloWorld {
    string public payload;

    function setPayload(string memory content) public {
        payload = content;
    }

     function sayHello() public pure returns (string memory) {
         return 'Hello World!';
     }
 }