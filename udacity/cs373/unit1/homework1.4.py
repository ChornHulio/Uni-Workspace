colors = [['red', 'green', 'green', 'red' , 'red'],
          ['red', 'red', 'green', 'red', 'red'],
          ['red', 'red', 'green', 'green', 'red'],
          ['red', 'red', 'red', 'red', 'red']]

measurements = ['green', 'green', 'green' ,'green', 'green']


motions = [[0,0],[0,1],[1,0],[1,0],[0,1]]

sensor_right = 0.7

p_move = 0.8

def show(p):
    for i in range(len(p)):
        print p[i]

#DO NOT USE IMPORT
#ENTER CODE BELOW HERE
#ANY CODE ABOVE WILL CAUSE
#HOMEWORK TO BE GRADED
#INCORRECT

def sense(p, Z):
    q = []
    for i in range(len(p)):
        qX = []
        for j in range(len(p[0])):
            hit = (Z == colors[i][j])
            qX.append( p[i][j] * (hit * sensor_right + (1-hit) * (1-sensor_right)))
        q.append(qX)
    
    # sum
    s = 0.
    for i in range(len(q)):
        for j in range(len(q[i])):
            s = s + q[i][j]

    # normalize
    for i in range(len(q)):
        for j in range(len(q[0])):
            q[i][j] = q[i][j] / s
    return q

def move(p, U):
    # U[0] -> up (-1) or down (1)
    # U[1] -> left (-1) or right (1)
    q = []
    for i in range(len(p)): # y
        qX = []
        for j in range(len(p[0])): # x
            s = p_move * p[(i-U[0]) % len(p)][(j-U[1]) % len(p[0])]
            s += (1-p_move) * p[(i-U[0]-1) % len(p)][(j-U[1]-1) % len(p[0])]
            qX.append(s)
        q.append(qX)
    return q

# init p
p = []
nrOfFields = len(colors) * len(colors[0])
for i in range(len(colors)):
    q=[]
    for j in range(len(colors[0])):
        q.append(1./nrOfFields)
    p.append(q)

# go for it
for i in range(len(measurements)):
    p = move(p,motions[i])
    p = sense(p,measurements[i])

#Your probability array must be printed 
#with the following code.

show(p)
