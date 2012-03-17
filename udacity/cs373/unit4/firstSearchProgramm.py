# ----------
# User Instructions:
# 
# Define a function, search() that takes no input
# and returns a list
# in the form of [optimal path length, x, y]. For
# the grid shown below, your function should output
# [11, 4, 5].
#
# If there is no valid path from the start point
# to the goal, your function should return the string
# 'fail'
# ----------

# Grid format:
#   0 = Navigable space
#   1 = Occupied space

grid = [[0, 0, 1, 0, 0, 0],
        [0, 0, 1, 0, 0, 0],
        [0, 0, 0, 0, 1, 0],
        [0, 0, 1, 1, 1, 0],
        [0, 0, 0, 0, 1, 0]]

init = [0, 0]
goal = [len(grid)-1, len(grid[0])-1] # Make sure that the goal definition stays in the function.

delta = [[-1, 0 ], # go up
        [ 0, -1], # go left
        [ 1, 0 ], # go down
        [ 0, 1 ]] # go right

delta_name = ['^', '<', 'v', '>']

cost = 1

def search():
    openList = [[0,init[0],init[1]]]
    while openList: # is not empty
        # search smallest g-value
        smallestG = 9999
        index = -1
        x = -1
        y = -1
        for i in range(len(openList)):
            if openList[i][0] < smallestG:
                smallestG = openList[i][0]
                y = openList[i][1]
                x = openList[i][2]
                index = i
                   
        # mark node as seen
        grid[y][x] = -1
        # remove node from openList
        del openList[index]
        # expand
        newList = expand(y,x)
        for n in newList:
            # check if goal
            if n[0] == goal[0] and n[1] == goal[1]:
                return [smallestG+1,n[0],n[1]] # sucessful
            openList.append([smallestG+1,n[0],n[1]])
    return 'fail'

def expand(y,x):
    oList = []
    for d in delta:
        if (y+d[0]) >= 0 and (y+d[0]) < len(grid) and (x+d[1]) >= 0 and (x+d[1]) < len(grid[0]):
            if grid[(y+d[0])][(x+d[1])] == 0:
                oList.append([y+d[0],x+d[1]])
    return oList

print search()
