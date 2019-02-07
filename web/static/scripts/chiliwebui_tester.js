$(document).ready(function() {
    // Connect to websockets server with namespace /webui
    namespace = '/webui';
    var socket = io.connect(location.protocol + '//' + document.domain + ':' + location.port + namespace);
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
    })

    socket.on('disconnect', function() {
        $("#connection_state").text('Disconnected');
        $("#connection_state").css('color', 'red');
    })


    $("#timer-test").submit(function(event) {
        event.preventDefault();
        socket.emit('broadcast', {
            'event': "receive_data_time",
            'data': $("#test-seconds").val()
        });
    });

    $("#gyro-test").submit(function(event) {
        event.preventDefault();
        socket.emit('broadcast', {
            'event': "receive_data_angle",
            'data': $("#test-angle").val()
        });
    });
});