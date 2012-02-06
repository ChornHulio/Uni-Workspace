-- exercise sheet 3, exercise 17
-- zipp two lists to one tuple-list, cut the longer list
zipp :: [a] -> [b] -> [(a,b)]
zipp [] _ = []
zipp _ [] = []
zipp (x:xs) (y:ys) = (x,y):(zipp xs ys)
-- zippWith is mapping a function to each pair of zipp
zippWith :: (a -> b -> c) -> [a] -> [b] -> [c]
zippWith f [] _ = []
zippWith f _ [] = []
zippWith f (x:xs) (y:ys) = (f x y):(zippWith f xs ys)
-- define zipp as instance of zippWith
zipp' :: [a] -> [b] -> [(a,b)]
zipp' xs ys = zippWith (,) xs ys

-- exercise sheet 3, exercise 18
-- length as recursive function
length' :: [a] -> Int
length' [] = 0
length' (x:xs) = 1 + (length' xs)
-- filter as recursive function
filter' :: Eq a => a -> [a] -> [a]
filter' _ [] = []
filter' x (y:ys) | x == y = y:(filter' x ys)
                 | otherwise = filter' x ys

-- exercise sheet 3, exercise 19
-- logical all quantifier
all' :: Eq a => a -> [a] -> Bool
all' _ [] = True
all' p (x:xs) | p == x = all' p xs
              |otherwise = False

-- exercise sheet 3, exercise 20
-- copy a n-times
copy :: Int -> a -> [a]
copy 0 _ = []
copy n x = x:(copy (n-1) x)
{-
	length (copy 0 x) 
	= length []
	= 0
	length (copy (n+1) x)
	= length (x:(copy n x))
	= 1 + length (copy n x)
	= 1 + n
	
	alle (==x) (copy 0 x)
	= alle (==x) []
	= True
	alle (==x) (copy (n+1) x)
	= (x==x) && alle (==x) (copy n x)
	= True && alle (==x) (copy n x)
	= alle (==x) (copy n x)
	= True
-}
