# import the necessary packages
from PIL import Image
import pytesseract
import cv2
import os

# https://miai.vn/2019/08/22/ocr-nhan-dang-van-ban-tieng-viet-voi-tesseract-ocr/

pytesseract.pytesseract.tesseract_cmd = 'C:/Program Files/Tesseract-OCR/tesseract.exe'

def read_image():
    image = cv2.imread("image_recognize.jpg")
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

    gray = cv2.threshold(gray, 0, 255, cv2.THRESH_BINARY | cv2.THRESH_OTSU)[1]

    # Ghi tạm ảnh xuống ổ cứng để sau đó apply OCR
    filename = "{}.png".format(os.getpid())
    cv2.imwrite(filename, gray)

    # Load ảnh và apply nhận dạng bằng Tesseract OCR
    text = pytesseract.image_to_string(Image.open(filename), lang='vie')

    # Xóa ảnh tạm sau khi nhận dạng
    os.remove(filename)

    # In dòng chữ nhận dạng được
    return text
