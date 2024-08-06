import sys

from oracle import Oracle_Connect, Oracle_Disconnect, Mac

BLOCK_SIZE = 16


def forge(text, tag):
    for i in range(0, len(text), 32):
        b1 = text[i: i + BLOCK_SIZE]
        b2 = text[i + BLOCK_SIZE: i + BLOCK_SIZE * 2]

        b = b1.encode()
        message = ''.join([chr(tag[j] ^ b[j]) for j in range(min(len(tag), len(b)))]) + b2
        mlength = len(message)
        tag = Mac(message, mlength)
    return tag.hex()


def main():
    if len(sys.argv) != 2:
        print("Usage: python decipher.py encrypted_text_file", file=sys.stderr)
        sys.exit(1)

    file_name = sys.argv[1]
    with open(file_name) as file:
        text = file.read().strip()

    Oracle_Connect()
    print(forge(text, bytes(BLOCK_SIZE)))
    Oracle_Disconnect()


if __name__ == "__main__":
    main()
