data Tree a = Leaf
              | Node (Tree a) a (Tree a)
              deriving (Show)
foldT :: (b -> a -> b -> b) -> b -> Tree a -> b
foldT f e Leaf = e
foldT f e (Node l x r) = f (foldT f e l) x (foldT f e r)

-- example tree
exTree = Node (Node (Node Leaf 'd' Leaf) 'b' Leaf) 
		'a' 
		(Node (Node Leaf 'e' Leaf) 'c' (Node Leaf 'f' Leaf))

-- exercise sheet 7, exercise 50
preorder, postorder, inorder :: Tree a -> [a]
preorder = foldT f []
	where f l x r = x:(l ++ r)
postorder = foldT f []
	where f l x r = l ++ r ++ x:[]
inorder = foldT f []
	where f l x r = l ++ x:[] ++ r

elemS :: Eq a => a -> Tree a -> Bool
elemS x t = elem x (foldT f [] t)
	where f l x r = x:(l ++ r)

-- exercise sheet 7, exercise 51
depths :: Tree a -> Tree (a,Int)
depths t = depths' 1 t
depths' :: Int -> Tree a -> Tree (a,Int)
depths' _ Leaf = Leaf
depths' n (Node l x r) = Node (depths' (n+1) l) (x,n) (depths' (n+1) r)

-- exercise sheet 7, exercise 52
fulltree :: Int -> Tree Int
fulltree n = fulltree' n 1
fulltree' :: Int -> Int -> Tree Int
fulltree' 0 _   = Leaf
fulltree' n cur = Node (fulltree' (n-1) (cur*2)) cur (fulltree' (n-1) (cur*2+1))

-- exercise sheet 7, exercise 53
instance Eq a => Eq (Tree a) where
	Leaf == Leaf = True
	Node l1 x1 r1 == Node l2 x2 r2 = x1 == x2 
	                                 && l1 == l2 
	                                 && r1 == r2
	_ == _ = False
instance Ord a => Ord (Tree a) where
	Leaf <= _ = True
	_ <= Leaf = False
	Node l1 x1 r1 <= Node l2 x2 r2 = x1 <= x2
	                                 && l1 <= l2
	                                 && r1 <= r2
	_ <= _ = False
	
-- test
next :: Integer -> Integer
next x | even x = x `div` 2
       | otherwise = 3*x + 1
steps :: Integer -> Integer
steps 1 = 0
steps x = 1 + steps (next x)
