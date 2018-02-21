var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    stompClient = Stomp.client('ws://localhost:8080/gs-guide-websocket');
    stompClient.reconnect_delay = 5000;
    stompClient.connect({
                'login' : $("#name").val(),
                'passcode' : $("#name").val()
            }
         , function (frame) {

            whoami = frame.headers['user-name'];
            console.log('whoami: ', whoami);
            console.log('Connected: ', frame);

            setConnected(true);
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/greetings', function (greeting) {
                showGreeting(JSON.parse(greeting.body).content);
            });
            stompClient.subscribe("/user/queue/reply", function (reply) {
            console.log("reply", reply)
               showGreeting("Whistle>> " + JSON.parse(reply.body).content);
            });
            stompClient.subscribe("/user/queue/error", function (error) {
               showGreeting("Error>> " + JSON.parse(error.body).content);
            });
        }
        , function(e){
            console.log('error. ', e)
        }
    );

}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function sendWhistle(username) {
    stompClient.send("/app/whistle", {}, JSON.stringify({'name': 'This is only to ', "to" : username }));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
    $( "#whistle" ).click(function() { sendWhistle('song'); });
});

