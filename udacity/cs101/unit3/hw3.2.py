def proc1(p):
	p[0] = p[1]
def proc2(p):
	p=p+[1]
def proc3(p):
	q=p
	p.append(3)
	q.pop
def proc4(p):
	q=[]
	while p:
		q.append(p.pop())
	while q:
		p.append(q.pop())

p=[1,2,3,4,5]
print p
proc1(p)
print p
p=[1,2,3,4,5]
proc2(p)
print p
p=[1,2,3,4,5]
proc3(p)
print p
p=[1,2,3,4,5]
proc4(p)
print p
