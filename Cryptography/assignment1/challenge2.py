buf1_hex = input().strip()
buf2_hex = input().strip()
buf1 = bytes.fromhex(buf1_hex)
buf2 = bytes.fromhex(buf2_hex)

if len(buf1) != len(buf2):
    print("invalid args")

result = bytearray(len(buf1))
for i in range(len(buf1)):
    result[i] = buf1[i] ^ buf2[i]

result_hex = result.hex()
print(result_hex)
