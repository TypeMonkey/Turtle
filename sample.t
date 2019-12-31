(data YoBoi 
  (
    a:DatBoi
    b:string
    c:bool
  )
)

(data DatBoi 
  (
    a:int
    b:int
    c:bool
  )
)

(
 def noArgs (n:DatBoi) : int
 (get n a)
)

(
  def noReturn : void 
)

(
 def sum (n:int) : int
  (
   if (= n 0) 0 (+ n (sum (dec n)))
  )
  (
    def nested (n:int) : int
    n
  )
  (nested n)
)
(
 def iter (n:int) : int
 (
  let ((i:int 0) (r:int 0))
  (
   while (< i n)
   (set r (inc r))
   (set i (inc i))
  )
  r
 )
)
(println (toStr (iter 5)) )
(println (toStr (sum 5)) )
(println (toStr (- -50 -25)))
(println (+ 'hello' 5))
(noArgs (DatBoi 10 5 false))
