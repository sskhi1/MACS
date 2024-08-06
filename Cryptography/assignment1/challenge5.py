key = input().strip().encode()
text = input().strip().encode()

ciphertext = b''
for i in range(len(text)):
    ciphertext += bytes([text[i] ^ key[i % len(key)]])

print(ciphertext.hex())
