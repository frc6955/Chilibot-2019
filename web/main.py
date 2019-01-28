from flask import Flask, render_template
from flask_socketio import SocketIO, emit
import random
import threading

app = Flask(__name__)
app.config['SECRET_KEY'] = 'frc6955!'
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
        socketio.emit("receive_data", {"data": numero}, namespace='/webui')
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
    num_clients += 1
    print("Conexiones hasta el momento:", num_clients)
    emit('connection_confirmed', {
        'client_id': num_clients
    }, namespace='/webui')

if __name__ == '__main__':
    print('hola')
    socketio.run(app, host='0.0.0.0', debug=True)