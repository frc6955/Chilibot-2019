$(document).ready(function() {
    /**
     * Codigo de inicializacion. Aqui se prepara WebSockets, Plotly.JS y los Canvas
     */
    // Obtain canvas object for battery voltage display
    var canvasBattery = document.getElementById("battery-animation");
    var canvas2DBattery = canvasBattery.getContext("2d");
    // Connect to websockets server with namespace /webui
    namespace = '/webui';
    var socket = io.connect(location.protocol + '//' + document.domain + ':' + location.port + namespace, { 'sync disconnect on unload': true });
    // Create a Plotly.JS graph to display stacked currents
    var arrayLength = 30;
    var currentTraces = {
        'arm': new Array(arrayLength).fill(0),
        'chassis': new Array(arrayLength).fill(0),
        'intake': new Array(arrayLength).fill(0)
    };
    Plotly.newPlot('graph', [
        { y: currentTraces.arm, stackgroup: 'one', name: 'Arm' }, 
        { y: currentTraces.chassis, stackgroup: 'one', name: 'Chassis' },
        { y: currentTraces.intake, stackgroup: 'one', name: 'Intake' }
    ], {
        title: "Current consumption by subsystem",
        plot_bgcolor: "#273674",
        paper_bgcolor: "#516ed0",
        font: {
            family: "'Open-Sans', sans-serif",
            size: 16,
            color: "#fff"
        }
    });

    /**
     * Codigo de listeners. Aqui se declaran los listeners de eventos de WebSockets. Los listeners disponibles son:
     *  - 'connect': Este evento se activa cuando un cliente logra una conexion exitosa por WS al servidor
     *  - 'connection_confirmed': Evento generado por el servidor luego de reconocer una conexion exitosa
     *  - 'receive_data': Evento generador por servidor para enviar informacion. Debiese ser separado en un evento
     *                    rapido y uno lento (2-4 Hz vs 20Hz de refresco)
     */
    // On connect listener: Triggers upon a client succesfully connecting to the server
    socket.on('connect', function() {
        $("#connection_state").text('Connected');
        $("#connection_state").css('color', 'green');
        // Battery canvas animation: Create battery on connect, set apparent voltage to 1V
        socket.emit('broadcast', {
            'event': "receive_data_battery",
            'data': 1
        });
        socket.emit('broadcast', {
            'event': "receive_data_time",
            'data': -1
        });
    })

    // On disconnect listener: Triggers upon a client disconnecting from the server
    socket.on('disconnect', function() {
        $("#connection_state").text('Disconnected');
        $("#connection_state").css('color', 'red');
    })

    // Show number of clients alongside connection state
    socket.on('client_number', function(payload) {
        var new_text = $('#connection_state').text();
        if (new_text[0] == 'C') {
            new_text = "Connected (" + payload['data'] + ')';
        } else if (new_text[1] == 'D') {
            new_text = "Disconnected (" + payload['data'] + ')';
        } else {
            new_text = "Error";
        }
        $("#connection_state").text(new_text);
    })

    // receive_data listener: Emitted async by the server to transmit information from simulations or roboRIO
    socket.on("receive_data_battery", function (payload){
        // The msg variable is the dictionary payload and contains the keys specified by the server
        var number = payload["data"];
        // Battery canvas animation: Clear canvas, draw battery shape, draw 'charge' of battery proportionally
        canvas2DBattery.clearRect(0, 0, 275, 120);
        canvas2DBattery.fillStyle = "#000000";
        canvas2DBattery.fillRect(0, 0, 260, 120);
        canvas2DBattery.fillRect(260, 40, 15, 50)
        canvas2DBattery.fillStyle = "#40FF00";
        canvas2DBattery.fillRect(10, 10, (240 / 13) * number, 100);
    });

    // receive_data_time listener: Displays time left in match in seconds
    socket.on('receive_data_time', function(payload){
        var number = payload['data'];
        if (number != -1) {
            var seconds = Math.floor(number % 60);
            var minutes = Math.floor((number / 60) % 60);
        } else {
            var seconds = 0;
            var minutes = 0;
        }
        var minuteSpan = $('#clockdiv').find('.minutes');
        var secondSpan = $('#clockdiv').find('.seconds');
        minuteSpan.html(minutes);
        secondSpan.html(seconds);
    });
    // receive_data_angle listener: Displays robot orientation
    socket.on('receive_data_angle', function(payload){
        var number = parseFloat(payload['data']);
        $("#imgMagnet").rotate(number);
    });

    socket.on('receive_data_all_currents', function(payload){
        var currentsJSONString = payload['data'];
        if (typeof currentsJSONString == "string") {
            var currentsJSON = JSON.parse(currentsJSONString);
        } else {
            var currentsJSON = currentsJSONString;
        }
        currentTraces.arm = currentTraces.arm.concat(currentsJSON.arm);
        currentTraces.arm.splice(0, 1);
        currentTraces.chassis = currentTraces.chassis.concat(currentsJSON.chassis);
        currentTraces.chassis.splice(0,1);
        currentTraces.intake = currentTraces.intake.concat(currentsJSON.intake);
        currentTraces.intake.splice(0,1);
        if (currentsJSON.arm + currentsJSON.chassis + currentsJSON.intake > 100) {
            var layout = { plot_bgcolor: "#c10d0d" };
        } else {
            var layout = { plot_bgcolor: "#273674" };
        }
        Plotly.update('graph', {
            y: [
                currentTraces.arm,
                currentTraces.chassis,
                currentTraces.intake
            ]
        }, layout);
    });
});