from flask import Flask, request, jsonify
import pickle
import numpy as np

app = Flask(__name__)

model = pickle.load(open('pipe.pkl', 'rb'))
df = pickle.load(open('df.pkl', 'rb'))

@app.route('/')
def home():
    return "Laptop Price Prediction API"

@app.route('/predict', methods=['POST'])
def predict():
    try:
        company = request.form.get('company',df['Company'].unique())
        typename = request.form.get('typename',df['TypeName'].unique())
        ram = int(request.form.get('ram'))
        weight = float(request.form.get('weight'))
        touchscreen = 1 if request.form.get('touchscreen') == 'Yes' else 0
        ips = 1 if request.form.get('ips') == 'Yes' else 0
        screen_size = float(request.form.get('screen_size'))
        resolution = request.form.get('resolution')
        cpubrand = request.form.get('cpubrand',df['Cpu brand'].unique())
        hdd = int(request.form.get('hdd'))
        ssd = int(request.form.get('ssd'))
        gpubrand = request.form.get('gpubrand',df['Gpu brand'].unique())
        os = request.form.get('os',df['os'].unique())
        X_res, Y_res = map(int, resolution.split('x'))
        ppi = ((X_res ** 2) + (Y_res ** 2)) ** 0.5 / screen_size
        input_query = np.array([[company, typename, ram, weight, touchscreen, ips, ppi, cpubrand, hdd, ssd, gpubrand, os]], dtype=object)
        predicted_price = model.predict(input_query)[0]
        
        return jsonify({'Predicted Price': str(int(np.exp(predicted_price)))})
    
    except Exception as e:
        return jsonify({'error': str(e)})

if __name__ == '__main__':
    app.run(debug=False)
