-- Blatt 1
-- Aufgabe 1
addmul :: (Integer,Integer,Integer) -> Integer
addmul (x,y,z) = (x + y) * z

add2 :: (Integer,Integer) -> Integer
add2 (x,y) = addmul (x,y,2)

square :: Integer -> Integer
square x = addmul (0,x,x)

{- nach Definitionen von addmul, square und Arithmetik gilt:

     addmul (x,y,x-y)
   = (x + y) * (x - y)
   = x * x - x * y + y * x - y * y
   = x * x - y * y
   = square x - square y
-}

-- Aufgabe 2
ggT :: (Integer,Integer) -> Integer
ggT (x,y) = if y == 0 then x else ggT (y,x `mod` y)

-- Aufgabe 3
abs :: Integer -> Integer
abs x = if x >= 0 then x else -x

circumference :: Double -> Double
circumference r = 2 * r * pi
  where pi = 3.1415926535897932384626433832795028841971694

area3 :: (Double,Double) -> Double
area3 (base,height) = 0.5 * base * height

fact :: Integer -> Integer
fact n = if n <= 1 then 1 else n * fact (n-1)

fib :: Integer -> Integer
fib n = if n <= 1 then n else fib (n-1) + fib (n-2)

binom :: (Integer,Integer) -> Integer
binom (n,k) = fact n `div` (fact k * fact (n-k))

-- Aufgabe 4
binExp :: (Integer,Integer) -> Integer
binExp (x,n) = if n == 0 then 1 else if n `mod` 2 == 0 then y else x * y
  where y = square (binExp (x,n `div` 2))

-- Aufgabe 5
next :: Integer -> Integer
next n = if n `mod` 2 == 0 then n `div` 2 else 3 * n + 1

steps :: Integer -> Integer
steps n = if n == 1 then 0 else steps (next n) + 1

sample5 :: Integer
sample5 = steps (2^1000-1)

-- Blatt 2
-- Aufgabe 6
data Day = Mo | Di | Mi | Do | Fr | Sa | So

isWorkDay :: Day -> Bool
isWorkDay Sa = False
isWorkDay So = False
isWorkDay x = True

isWeekend :: Day -> Bool
isWeekend x = not (isWorkDay x)

theDayAfter :: Day -> Day
theDayAfter Mo = Di
theDayAfter Di = Mi
theDayAfter Mi = Do
theDayAfter Do = Fr
theDayAfter Fr = Sa
theDayAfter Sa = So
theDayAfter So = Mo

isEqDay :: Day -> Day -> Bool
isEqDay Mo Mo = True
isEqDay Di Di = True
isEqDay Mi Mi = True
isEqDay Do Do = True
isEqDay Fr Fr = True
isEqDay Sa Sa = True
isEqDay So So = True
isEqDay x y = False

areAdjacent :: Day -> Day -> Bool
areAdjacent x y = if ( isEqDay x (theDayAfter y) ) then True
                  else if ( isEqDay y (theDayAfter x) ) then True
                  else False

-- Aufgabe 7
addmul' :: Integer -> Integer -> Integer -> Integer
addmul' x y z = (x + y) * z

add2' :: Integer -> Integer -> Integer
add2' x y = addmul' x y 2

ggT' :: Integer -> Integer -> Integer
ggT' x y | y == 0 = x -- guards
         | otherwise = ggT' y (x `mod` y)

area3' :: Double -> Double -> Double
area3' base height = 0.5 * base * height

binom' :: Integer -> Integer -> Integer
binom' n k = fact n `div` (fact k * fact (n-k))

binExp' :: Integer -> Integer -> Integer
binExp' x n | n == 0 = 1 
            | n `mod` 2 == 0 = y 
            | otherwise = x * y
  where y = square (binExp' x (n `div` 2))

abs' :: Integer -> Integer
abs' x = case x >= 0 of -- pattern matching
         True -> x 
         False -> -x

next' :: Integer -> Integer
next' n = case n `mod` 2 == 0 of
          True -> n `div` 2 
          False -> 3 * n + 1

steps' :: Integer -> Integer
steps' n = case n == 1 of
           True -> 0 
           False -> steps (next n) + 1
