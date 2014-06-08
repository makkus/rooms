/**
 * Created by markus on 29/05/14.
 */
var stompClient = null;

function connect() {
    var socket = new SockJS('http://localhost:8084/hello');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/light_change', function (greeting) {
            var light_msg = JSON.parse(greeting.body)
            lightChanged(JSON.parse(greeting.body).key);
        });
    });
}


function sendName() {
    var name = document.getElementById('name').value;
    stompClient.send("/app/hello", {}, JSON.stringify({ 'on': true }));
}

function lightChanged(message) {
    var response = document.getElementById('response');
    var p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    p.appendChild(document.createTextNode(message));

    response.appendChild(p);
}
