-- replicate with list comprehension
replicate' :: Int -> a -> [a]
replicate' n t = [ t | _ <- [1..n]]

-- all possible (natural) numbers till n, which can be interpreted as pythargorean
pyths :: Int -> [(Int,Int,Int)]
pyths n = [ (x,y,z) | x <- [1..n], y <- [1..n], z <- [1..n], x^2 + y^2 == z^2]

-- perfect numbers
perfects :: Int -> [Int]
perfects n = [ x | x <- [1..n], isPerfect x]
isPerfect :: Int -> Bool
isPerfect x = x == (foldr (+) 0 (facts x))
facts :: Int -> [Int]
facts n = [ x | x <- [1..(n-1)], n `mod` x == 0] 

-- represent a two generators comprehension with two one generator comprehensions
singeGen :: Int -> Int -> [(Int,Int)]
singeGen n1 n2 = [ (x,y) | x <- [1..n1], y <- [1..n2]]
doubleGen :: Int -> Int -> [(Int,Int)]
doubleGen n1 n2 = concat [[(x,y)|y<-[1..n2]] |x<-[1..n1]]

-- positions of something in a list (using function find)
positions :: Eq a => a -> [a] -> [Int]
positions x xs = find x (zip xs [1..(length xs)])
find :: Eq a => a -> [(a,Int)] -> [Int]
find k t = [ v | (k',v) <- t, k==k']

-- scalar product with list comprehension
scalarproduct :: Num a => [a] -> [a] -> a
scalarproduct xs ys = sum [ x * y | (x,y) <- zip xs ys]
