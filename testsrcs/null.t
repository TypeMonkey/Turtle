(data Dat 
 (
   a:int
   b:bool
   c:string
   d:Dat
 )
)

(
  def f (a : int) (b : string) : string
  (
    if (= a 1)
    b
    (f (dec a) (println (+ b 'add')))
  )
)

(let ((b:int 5)) 
  (println (f b (println 'mead')) )
  (println 'hello world')
  (set b 
   (if true
      10
      5
    ) 
  )
  (println (toStr b))
  (println (toStr (dec 1)))
  
  (let (( c:Dat (null Dat) )) 
    (set c (Dat 10 true 'for dat' c ))
    (println (get c c))
    (println 'nestedlet')
  )
)
