import sys
from oracle import Oracle_Connect, Oracle_Disconnect, Oracle_Send

BLOCK_SIZE = 16


def decipher_text(encrypted_text):
    pads = {
        i: [0] * (BLOCK_SIZE - (i + 1)) + [i + 1] * (i + 1) for i in range(BLOCK_SIZE)
    }
    blocks_to_decipher = [
        int(encrypted_text[i:i + 2], BLOCK_SIZE) for i in range(0, len(encrypted_text), 2)
    ]

    return decipher_helper(pads, blocks_to_decipher)


def decipher_helper(pads, blocks_to_decipher):
    result_blocks = []
    remaining_blocks = len(blocks_to_decipher) // BLOCK_SIZE
    while remaining_blocks >= 2:
        curr = [0] * BLOCK_SIZE
        for i in range(BLOCK_SIZE, 0, -1):
            found = False
            for j in range(256):
                curr[i - (BLOCK_SIZE + 1)] = j
                if j != BLOCK_SIZE + 1 - i:
                    ctext_t = blocks_to_decipher.copy()
                    ctext = ctext_t
                    for k in range(BLOCK_SIZE):
                        ctext[len(ctext) + k - BLOCK_SIZE * 2] ^= (curr[k] ^ pads[BLOCK_SIZE - i][k])
                    if Oracle_Send(ctext, remaining_blocks):
                        found = True
                        break
            if not found:
                curr[i - (BLOCK_SIZE + 1)] = BLOCK_SIZE + 1 - i
        result_blocks = curr + result_blocks
        blocks_to_decipher = blocks_to_decipher[:-BLOCK_SIZE]
        remaining_blocks -= 1
    last_idx = len(result_blocks) - result_blocks[-1]
    return bytes(result_blocks[:last_idx]).decode()


def main():
    if len(sys.argv) != 2:
        print("Usage: python decipher.py encrypted_text_file", file=sys.stderr)
        sys.exit(1)

    file_name = sys.argv[1]
    with open(file_name) as file:
        encrypted_text = file.read().strip()

    Oracle_Connect()
    print(decipher_text(encrypted_text))
    Oracle_Disconnect()


if __name__ == "__main__":
    main()
