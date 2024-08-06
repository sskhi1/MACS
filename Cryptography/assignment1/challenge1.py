# add your imports here
import base64

# reading input (don't forget strip in other challenges!)
str_hex = input().strip()
hex_bytes = bytes.fromhex(str_hex)

str_base64 = base64.b64encode(hex_bytes).decode()

print(str_base64)
