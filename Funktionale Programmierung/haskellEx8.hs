-- exercise sheet 8, exercise 64
perfect :: [Int]
perfect = [ x | x <- [1..] , x == sum [ y | y <- [1..(x-1)] , x `mod` y == 0 ]]

-- exercise sheet 8, exercise 65
omn :: [a] -> [a]
omn xs = xs ++ omn xs

-- exercise sheet 8, exercise 66
uniq :: Eq a => [a] -> [a]
uniq [x] = [x]
uniq (x:xs) | x == head xs = uniq xs
            | otherwise = x:(uniq xs)
            
-- exercise sheet 8, exercise 67
quersumme :: Integer -> Integer
quersumme x | x < 10 = x
            | otherwise = x `mod` 10 + quersumme (x `div` 10)
quers :: Integer -> Integer
quers x | quersumme x < 10 = quersumme x
        | otherwise = quers (quersumme x)
prims :: [Integer]
prims = [x| x <- [1..], 0 == length [y| y <- [2..(x-1)], x `mod` y == 0]]
quersOfPrims :: [Integer]
quersOfPrims = map quers prims

-- exercise sheet 8, exercise 68
merge :: Ord a => [a] -> [a] -> [a]
merge [] ys = ys
merge xs [] = xs
merge (x:xs) (y:ys) | x <= y = x : merge xs (y:ys)
                    | otherwise = y : merge (x:xs) ys

mergeAll :: Ord a => [[a]] -> [a]
mergeAll [] = []
mergeAll ([]:xss) = mergeAll xss
mergeAll ((x:xs):xss) = x : (merge xs (mergeAll xss))

mergeAllUniq :: Ord a => [[a]] -> [a]
mergeAllUniq = uniq . mergeAll

primPots :: [[Integer]]
primPots = pots (tail prims)
pots :: [Integer] -> [[Integer]]
pots [] = []
pots (x:xs) = (map (x^) [1..]) : pots xs 
