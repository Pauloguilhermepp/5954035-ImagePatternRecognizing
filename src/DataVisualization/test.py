'''
[[61.333333333333336, 195.0, 3.0]
[[61.666666666666664, 194.0, 3.0]
'''
def fun(a, b, x):
    return a * x + b

def areaTrap(sb, h):
    return sb * h / 2

b1 = 195.0 + (61.333333333333336 * 3 + 195.0)
b2 = 194.0 + (61.666666666666664 * 3 + 194.0)
h = 3

a1 = areaTrap(b1, h)
a2 = areaTrap(b2, h)
print(a1 - a2)