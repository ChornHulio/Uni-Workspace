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

-- produce a list with n identical elements
replicate' :: Int -> a -> [a]
replicate' 0 _ = []
replicate' n x = x:(replicate' (n-1) x)

-- select the n'th element of a list
select :: [a] -> Int -> a
select (x:xs) 1 = x 
select (x:xs) n = (!!) xs (n-1) 

-- decide if a value is an element of a list
elem' :: Eq a => a -> [a] -> Bool
elem' _ [] = False
elem' x (y:ys) | x == y = True
              | otherwise = elem x ys

-- merge and order two sorted lists
mergeOrd :: Ord a => [a] -> [a] -> [a]
mergeOrd [] ys = ys
mergeOrd xs [] = xs
mergeOrd (x:xs) (y:ys) | x < y = x:(mergeOrd xs (y:ys))
                       | otherwise = y:(mergeOrd (x:xs) ys)
                       
-- merge sort
mergeSort :: Ord a => [a] -> [a]
mergeSort [x] = [x]
mergeSort xs = mergeOrd (mergeSort (fst (halve xs))) (mergeSort (snd (halve xs)))
halve :: [a] -> ([a],[a])
halve xs = (take (length xs `div` 2) xs, drop (length xs `div` 2) xs)

-- sum with recursion
sum' :: Integral a => [a] -> a
sum' xs = foldr (+) 0 xs

-- take with recursion
take' :: Int -> [a] -> [a]
take' 0 _ = []
take' _ [] = []
take' n (x:xs) = x:(take' (n-1) xs)

-- last element of a list
last' :: [a] -> a
last' [x] = x
last' (x:xs) = last xs
