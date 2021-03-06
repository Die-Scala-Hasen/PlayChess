$('document').ready(function(){
    console.log("starting Chess game");
    start();
});
var socket;

var full = '{"gamefield":[{"figur":"bTower","x":0,"y":0},{"figur":"bKnight","x":1,"y":0},{"figur":"bBishop","x":2,"y":0},{"figur":"bQueen","x":3,"y":0},{"figur":"bKing","x":4,"y":0},{"figur":"bBishop","x":5,"y":0},{"figur":"bKnight","x":6,"y":0},{"figur":"bTower","x":7,"y":0},{"figur":"bPawn","x":0,"y":1},{"figur":"bPawn","x":1,"y":1},{"figur":"bPawn","x":2,"y":1},{"figur":"bPawn","x":3,"y":1},{"figur":"bPawn","x":4,"y":1},{"figur":"bPawn","x":5,"y":1},{"figur":"bPawn","x":6,"y":1},{"figur":"bPawn","x":7,"y":1},{"figur":"wPawn","x":0,"y":6},{"figur":"wPawn","x":1,"y":6},{"figur":"wPawn","x":2,"y":6},{"figur":"wPawn","x":3,"y":6},{"figur":"wPawn","x":4,"y":6},{"figur":"wPawn","x":5,"y":6},{"figur":"wPawn","x":6,"y":6},{"figur":"wPawn","x":7,"y":6},{"figur":"wTower","x":0,"y":7},{"figur":"wKnight","x":1,"y":7},{"figur":"wBishop","x":2,"y":7},{"figur":"wQueen","x":3,"y":7},{"figur":"wKing","x":4,"y":7},{"figur":"wBishop","x":5,"y":7},{"figur":"wKnight","x":6,"y":7},{"figur":"wTower","x":7,"y":7}]}'

var numberChar = {
    0: "-",
    1: "A",
    2: "B",
    3: "C",
    4: "D",
    5: "E",
    6: "F",
    7: "G",
    8: "H"
};

function mapper (propertyName) {
    return numberChar[propertyName];
};

function start() {
    socket = new WebSocket("ws://localhost:9000/socket");

    socket.onmessage = function (event) {

        var data = event.data;
         var json = JSON.parse(data);
        if(JSON.stringify(json) != JSON.stringify(JSON.parse(full))) {
            if(json.hasOwnProperty('gamefield')){
                var gg = document.getElementById("mydiv");
                gg.innerHTML = "";
                full = JSON.stringify(json)
                console.log("is anders")
                drawGamefield(json)
            }else{
                gameOver(json)
            }

        } else {
            if(json.hasOwnProperty('gamefield')){
                var gg = document.getElementById("mydiv");
                gg.innerHTML = "";
                console.log("is gleich")
                drawGamefield(json)
            }else{
                gameOver(json)
            }
        }
    };
    socket.onopen = function(event) {
        var maindiv = document.getElementById("mydiv");
        maindiv.className = "mydiv";

        console.log("socket opened");
    };
    socket.onclose = function(event) {
        console.log("socket closed");
    };
    socket.onerror = function(event) {
        console.log("got socket error");
    };


}

function gameOver(json) {
    alert('GAME-OVER '+json.status)
}

function drawGamefield(json) {

    for (var i = 0; i < 9; i++) {
        var zdiv = document.createElement("DIV");
        var maindiv = document.getElementById("mydiv")
        for (n = 0; n < 9; n++) {
            if(i == 0){
                var btn = document.createElement("BUTTON");
                btn.className = "Chessbtn"
                btn.backgroundColor = "white"
                    var t = document.createTextNode("" + mapper(n));
                    btn.appendChild(t);
                    zdiv.appendChild(btn);

            } else
            drawChessPattern(i,n)
        }
        maindiv.appendChild(zdiv);
    }

    placeFigure(json);
    placeSideCoordinates();


    function placeSideCoordinates() {
        for (var i = 0; i < 8; i++) {
            var btn = document.getElementById(i + "_0");
            // btn.id = i + "_A";
            btn.className = "img" + (i+1)
            btn.disabled = "disabled";
            // btn.style.backgroundColor="grey";
            // //var t = document.createTextNode(""+(i+1));
            // // btn.innerHTML = ""+(i+1);
            // btn.textContent = "test"
        }
    }

    function placeFigure(json) {
        for (var i = 0; i < json.gamefield.length; i++) {
            var y = json.gamefield[i].x
            var x = json.gamefield[i].y
            var btn = document.getElementById(x+"_"+(y+1))
            btn.className = ""+json.gamefield[i].figur
        }
    }

    function drawChessPattern(i,n) {
        if(n % 2 == 0 && i % 2 == 0) {
            innerdiv("#efe3af")
        } else if(n % 2 == 1 && i % 2 == 0) {
            innerdiv("#b97a57")
        } else if(n % 2 == 0 && i % 2 == 1) {
            innerdiv("#b97a57")
        } else {
            innerdiv("#efe3af")
        }

    }

    function innerdiv(color){
        if(i==0)
        {

        } else{
            var btn = document.createElement("BUTTON");
            btn.id = (i-1) + "_" + n;
            btn.style.backgroundColor=color;
            btn.className = "Chessbtn"
            zdiv.appendChild(btn);
        }
    }
}

