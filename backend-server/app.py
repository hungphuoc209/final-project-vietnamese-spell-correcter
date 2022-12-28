from flask import Flask, flash, request, redirect, url_for
from flask_cors import CORS, cross_origin
from core_func import predictor as checker
import main
import py_ocr as ocr
import os


# Khởi tạo Flask Server Backend
app = Flask(__name__)
CORS(app)
app.config['CORS_HEADERS'] = 'Content-Type'
app.config['UPLOAD_FOLDER'] = ''

@app.route('/checking', methods=['GET'])
@cross_origin(origin='*')
def predict():
    image = request.args["text"]
    return checker.correct(image)


@app.route('/ocr', methods=['POST'])
@cross_origin(origin='*')
def getOrcText():
    image = request.files['image']
    print(image)
    image.save(os.path.join(
        app.config['UPLOAD_FOLDER'], 'image_recognize.jpg'))
    return ocr.read_image()


# Start Backend
if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5555)
    print("done")
