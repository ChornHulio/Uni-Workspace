-- exercise sheet 6, exercise 44
nub :: Eq a => [a] -> [a]
nub [] = []
nub (x:xs) = x:(nub [ z | z <- xs, x /= z])

-- exercise sheet 6, exercise 45
type Set a = [a]
union, intersect, (\\),(\/) :: Eq a => Set a -> Set a -> Set a

union [] ys = ys
union (x:xs) ys = x:(union xs [z | z <- ys, x /= z])

intersect [] _ = []
intersect (x:xs) ys | elem x ys = x : (intersect xs ys)
                    | otherwise = intersect xs ys

(\\) [] _ = []         -- A without B
(\\) (x:xs) ys | elem x ys = (\\) xs [z | z <- ys, x /= z]
               | otherwise = x : ((\\) xs ys)

(\/) [] ys = ys       -- union without intersect
(\/) (x:xs) ys | elem x ys = (\/) xs [z | z <- ys, x /= z]
               | otherwise = x : ((\/) xs ys)

-- exercise sheet 6, exercise 46
data Nat = Zero | Succ Nat
instance Eq Nat where
	Zero   == Zero   = True
	Succ x == Succ y = x == y
	_      == _      = False
instance Ord Nat where
	Zero   <=  x = True
	Succ x <= Succ y = x <= y
	_      <=  _     = False

data Error a = Ok a | Fail String
instance Eq a => Eq (Error a) where
	Ok x   == Ok y   = x == y
	Fail x == Fail y = x == y
	_      == _      = False
instance Ord a => Ord (Error a) where
 	Ok x   <= Ok y   = x <= y
 	Fail x <= Fail y = x <= y
 	Ok _   <= Fail _ = True
 	_      <= _      = False

-- exercise sheet 6, exercise 49

type Text  = String
type Zeile = String
type Wort  = String

sample :: Text
sample = "\nDies ist ein \n\n\n Text\n\n"

zeilen :: Text -> [Zeile]
zeilen s = case span (/='\n') s of
             ("",""  ) -> []
             (s1,""  ) -> [s1]
             (s1,_:s2) -> s1 : zeilen s2

text :: [Zeile] -> Text
text = concat . map (++"\n")

woerter :: Zeile -> [Wort]
woerter s = case span (/=' ') s of
              ("","") -> []
              (s1,"") -> [s1]
              ("",_:s2) -> woerter s2
              (s1,_:s2) -> s1 : woerter s2

zeile :: [Wort] -> Zeile
zeile [] = []
zeile (x:xs) = x ++ " " ++ zeile xs

wc :: Text -> Int
wc s = sum (map (length . woerter) (zeilen s))

clean :: Text -> Text
clean = text . filter (not . null)
      . map (zeile . woerter) . zeilen
