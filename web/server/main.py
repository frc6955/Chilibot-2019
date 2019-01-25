from Flask import Flask, render_template
from flask_socketio import SocketIO, emit

app = Flask(__name__)
app.config['SECRET_KEY'] = 'frc6955!'
socketio = SocketIO(app)

num_clients = 0

@app.route('/')
def index():
    return render_template('../index.html')
    
@socketio.on('connect_event', namespace='/webui')
def connect_event(payload):
    num_clients += 1
    emit('client_connected', {
        'client_id': num_clients
    })



if __name__ == '__main__':
    socketio.run(app, debug=True)