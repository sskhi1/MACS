import math


def mod_inv(a, m):
    return pow(a, -1, m)


def build_left_table(b, g, h, p):
    left_table, left_val = {}, h
    left_table[h] = 0
    g_inverse = mod_inv(g, p)
    for x1 in range(1, b):
        left_val = (left_val * g_inverse) % p
        left_table[left_val] = x1
    return left_table


def lookup_result(b, g, p, left_table):
    g_to_b, right_val = pow(g, b, p), 1
    for x0 in range(b):
        if right_val in left_table:
            x1 = left_table[right_val]
            return x0, x1
        right_val = (right_val * g_to_b) % p


# Find x such that g^x = h (mod p)
# 0 <= x <= max_x
def discrete_log(p, g, h, max_x):
    b = int(math.ceil(math.sqrt(max_x)))

    left_table = build_left_table(b, g, h, p)

    x0, x1 = lookup_result(b, g, p, left_table)
    return x0 * b + x1


def main():
    p = int(input().strip())
    g = int(input().strip())
    h = int(input().strip())
    max_x = 1 << 40  # 2^40

    dlog = discrete_log(p, g, h, max_x)
    print(dlog)


if __name__ == '__main__':
    main()
