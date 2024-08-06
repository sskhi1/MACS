from oracle import *
from helper import *
import math


def mod_inv(a, m):
    return pow(a, -1, m)


def get_signature(m, n):
    b = int(math.sqrt(m))
    for i in range(2, b):
        if m % i == 0:
            return (Sign(m // i) * Sign(i) * mod_inv(Sign(1), n)) % n


def main():
    with open('project_3_2/input.txt', 'r') as f:
        n = int(f.readline().strip())
        msg = f.readline().strip()

    Oracle_Connect()

    m = ascii_to_int(msg)
    sigma = get_signature(m, n)

    print(sigma)

    Oracle_Disconnect()


if __name__ == '__main__':
    main()
