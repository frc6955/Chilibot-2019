$(document).ready(function() {
    /**
     * Codigo de inicializacion. Aqui se prepara WebSockets, Plotly.JS y los Canvas
     */
    // Obtain canvas object for battery voltage display
    var canvasBattery = document.getElementById("battery-animation");
    var canvas2DBattery = canvasBattery.getContext("2d");
    // Connect to websockets server with namespace /webui
    namespace = '/webui';
    var socket = io.connect(location.protocol + '//' + document.domain + ':' + location.port + namespace);
    // Create a Plotly.JS graph to display stacked currents
    var arrayLength = 30
    var currentTraces = {
        'arm': new Array(arrayLength).fill(0),
        'chassis': new Array(arrayLength).fill(0)
    }
    // var armTrace = new Array(arrayLength).fill(0);
    Plotly.newPlot('graph', [
        {
            y: currentTraces.arm, stackgroup: 'one'
        }, 
        {
            y: currentTraces.chassis, stackgroup: 'one'
        },
        {
            y:currentTraces.intake, stackgroup: 'one'
        }
    ]);

    /**
     * Codigo de listeners. Aqui se declaran los listeners de eventos de WebSockets. Los listeners disponibles son:
     *  - 'connect': Este evento se activa cuando un cliente logra una conexion exitosa por WS al servidor
     *  - 'connection_confirmed': Evento generado por el servidor luego de reconocer una conexion exitosa
     *  - 'receive_data': Evento generador por servidor para enviar informacion. Debiese ser separado en un evento
     *                    rapido y uno lento (2-4 Hz vs 20Hz de refresco)
     */
    // On connect listener: Triggers upon a client succesfully connecting to the server
    socket.on('connect', function() {
        //socket.emit('connect_event', {});
    })
    // connection_confirmed listener: Emitted by the serving for acknowledging a succesful connection
    socket.on('connection_confirmed', function(msg) {
        $('#log').append('<p>Num conexion: ' + msg['client_id'] + '</p>');
    });
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

    socket.on('receive_data_time', function(payload){
        var number = payload['data'];
        var seconds = Math.floor(number % 60);
        var minutes = Math.floor((number / 60) % 60);
        var minuteSpan = $('#clockdiv').find('.minutes');
        var secondSpan = $('#clockdiv').find('.seconds');
        minuteSpan.html(minutes);
        secondSpan.html(seconds);
    });

    socket.on('receive_data_angle', function(payload){
        var number = parseFloat(payload['data']);
        $("#imgMagnet").rotate(number);
    });

    socket.on('receive_data_all_currents', function(payload){
        var currentsJSONString = payload['data'];
        var currentsJSON = JSON.parse(currentsJSONString);
        // console.log(currentsJSON);

        currentTraces.arm = currentTraces.arm.concat(currentsJSON.arm);
        currentTraces.arm.splice(0, 1);

        currentTraces.chassis = currentTraces.chassis.concat(currentsJSON.chassis);
        currentTraces.chassis.splice(0,1);

        currentTraces.intake = currentTraces.intake.concat(currentsJSON.intake);
        currentTraces.intake.splice(0,1);
        Plotly.update('graph', {
            y: [
                currentTraces.arm,
                currentTraces.chassis,
                currentTraces.intake
            ]
        });
    });
});