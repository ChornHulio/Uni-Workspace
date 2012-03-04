n = 0
while True:
	n += 1
	x = n*5 + 1
	i = 5
	while i > 0:
		i -= 1
		if x % 4 != 0:
			x = -1
			break
		x = x / 4 * 5 +1
	if x > 0:
		print x
		break
