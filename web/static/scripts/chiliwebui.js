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
    // Create a Plotly.JS graph with 30 null samples
    var arrayLength = 30
    var batteryVoltageTrace = new Array(arrayLength).fill(0);
    Plotly.plot('graph', [{
        y: batteryVoltageTrace,
        mode: 'lines',
        line: {color: '#80CAF6'}
    }]);

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
        // To plot updated battery voltage trace, add new data point
        batteryVoltageTrace = batteryVoltageTrace.concat(number);
        // The remove first data point to "shift" forward
        batteryVoltageTrace.splice(0, 1);
        // Update Plotly plot on 'graph' ID with shifted array
        Plotly.update('graph', {
            y: [batteryVoltageTrace]
        });
    });
    socket.on('receive_data_time', function(payload){
        var number = payload['data'];
        console.log(number);
    });
});