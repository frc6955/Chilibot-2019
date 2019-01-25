from flask import Flask, render_template
from flask_socketio import SocketIO, emit
import random
import time
import threading

app = Flask(__name__)
app.config['SECRET_KEY'] = 'frc6955!'
socketio = SocketIO(app)

threadsim = None
num_clients = 0

def simular_evento():
    with app.app_context():
        while True:
            numero = random.randint(0, 500)
            print(numero)
            emit("receive_data", {"data": numero})
            time.sleep(0.020)

@app.route('/')
def index():
    return render_template('index.html')
    
@socketio.on('connect_event', namespace='/webui')
def connect_event(payload):
    global num_clients, threadsim
    if threadsim is None:
        threadsim = threading.Thread(target=simular_evento)
        threadsim.start()
    num_clients += 1
    print("Conexiones hasta el momento:", num_clients)
    emit('client_connected', {
        'client_id': num_clients
    })

if __name__ == '__main__':
    print('hola')
    socketio.run(app, debug=True)