from flask import Flask, render_template
from flask_socketio import SocketIO, emit
from flask_mqtt import Mqtt 


app = Flask(__name__)
app.config['SECRET_KEY'] = 'frc6955!'
app.config['TEMPLATES_AUTO_RELOAD'] = True
app.config['MQTT_BROKER_URL'] = 'localhost'
app.config['MQTT_BROKER_PORT'] = 1883
app.config['MQTT_USERNAME'] = ''
app.config['MQTT_PASSWORD'] = ''
app.config['MQTT_KEEPALIVE'] = 5
app.config['MQTT_TS_ENABLE'] = False
mqtt = Mqtt(app)
socketio = SocketIO(app, async_mode=None)

num_clients = 0

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/test')
def tester():
    return render_template('tester.html')
    
@socketio.on('connect', namespace='/webui')
def connect_event():
    global num_clients
    mqtt.subscribe('webui/#', qos=0)
    num_clients += 1
    print("Conexiones hasta el momento:", num_clients)
    emit('client_number', {
        'data': num_clients
    }, namespace='/webui', broadcast=True)

@socketio.on('disconnect', namespace='/webui')
def disconnect_event():
    global num_clients
    num_clients -= 1
    print("Conexiones hasta el momento:", num_clients)
    emit('client_number', {
        'data': num_clients
    }, namespace='/webui', broadcast=True)

@socketio.on('broadcast', namespace='/webui')
def broadcast_echo(payload):
    print("Received broadcast request!\n{}".format(payload))
    emit(payload['event'], {
        'data': payload['data']
    }, namespace='/webui', broadcast=True)

@mqtt.on_message()
def handle_mqtt_messages(client, userdata, message):
    if message.topic == 'webui/battery/voltage':
        socketio.emit('receive_data_battery', {'data': message.payload.decode()}, namespace='/webui')
    if message.topic == 'webui/driverstation/matchtime':
        socketio.emit('receive_data_time', {'data': message.payload.decode()}, namespace='/webui')
    if message.topic == 'webui/sensors/gyro':
        socketio.emit('receive_data_angle', {'data': message.payload.decode()}, namespace='/webui')
    if message.topic == 'webui/pdp/all': 
        socketio.emit('receive_data_all_currents', {'data': message.payload.decode()}, namespace='/webui')
    if message.topic == 'webui/sensors/arm': 
        socketio.emit('receive_data_arm', {'data': message.payload.decode()}, namespace='/webui')
    if message.topic == 'webui/sensors/ball':
        socketio.emit('receive_data_ball', {'data': message.payload.decode()}, namespace='/webui')

if __name__ == '__main__':
    print('Initializing Chili UI Flask server')
    socketio.run(app, host='0.0.0.0', debug=True)
