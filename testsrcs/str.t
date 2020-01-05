(data Dat 
 (
   a:int
   b:bool
   c:string
   d:Dat
 )
)


(let ((a:int 0))
  ( while (< a 5)
    (println 'yooo')
    (set a (+ a 1))
  )
)