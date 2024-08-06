import string

letterFrequency = {'E': 12.0,
                   'T': 9.10,
                   'A': 8.12,
                   'O': 7.68,
                   'I': 7.31,
                   'N': 6.95,
                   'S': 6.28,
                   'R': 6.02,
                   'H': 5.92,
                   'D': 4.32,
                   'L': 3.98,
                   'U': 2.88,
                   'C': 2.71,
                   'M': 2.61,
                   'F': 2.30,
                   'Y': 2.11,
                   'W': 2.09,
                   'G': 2.03,
                   'P': 1.82,
                   'B': 1.49,
                   'V': 1.11,
                   'K': 0.69,
                   'X': 0.17,
                   'Q': 0.11,
                   'J': 0.10,
                   'Z': 0.07
                   }

hex_encoded_string = input().strip()
hex_bytes = bytes.fromhex(hex_encoded_string)


def score_plaintext(text):
    score = 0
    for char in text:
        if char in string.ascii_lowercase:
            score += letterFrequency[char.upper()]
        elif char == ' ':
            score += 13.0
    return score


best_score = 0
result = ""
for key in range(256):
    plaintext = bytes([byte ^ key for byte in hex_bytes]).decode('utf-8', 'ignore')
    current_score = score_plaintext(plaintext)
    if current_score > best_score:
        best_score = current_score
        result = plaintext

print(result)
