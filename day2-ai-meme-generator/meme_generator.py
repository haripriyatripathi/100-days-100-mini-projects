import gradio as gr
from PIL import Image, ImageDraw, ImageFont
from transformers import pipeline, BlipProcessor, BlipForConditionalGeneration
import torch

# 1️⃣ Load GPT-2 text generator
generator = pipeline("text-generation", model="gpt2")

# 2️⃣ Load BLIP image captioning model
processor = BlipProcessor.from_pretrained("Salesforce/blip-image-captioning-base")
model = BlipForConditionalGeneration.from_pretrained("Salesforce/blip-image-captioning-base")

# 3️⃣ Function to generate meme
def generate_meme(image):
    # Generate image description using BLIP
    inputs = processor(images=image, return_tensors="pt")
    out = model.generate(**inputs)
    description = processor.decode(out[0], skip_special_tokens=True)
    
    # Generate funny caption using GPT-2
    prompt = f"Write a short, funny meme caption for this: {description}"
    caption = generator(prompt, max_length=50, num_return_sequences=1)[0]['generated_text']
    
    # Add caption to image
    img = image.convert("RGB")
    draw = ImageDraw.Draw(img)
    width, height = img.size
    try:
        font = ImageFont.truetype("arial.ttf", 30)
    except:
        font = ImageFont.load_default()
    
    # Wrap text if too long
    lines = []
    words = caption.split()
    line = ""
    for word in words:
        if font.getsize(line + " " + word)[0] > width - 20:
            lines.append(line)
            line = word
        else:
            line += " " + word if line else word
    lines.append(line)
    
    # Draw text on image
    y_text = height - 50 * len(lines)
    for line in lines:
        draw.text((10, y_text), line, fill="white", font=font, stroke_width=2, stroke_fill="black")
        y_text += 50
    
    return img

# 4️⃣ Gradio interface
iface = gr.Interface(
    fn=generate_meme,
    inputs=gr.Image(type="pil"),
    outputs=gr.Image(type="pil"),
    title="😹 AI Meme Generator",
    description="Upload any image, and AI will add a funny caption for a meme!",
    examples=[],
    allow_flagging="never",
    live=False
)

iface.launch()
