halve :: [a] -> ([a],[a])
halve xs = (take (length xs `div` 2) xs, drop (length xs `div` 2) xs)

-- safetail returns [] if list is empty
-- safetailC works with a conditional expression
safetailC :: [a] -> [a]
safetailC xs = if null xs then [] else tail xs
-- safetailG works with guarded equations
safetailG :: [a] -> [a]
safetailG xs | null xs = []
             | otherwise = tail xs
-- safetailP works with pattern matching
safetailP :: [a] -> [a]
safetailP (_:xs) = xs
safetailP [] = []

-- logical disjunction operator
or' :: Bool -> Bool -> Bool
or' False a = a
or' _ _ = True

-- logical conjunction operator with conditional expressions
and' :: Bool -> Bool -> Bool
and' a b = if a == True then b else False

-- multiplication with lambda
mul' :: Num a => a -> a -> a -> a
mul' = \a -> \b -> \c -> (a*b*c)

-- exercise sheet 1, exercise 1
addmul :: (Int, Int, Int) -> Int
addmul (x,y,z) = (x + y) * z
add2 :: (Int,Int) -> Int
add2 (x,y) = addmul(x,y,2)
square :: Int -> Int
square x = addmul (x,0,x)

-- exercise sheet 1, exercise 2
ggT :: (Int, Int) -> Int
ggT (x,y) = if y == 0 then x else ggT (y,x `mod` y)

-- exercise sheet 1, exercise 3
abs :: Integer -> Integer
abs x | x >= 0 = x
      | otherwise = -x
circumference :: Double -> Double
circumference r = 2 * pi * r
area3 :: Double -> Double -> Double
area3 w h = 0.5 * w * h
fact :: Integer -> Integer
fact x | x <= 1 = 1
       | otherwise = x * fact (x-1)
fibA :: Int -> [Int] -> [Int] -- returns the fibonacci series
fibA n xs | xs == [] = if n >= 1 then fibA n [1] else []
          | xs == [1] = if n >= 2 then fibA n [1,1] else [1]
          | length xs < n = fibA n ((head xs + head (tail xs)):xs)
          | otherwise = reverse xs
fibB :: Integer -> Integer -- return the fibonacci number
fibB n | n <= 1 = n 
       | otherwise = fibB (n-1) + fibB (n-2)
binom :: Integer -> Integer -> Integer
binom n k = fact n `div` (fact k * (fact (n - k)))

-- exercise sheet 1, exercise 4
expB :: Integer -> Integer -> Integer
expB x n | n == 0 = 1
         | n > 0 && (n `mod` 2 == 0) = (x ^ (n `div` 2)) ^ 2
         | otherwise = x * (x ^((n-1) `div` 2) ^ 2) -- wrong calc with 'expB 2 3', but why?
         
-- exercise sheet 1, exercise 5
next :: Integer -> Integer
next x | x `mod` 2 == 0 = x `div` 2
       | otherwise = 3 * x + 1
steps :: Integer -> Integer
steps x = steps' x 0
steps' :: Integer -> Integer -> Integer
steps' x s | x == 1 = s
           | otherwise = steps' (next x) (s + 1)
