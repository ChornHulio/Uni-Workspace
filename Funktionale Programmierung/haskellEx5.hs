-- exercise sheet 5, exercise 37
at :: [a] -> Integer -> a
at (x:xs) 0 = x
at (x:xs) n = at xs (n-1)

-- exercise sheet 5, exercise 38
split :: Integer -> [a] -> ([a],[a])
split 0 xs = ([],xs)
split n [] = ([],[])
split n (x:xs) = (x:ys,zs)
	where (ys,zs) = split (n-1) xs

-- exercise sheet 5, exercise 39
unZip :: [(a,b)] -> ([a],[b])
unZip [] = ([],[])
unZip ((x,y):zs) = (x:xs,y:ys)
	where (xs,ys) = unZip zs


