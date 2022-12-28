from core_func import predictor
from core_func import add_noise as noise

text="nuôn nuôn lắng nge, luôn luôn thấu hiểu"
wrong_text = noise.add_noise(text,0.7,0.985)
print(wrong_text)
correct_text = predictor.correct(text)
print(correct_text)
