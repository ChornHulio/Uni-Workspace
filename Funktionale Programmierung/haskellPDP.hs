elemIndex :: Eq a => a -> [a] -> Maybe Int
elemIndex x xs = case [i|(y,i) <- zip xs [0..], y==x] of
					[] -> Nothing
					(i:_) -> Just i

-- 6 a

zipWithA :: (a -> b -> c) -> [a] -> [b] -> [c]
zipWithA f [] _ = []
zipWithA f _ [] = []
zipWithA f (x:xs) (y:ys) = f x y : (zipWithA f xs ys)

zipWithB :: (a -> b -> c) -> [a] -> [b] -> [c]
zipWithB f xs ys = [f x y | (x,y) <- zip xs ys]

zipWithC :: (a -> b -> c) -> [a] -> [b] -> [c]
zipWithC f xs ys = map (test f) (zip xs ys)
test :: (a -> b -> c) -> (a,b) -> c
test f (x,y) = f x y

-- 6 b

isbn10A :: [Int] -> Int
isbn10A xs = isbnH1 xs 1 `mod` 11
isbnH1 :: [Int] -> Int -> Int
isbnH1 _ 10 = 0
isbnH1 (x:xs) n = x*n + isbnH1 xs (n+1)

isbn10B :: [Int] -> Int
isbn10B xs = sum (zipWithA (*) xs [1..]) `mod` 11 

-- 7 a

data BoolExpr = Value Bool
              | And BoolExpr BoolExpr
              | Not BoolExpr
eval :: BoolExpr -> Bool
eval (Value a) = a
eval (And x y) = (eval x) && (eval y)
eval (Not x) | eval x == True = False
           | otherwise = True 

-- 7 b,c
-- wtf

-- 8
gray :: Int -> [String]
gray 1 = ["0","1"]
gray n = [ '0':y | y <- gray (n-1)] ++ [ '1':y | y <- reverse (gray (n-1))]

-- 2 a
getNth :: [a] -> Int -> Maybe a
getNth [] _ = Nothing
getNth (x:xs) 0 = Just x
getNth (x:xs) n = getNth xs (n-1)

-- 2 b
getandremoveNth :: [a] -> Int -> (Maybe a,[a])
getandremoveNth xs n = getARNth xs [] n
getARNth :: [a] -> [a] -> Int -> (Maybe a,[a])
getARNth [] ys _ = (Nothing, ys)
getARNth (x:xs) ys 0 = (Just x, ys ++ xs)
getARNth (x:xs) ys n = getARNth xs (concat [ys,x:[]]) (n-1)

-- 3 a
data Leitung = Rohr Groesse Bool
               | Schlauch Groesse Leitung
               | Verteiler Leitung Leitung Leitung
data Groesse = B | C
	deriving (Eq) 

gesamtlaenge :: Leitung -> Int
gesamtlaenge (Schlauch B l) = 20 + gesamtlaenge l
gesamtlaenge (Schlauch C l) = 15 + gesamtlaenge l
gesamtlaenge (Verteiler x y z) = gesamtlaenge x 
                                 + gesamtlaenge y
                                 + gesamtlaenge z
gesamtlaenge _ = 0

bsp = Schlauch B (Verteiler (Schlauch C (Schlauch C (Rohr C True)))
                            (Schlauch B (Rohr B True))
                            (Rohr C False))




