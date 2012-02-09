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

-- exercise sheet 3, exercise 21
intersperse :: a -> [a] -> [a]
intersperse _ (y:[]) = [y]
intersperse x (y:ys) = y:x:(intersperse x ys)

-- exercise sheet 3, exercise 22
-- perfect numbers
perfect :: Int -> [Int]
perfect n = [x | x <- [1..n], (sum [y | y <- [1..(x-1)], x`mod` y == 0]) == x]

-- exercise sheet 3, exercise 23
-- write the following equation with map, concat and filter
-- [x | xs <- xss, x <- xs, odd x]
oddFromXSS :: [[Int]] -> [Int]
oddFromXSS xss = filter odd (concat xss)

-- exercise sheet 3, exercise 24
-- same output? efficiency?
listComA :: (a -> Bool) -> [a] -> [b] -> [(a,b)]
listComA p xs ys = [(x,y) | x <- xs, p x, y <- ys]
listComB :: (a -> Bool) -> [a] -> [b] -> [(a,b)]
listComB p xs ys = [(x,y) | x <- xs, y <- ys, p x]
-- same output! listComA is more efficient!

-- exercise sheet 3, exercise 25
-- calculations with vectors and matrixes
type Vector = [Int]
type Matrix = [Vector]
-- dot product
dot_product :: Vector -> Vector -> Int
dot_product [] _ = 0 
dot_product _ [] = 0 
dot_product (v:vs) (w:ws) = v*w + dot_product vs ws
-- mulitply a matrix with a column vector
mat_x_vec :: Matrix -> Vector -> Vector
mat_x_vec mss vs = [ dot_product ms vs | ms <- mss ]
-- transpose a matrix (only symmetrical ones)
mat_transp :: Matrix -> Matrix
mat_transp mss | head mss == [] = []
               | otherwise = [ head ms | ms <- mss ] : (mat_transp [ tail ms | ms <- mss ])
-- mulitply two matrixes
mat_x_mat :: Matrix -> Matrix -> Matrix
mat_x_mat mss nss = mat_transp [ mat_x_vec mss ns | ns <- mat_transp nss] 


