# ----------
# User Instructions:
# 
# Create a function compute_value() which returns
# a grid of values. Value is defined as the minimum
# number of moves required to get from a cell to the
# goal. 
#
# If it is impossible to reach the goal from a cell
# you should assign that cell a value of 99.

# ----------

grid = [[0, 1, 0, 0, 0, 0],
        [0, 1, 0, 0, 0, 0],
        [0, 1, 0, 0, 0, 0],
        [0, 1, 0, 0, 0, 0],
        [0, 0, 0, 0, 1, 0]]

init = [0, 0]
goal = [len(grid)-1, len(grid[0])-1]

delta = [[-1, 0 ], # go up
         [ 0, -1], # go left
         [ 1, 0 ], # go down
         [ 0, 1 ]] # go right

delta_name = ['^', '<', 'v', '>']

cost_step = 1 # the cost associated with moving from a cell to an adjacent one.

# ----------------------------------------
# insert code below
# ----------------------------------------

def compute_value():
	value = [[99 for row in range(len(grid[0]))] for col in range(len(grid))]
	openL = [[0,goal[0],goal[1]]]
	
	while openL:
		openL.sort()
		openL.reverse()
		next = openL.pop()
		
		x = next[1]
		y = next[2]
		v = next[0] # value
		value[x][y] = v
		
		for d in delta:
			x2 = x + d[0]
			y2 = y + d[1]
			v2 = v + cost_step
			if x2 >= 0 and x2 < len(grid) and y2 >= 0 and y2 < len(grid[0]):
				if grid[x2][y2] == 0 and value[x2][y2] > v2:
					openL.append([v2,x2,y2])	
	
	for i in value:
		print i

	return value

compute_value()


