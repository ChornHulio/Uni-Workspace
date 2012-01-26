-- exponentiation with recursion
pow :: Int -> Int -> Int
pow 0 _ = 0
pow m 0 = 1
pow m n = m * (pow m (n - 1))

-- and with recursion
and' :: [Bool] -> Bool
and' [] = True
and' (True:xs) = and xs
and' (False:xs) = False

-- concat with recursion
concat' :: [[a]] -> [a]
concat' = foldr (++) []

