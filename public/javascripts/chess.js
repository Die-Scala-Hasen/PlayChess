// $('document').ready(function(){
//     console.log("starting Chess game");
//     start();
// });
start();
var socket;
function start() {
    socket = new WebSocket("ws://localhost:9000/socket");
    socket.onmessage = function (event) {
        var json = event.data;
        // var json = JSON.parse(data);
        console.log(json);
    };
    socket.onopen = function(event) {
        socket.send(JSON.stringify({msg: "HelloFromBrowser"}));
        console.log("socket opened");
    };
    socket.onclose = function(event) {
        console.log("socket closed");
    };
    socket.onerror = function(event) {
        console.log("got socket error");
    };
}
