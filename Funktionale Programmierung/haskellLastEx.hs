-- exercise sheet 1, exercise 4
binExp :: Integer -> Integer -> Integer
binExp x n | n == 0 = 1
           | n `mod` 2 == 0 = (x ^ (n `div` 2)) ^ 2
           | otherwise = x * (x ^ ((n-1) `div` 2)) ^ 2

-- exercise sheet 3, exercise 19
all :: (a -> Bool) -> [a] -> Bool
all p = foldr ((&&) . p) True

-- exercise sheet 3, exercise 23
doIt :: [[Int]] -> [Int]
doIt = filter(odd) . concat
