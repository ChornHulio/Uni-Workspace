-- exercise sheet 2, exercise 6
-- weekdays
data Day = Mon | Tue | Wed | Thu | Fri | Sat | Sun
isWorkDay :: Day -> Bool
isWorkDay Sat = False
isWorkDay Sun = False
isWorkDay _ = True
isWeekend :: Day -> Bool
isWeekend x = not (isWorkDay x)
theDayAfter :: Day -> Day
theDayAfter Mon = Tue
theDayAfter Tue = Wed
theDayAfter Wed = Thu
theDayAfter Thu = Fri
theDayAfter Fri = Sat
theDayAfter Sat = Sun
theDayAfter Sun = Mon
areAdjacent :: Day -> Day -> Bool
areAdjacent x y = (sameDay (theDayAfter x) y) || (sameDay (theDayAfter y) x)
sameDay :: Day -> Day -> Bool
sameDay Mon Mon = True
sameDay Tue Tue = True
sameDay Wed Wed = True
sameDay Thu Thu = True
sameDay Fri Fri = True
sameDay Sat Sat = True
sameDay Sun Sun = True
sameDay _ _ = False

-- exercise sheet 2, exercise 7
addmul :: Int -> Int -> Int -> Int
addmul x y z = (x+y)*z
add2 :: Int -> Int -> Int
add2 x y = (x+y)*2
ggT :: Int -> Int
ggT x = ggT' x (x-1)
ggT' :: Int -> Int -> Int
ggT' x d | (x `mod` d) == 0 = d
         | otherwise = ggT' x (d-1)

-- exercise sheet 2, exercise 8
applyM :: (a -> b) -> Maybe a -> Maybe b
applyM f Nothing = Nothing
applyM f (Just x) = Just (f x)

-- exercise sheet 2, exercise 9
data Nat = Zero | Succ Nat
	deriving Show
zero :: Nat
zero = Zero
one :: a -> Nat
one x = Succ Zero
apply :: (a -> b) -> a -> b
apply f x = f x
always :: a -> b -> b
always x y = y
subst :: (a -> b -> c) -> (a -> b) -> a -> c
subst f g x = f x (g x)

-- exercise sheet 2, exercise 10
equals :: Nat -> Nat -> Bool
equals Zero Zero = True
equals (Succ x) (Succ y) = equals x y
equals _ _ = False

lessthan :: Nat -> Nat -> Bool
lessthan Zero Zero = False
lessthan Zero _ = True
lessthan (Succ x) (Succ y) = lessthan x y
lessthan _ _ = False

int :: Nat -> Int
int Zero = 0
int (Succ x) = int x + 1

nat :: Int -> Nat
nat 0 = Zero
nat x = Succ (nat (x-1))

foldn :: (a -> a) -> a -> Int -> a
foldn f e 0 = e
foldn f e n = f (foldn f e (n-1))

fib :: Int -> Int
fib = fst . foldn f (0,1)
	where f (x,y) = (y,x+y)
{-
	fib 4
	= (fst . foldn f (0,1)) 4
	= fst (foldn f (0,1) 4)
	= fst (f (foldn f (0,1) 3))
	= fst (f (f (foldn f (0,1) 2)))
	= fst (f (f (f (foldn f (0,1) 1))))
	= fst (f (f (f (f (foldn f (0,1) 0)))))
	= fst (f (f (f (f (0,1)))))
	= fst (f (f (f (1,1))))
	= fst (f (f (1,2)))
	= fst (f (2,3))
	= fst (3,5)
	= 3
-}

fact :: Int -> Int
fact = snd . foldn f (0,1)
	where f (x,y) = (x+1,(x+1)*y)
{-
	fact 4
	= (snd . foldn f (0,1)) 4
	= snd (foldn f (0,1) 4)
	= snd (f (foldn f (0,1) 3))
	= snd (f (f (foldn f (0,1) 2)))
	= snd (f (f (f (foldn f (0,1) 1))))
	= snd (f (f (f (f (foldn f (0,1) 0)))))
	= snd (f (f (f (f (0,1)))))
	= snd (f (f (f (1,1))))
	= snd (f (f (2,2)))
	= snd (f (3,6))
	= snd (4,24)
	= 24
-}

-- exercise sheet 2, exercise 12
f' :: Int -> Int
f' x = x
g' :: Int -> Int -> Int
g' x y = x + y
h' :: Int -> Int -> Int
h' x y = f' (g' x y)

-- exercise sheet 2, exercise 14
lastP :: (Nat -> Bool) -> Int -> Nat
lastP p = g . foldn f e
	where e = (Zero,Zero)
	      f (x,y) = (Succ x, if p (Succ x) then (Succ x) else y)
	      g = snd

-- exercise sheet 2, exercise 15
{-
	curry (uncurry f) = f
	= (curry . uncurry) f
	uncurry (curry g) = g
	= (uncurry . curry) g
	flip (flip h) = h
	= (flip . flip) h
-}

-- exercise sheet 2, exercise 16
f1 :: (a,b) -> (b,a)
f1 (x,y) = (y,x)
