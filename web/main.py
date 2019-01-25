from flask import Flask, render_template
from flask_socketio import SocketIO, emit

app = Flask(__name__)
app.config['SECRET_KEY'] = 'frc6955!'
socketio = SocketIO(app)

num_clients = 0

@app.route('/')
def index():
    return render_template('index.html')
    
@socketio.on('connect_event', namespace='/webui')
def connect_event(payload):
    global num_clients
    num_clients += 1
    print("Conexiones hasta el momento:", num_clients)
    emit('client_connected', {
        'client_id': num_clients
    })

if __name__ == '__main__':
    print('hola')
    socketio.run(app, debug=True)