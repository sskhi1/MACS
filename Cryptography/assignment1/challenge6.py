import math
import string
from base64 import b64decode

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


def score_plaintext(text):
    score = 0
    for char in text:
        if char in string.ascii_lowercase:
            score += letterFrequency[char.upper()]
        elif char == ' ':
            score += 13.0
    return score


def ch3_sol(ciphertext_):
    best_score = 0
    result_i = 0
    for key_ in range(256):
        plaintext = bytes([ord(byte) ^ key_ for byte in ciphertext_]).decode('utf-8', 'ignore')
        current_score = score_plaintext(plaintext)
        if current_score > best_score:
            best_score = current_score
            result_i = key_
    return result_i


def ch5_sol(text_c5, key_c5):
    text_c5 = text_c5.encode()
    key_c5 = key_c5.encode()
    ciphertext_ = ''
    for j in range(len(text_c5)):
        ciphertext_ += chr(text_c5[j] ^ key_c5[j % len(key_c5)])

    return ciphertext_


def hamming_distance(s1, s2):
    return sum(bin(x ^ y).count('1') for x, y in zip(s1.encode(), s2.encode()))


def find_key_size(message_, min_size=2, max_size=40):
    min_distance = math.inf
    key_size_ = 0
    for size in range(min_size, max_size + 1):
        distance_sum = 0
        num_blocks = len(message_) // size - 1
        for m in range(num_blocks):
            block1 = message_[m * size:(m + 1) * size]
            block2 = message_[(m + 1) * size:(m + 2) * size]
            distance_sum += hamming_distance(block1, block2)
            # print(distance_sum)
        normalized_distance = distance_sum / size

        if normalized_distance < min_distance:
            min_distance = normalized_distance
            key_size_ = size
    return key_size_


# print(hamming_distance('this is a test', 'wokka wokka!!!'))
ciphertext = b64decode(input().strip()).decode()
KEY_SIZE = find_key_size(ciphertext)
# print(KEY_SIZE)
blocks = [''] * KEY_SIZE
for i in range(len(ciphertext)):
    blocks[i % KEY_SIZE] += ciphertext[i]

key = ''
for block in blocks:
    key += chr(ch3_sol(block))
# print(key)
result = ch5_sol(ciphertext, key)
print(result)
