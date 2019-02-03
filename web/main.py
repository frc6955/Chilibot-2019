from flask import Flask, render_template
from flask_socketio import SocketIO, emit
from flask_mqtt import Mqtt 
import random
import threading


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

threadsim = None
thread_lock = threading.Lock()
num_clients = 0
refresh_period = 0.1

def simular_evento():
    while True:
        numero = random.randint(0, 12)
        # Normal blocking functions can't be used in an async SocketIO app
        # For future reference, view the Flask-SocketIO example application:
        # https://github.com/miguelgrinberg/Flask-SocketIO/blob/master/example/app.py
        # socketio.emit("receive_data", {"data": numero}, namespace='/webui')
        socketio.sleep(refresh_period)

@app.route('/')
def index():
    return render_template('index.html')
    
@socketio.on('connect', namespace='/webui')
def connect_event():
    global num_clients, threadsim
    with thread_lock:
        if threadsim is None:
            threadsim = socketio.start_background_task(simular_evento)
    mqtt.subscribe('webui/#', qos=0)
    num_clients += 1
    print("Conexiones hasta el momento:", num_clients)
    emit('connection_confirmed', {
        'client_id': num_clients
    }, namespace='/webui')

@mqtt.on_message()
def handle_mqtt_messages(client, userdata, message):
    if message.topic == 'webui/battery/voltage':
        socketio.emit('receive_data_battery', {'data': message.payload.decode()}, namespace='/webui')
    if message.topic == 'webui/driverstation/matchtime':
        socketio.emit('receive_data_time', {'data': message.payload.decode()}, namespace='/webui')


if __name__ == '__main__':
    print('hola')
    socketio.run(app, host='0.0.0.0', debug=True)